package com.milkyway.dukan.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.milkyway.dukan.model.SliderImage;

import java.util.List;

public class FirebaseRepository {

    private OnFirebaseTaskComplete onFirebaseTaskComplete;
    private FirebaseFirestore mFirestore=FirebaseFirestore.getInstance();
    private CollectionReference mReference=mFirestore.collection("Categories");

    public FirebaseRepository(OnFirebaseTaskComplete onFirebaseTaskComplete) {
        this.onFirebaseTaskComplete = onFirebaseTaskComplete;
    }

    public void getCategoryList(){
        mReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        onFirebaseTaskComplete.categoryList(task.getResult().toObjects(SliderImage.class));
                    }else {
                        onFirebaseTaskComplete.error(task.getException());
                    }
            }
        });
    }
    public interface OnFirebaseTaskComplete {
        void categoryList(List<SliderImage> imageList);
        void error(Exception e);
    }

}
