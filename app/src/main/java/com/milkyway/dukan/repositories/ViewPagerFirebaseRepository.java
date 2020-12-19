package com.milkyway.dukan.repositories;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.milkyway.dukan.model.SliderImage;
import com.milkyway.dukan.model.ViewPagerSliderImage;

import java.util.List;

public class ViewPagerFirebaseRepository {

    private OnFirebaseTaskComplete onFirebaseTaskComplete;
    private FirebaseFirestore mFirestore=FirebaseFirestore.getInstance();
    private CollectionReference mReference=mFirestore.collection("ViewPagerItem");

    public ViewPagerFirebaseRepository(OnFirebaseTaskComplete onFirebaseTaskComplete) {
        this.onFirebaseTaskComplete = onFirebaseTaskComplete;
    }

    public void getViewPagerImageList(){
        mReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                onFirebaseTaskComplete.categoryList(task.getResult().toObjects(ViewPagerSliderImage.class));
            }else {
                onFirebaseTaskComplete.error(task.getException());
            }
        });
    }
    public interface OnFirebaseTaskComplete {
        void categoryList(List<ViewPagerSliderImage> imageList);
        void error(Exception e);
    }

}
