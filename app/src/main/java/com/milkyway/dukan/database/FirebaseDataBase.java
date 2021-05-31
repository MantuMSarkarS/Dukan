package com.milkyway.dukan.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.milkyway.dukan.model.UserModel;

public class FirebaseDataBase {
    private static final String TAG = "FirebaseDataBase";
    private FirebaseFirestore mFirestore;
    private DocumentReference mReference;
    private FirebaseAuth mAuth;
    private FirebaseDataBase mFirebaseDataBase;
    private OnFirebaseListener mOnFirebaseListener;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String mUserId;
    String mResponse;
    public FirebaseDataBase(OnFirebaseListener mOnFirebaseListener) {
        this.mOnFirebaseListener=mOnFirebaseListener;
        mAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                mAuth=firebaseAuth;
                if(mFirebaseUser != null) {
                    mUserId = mFirebaseUser.getUid();
                }
                mReference = mFirestore.collection("Users").document(mUserId);
            }
        };
        mFirestore = FirebaseFirestore.getInstance();

    }

    public String createUsers(UserModel userModel) {
        mAuth.createUserWithEmailAndPassword(userModel.getmEmail(), userModel.getmPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "User Created: ");
                mResponse=task.getResult().toString();
            } else {
                mResponse= task.getException().getMessage();
                Log.d(TAG, "User not Created: ");
            }
        });

        return mResponse;
    }
    public interface OnFirebaseListener{
        void getResponse(AuthResult result);
        void error(Exception e);
    }
}
