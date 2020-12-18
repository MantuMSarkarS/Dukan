package com.milkyway.dukan.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.milkyway.dukan.R;
import com.milkyway.dukan.activities.LoginActivity;
import com.milkyway.dukan.activities.MainActivity;
import com.milkyway.dukan.databinding.FragmentLoginBinding;
import com.milkyway.dukan.util.Session;

import org.json.JSONException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding mBinding;
    private FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthStateListener;
    public String mEmail, mPassword;
    private ProgressDialog bar;
    private GoogleSignInOptions gso;
    private static final String EMAIL = "email";
    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;
    private Session mSession;
    private FirebaseFirestore mFirestore;
    private String userId;
    private DocumentReference mReference;
    private static final int RC_SIGN_IN = 1;
    public LoginActivity activity;


    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        View view = mBinding.getRoot();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finishAffinity();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);

        mSession = new Session(getContext());
        mFirestore = FirebaseFirestore.getInstance();
       /* FacebookSdk.sdkInitialize(this.getContext());
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        callbackManager = CallbackManager.Factory.create();
       mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions); */
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEmail = mBinding.email.getText().toString();
        mPassword = mBinding.password.getText().toString();
        mAuth = FirebaseAuth.getInstance();
        /*if(mAuth.getCurrentUser() != null){
            login();
        }*/
        mBinding.login.setOnClickListener(v -> {
            login();
        });
        mBinding.newUserText.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new RegisterFragment()).commit());
       // mBinding.facebookLogin.setOnClickListener(v -> facebookLogin());
       // mBinding.googleLogin.setOnClickListener(v -> signInWithGoogle());

    }

    private void login() {
        bar = new ProgressDialog(getActivity());
        bar.setCancelable(false);
        bar.setMessage("Please wait...");
        bar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mEmail = mBinding.email.getText().toString().trim();
        mPassword = mBinding.password.getText().toString().trim();
        if (TextUtils.isEmpty(mEmail)) {
            mBinding.email.setError("Email is Required.");
            mBinding.email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mPassword)) {
            mBinding.password.setError("Password is Required.");
            mBinding.password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mPassword) | TextUtils.isEmpty(mEmail)) {
            Toast.makeText(getContext(), "Email & Password fields are empty! ", Toast.LENGTH_SHORT).show();
        } else {
            bar.show();
            mAuth.signInWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener((Activity) requireContext(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        bar.dismiss();
                        Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), MainActivity.class));
                        requireActivity().finishAffinity();
                    } else {
                        bar.dismiss();
                        Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(activity, MainActivity.class));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (LoginActivity) activity;
    }


  /*  private void signInWithGoogle() {
        mSession.set("logged_in","true");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }*/

  /*  private void facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(), "Facebook Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError: " + exception.getMessage().toString());
            }
        });
    }

    private void handleFacebookToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }*/

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(getContext(), MainActivity.class));
            Toast.makeText(getContext(), "Authentication Successful.",
                    Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

       /* if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }*/
    }
  /*  private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                mSession.set("logged_in","true");
                Intent intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(requireContext(), "Signed In Successfully", Toast.LENGTH_SHORT).show();
            }
        } catch (ApiException e) {
            Log.w("TAG", "SignInResult failed code=" + e.getStatusCode());
        }
    }*/
}