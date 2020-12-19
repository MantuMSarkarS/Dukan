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
    private CollectionReference mCategoryReference=mFirestore.collection("Category");

    public FirebaseRepository(OnFirebaseTaskComplete onFirebaseTaskComplete) {
        this.onFirebaseTaskComplete = onFirebaseTaskComplete;
    }

    public void getCategoryList(){
        mReference.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    onFirebaseTaskComplete.categoryList(task.getResult().toObjects(SliderImage.class));
                }else {
                    onFirebaseTaskComplete.error(task.getException());
                }
        });
    }
    public void getCategoryDetailsList(){
        mCategoryReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                onFirebaseTaskComplete.categoryList(task.getResult().toObjects(SliderImage.class));
            }else {
                onFirebaseTaskComplete.error(task.getException());
            }
        });
    }
    public interface OnFirebaseTaskComplete {
        void categoryList(List<SliderImage> imageList);
        void error(Exception e);
    }

}
