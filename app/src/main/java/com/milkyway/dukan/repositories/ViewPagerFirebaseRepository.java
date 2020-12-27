package com.milkyway.dukan.repositories;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.milkyway.dukan.model.DealsOfTheDayResponse;
import com.milkyway.dukan.model.SliderImage;
import com.milkyway.dukan.model.TopPicksResponse;
import com.milkyway.dukan.model.ViewPagerSliderImage;

import java.util.List;

public class ViewPagerFirebaseRepository {

    private OnFirebaseTaskComplete onFirebaseTaskComplete;
    private OnFirebaseTaskCompleteForDeals onFirebaseTaskCompleteForDeals;
    private OnFirebaseTaskCompleteForTopPicks onFirebaseTaskCompleteForTopPicks;
    private FirebaseFirestore mFirestore=FirebaseFirestore.getInstance();
    private CollectionReference mReference=mFirestore.collection("ViewPagerItem");
    private CollectionReference mDealsReference=mFirestore.collection("Dealsofthedayitems");
    private CollectionReference mMostViewedReference=mFirestore.collection("Dealsofthedayitems");
    private CollectionReference mTopPicksReference=mFirestore.collection("TopPicks");


    public ViewPagerFirebaseRepository(OnFirebaseTaskComplete onFirebaseTaskComplete) {
        this.onFirebaseTaskComplete = onFirebaseTaskComplete;
    }
    public ViewPagerFirebaseRepository(OnFirebaseTaskCompleteForDeals onFirebaseTaskCompleteForDeals) {
        this.onFirebaseTaskCompleteForDeals = onFirebaseTaskCompleteForDeals;
    }
    public ViewPagerFirebaseRepository(OnFirebaseTaskCompleteForTopPicks onFirebaseTaskCompleteForTopPicks) {
        this.onFirebaseTaskCompleteForTopPicks = onFirebaseTaskCompleteForTopPicks;
    }
    public interface OnFirebaseTaskComplete {
        void dealsList(List<DealsOfTheDayResponse> dealsList);
        void categoryList(List<ViewPagerSliderImage> imageList);
        void error(Exception e);
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


    public interface OnFirebaseTaskCompleteForDeals {
        void getMostViewedList(List<DealsOfTheDayResponse> dealsList);
        void error(Exception e);
    }
    public void getDealsList(){
        mDealsReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                onFirebaseTaskComplete.dealsList(task.getResult().toObjects(DealsOfTheDayResponse.class));
            }else {
                onFirebaseTaskComplete.error(task.getException());
            }
        });
    }
    public void getMostViewItemList(){
        mMostViewedReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                onFirebaseTaskCompleteForDeals.getMostViewedList(task.getResult().toObjects(DealsOfTheDayResponse.class));
            }else {
                onFirebaseTaskCompleteForDeals.error(task.getException());
            }
        });
    }


    public interface OnFirebaseTaskCompleteForTopPicks {
        void getTopPicksList(List<TopPicksResponse> dealsList);
        void error(Exception e);
    }
    public void getTopPicksItemList(){
        mTopPicksReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                onFirebaseTaskCompleteForTopPicks.getTopPicksList(task.getResult().toObjects(TopPicksResponse.class));
            }else {
                onFirebaseTaskCompleteForTopPicks.error(task.getException());
            }
        });
    }
}
