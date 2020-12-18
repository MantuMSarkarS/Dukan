package com.milkyway.dukan.fragments;

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
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.milkyway.dukan.R;
import com.milkyway.dukan.activities.MainActivity;
import com.milkyway.dukan.databinding.FragmentRegisterBinding;
import com.milkyway.dukan.util.Session;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RegisterFragment extends Fragment {

    public static final String TAG="RegisterFragment";
    private static final int RC_SIGN_IN = 1;
    private FragmentRegisterBinding mBinding;
    public String mName, mEmail, mMobile, mPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog bar;
    private Session mSession;
    private FirebaseFirestore mFirestore;
    private String userId;
    private DocumentReference mReference;
    private GoogleSignInOptions gso;
    private static final String EMAIL = "email";
    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        View view = mBinding.getRoot();
        FacebookSdk.sdkInitialize(getApplicationContext());
        mSession=new Session(getContext());
        mFirestore = FirebaseFirestore.getInstance();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        callbackManager = CallbackManager.Factory.create();
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new LoginFragment()).commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
        mBinding.facebookLogin.setOnClickListener(v->{
            mSession.set("login","facebook");
            signInWithFacebook();
        });
        mBinding.googleLogin.setOnClickListener(v->{
            mSession.set("login","google");
            signInWithGoogle();
        });
        mBinding.alreadyReg.setOnClickListener(v->{
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new LoginFragment()).commit();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        mBinding.backRegLayout.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new LoginFragment()).commit());
        mBinding.regButton.setOnClickListener(v ->{
            mSession.set("login","manual");
            registerWithMobileAndPassword();
        } );


    }

    private void signInWithGoogle() {
        mSession.set("logged_in","true");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    String facebook,google,manual;
    @Override
    public void onStart() {
        super.onStart();
        google=mSession.get("google");
        manual=mSession.get("manual");
      /*
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (account != null) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }*/
    }


    private void  signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "onError: " + exception.getMessage().toString());
            }
        });
    }

    private void handleFacebookToken( AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                startActivity(new Intent(getContext(), MainActivity.class));
                                Toast.makeText(getContext(), "Authentication Successful.",
                                        Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
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
    }

    private void registerWithMobileAndPassword() {
        bar = new ProgressDialog(getActivity());
        bar.setCancelable(false);
        bar.setMessage("Please wait...");
        bar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mName = Objects.requireNonNull(mBinding.username.getEditText().getText()).toString().trim();
        mEmail = Objects.requireNonNull(mBinding.email.getEditText().getText()).toString().trim();
        mMobile = Objects.requireNonNull(mBinding.mobile.getEditText().getText()).toString().trim();
        mPassword = Objects.requireNonNull(mBinding.password.getEditText().getText()).toString().trim();

        if (!textInputName() |!textInputEmail() |!textInputMobile() | !textInputPassword()) {
            Toast.makeText(getContext(), "Fill required fields", Toast.LENGTH_SHORT).show();
        }else {

            bar.show();
            mAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    bar.dismiss();
                    uploadUserDeatilsToFirestore();
                    mSession.set("name",mName);
                    mSession.set("email",mEmail);
                    mSession.set("mobile",mMobile);
                    mSession.set("passowrd",mPassword);
                    Toast.makeText(getContext(), "Congratulations \n You've Signed Up Successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new LoginFragment()).commit();
                } else {
                    bar.dismiss();
                    Toast.makeText(getContext(), "Error ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public boolean textInputName() {
        String email = mBinding.username.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            mBinding.username.setError("Email field can't be empty");
            return false;
        } else {
            mBinding.username.setError(null);
            return true;
        }
    }

    public boolean textInputEmail() {
        String email = mBinding.email.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            mBinding.email.setError("Password field can't be empty");
            return false;
        } else {
            mBinding.email.setError(null);
            return true;
        }
    }

    public boolean textInputMobile() {
        String email = mBinding.mobile.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            mBinding.mobile.setError("Email field can't be empty");
            return false;
        } else {
            mBinding.mobile.setError(null);
            return true;
        }
    }

    public boolean textInputPassword() {
        String email = mBinding.password.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            mBinding.password.setError("Password field can't be empty");
            return false;
        } else {
            mBinding.password.setError(null);
            return true;
        }
    }

    private void uploadUserDeatilsToFirestore() {
        userId= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mReference=mFirestore.collection("Users").document(userId);
        Map<String, Object> user = new HashMap<>();
        user.put("name", mName);
        user.put("mobile",mMobile);
        user.put("email", mEmail);
        user.put("password",mPassword);
        user.put("dob","");
        user.put("address","");
        mReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: "+userId);
            }
        });
    }
}