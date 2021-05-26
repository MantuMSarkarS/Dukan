package com.milkyway.dukan.repositories;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.milkyway.dukan.model.CommonModel;
import com.milkyway.dukan.model.User;
import com.milkyway.dukan.model.ViewPagerSliderImage;
import com.milkyway.dukan.viewModel.UserViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserRepository {
    private static final String TAG = "UserRepository";
    private OnFirebaseTaskComplete onFirebaseTaskComplete;
    FirebaseAuth  mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String userId = mAuth.getCurrentUser().getUid();
    DocumentReference mReference = fStore.collection("Users").document(userId);
    public UserRepository(OnFirebaseTaskComplete onFirebaseTaskComplete) {
        this.onFirebaseTaskComplete = onFirebaseTaskComplete;
    }
    public void createUserToServer(User[] user){
        Map<String, Object> users = new HashMap<>();
        users.put("address", user);
        mReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: " + userId);
                onFirebaseTaskComplete.createUser("success");
            }
        });
        /*mReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                onFirebaseTaskComplete.createUser(task.getResult().toObjects(User.class));
            }else {
                onFirebaseTaskComplete.error(task.getException());
            }
        });*/
    }
    public interface OnFirebaseTaskComplete {
        void createUser(String success);
    }
}
