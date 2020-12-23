package com.milkyway.dukan.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.milkyway.dukan.model.DealsOfTheDayResponse;
import com.milkyway.dukan.model.SliderImage;
import com.milkyway.dukan.model.ViewPagerSliderImage;
import com.milkyway.dukan.repositories.FirebaseRepository;
import com.milkyway.dukan.repositories.ViewPagerFirebaseRepository;

import java.util.List;

public class DealsViewModel extends ViewModel implements ViewPagerFirebaseRepository.OnFirebaseTaskComplete {

    private ViewPagerFirebaseRepository dealsrepository = new ViewPagerFirebaseRepository(this);
    private MutableLiveData<List<DealsOfTheDayResponse>> dealsliveData = new MutableLiveData<>();


    public DealsViewModel() {
        dealsrepository.getDealsList();
    }

    public LiveData<List<DealsOfTheDayResponse>> getDealsModelData() {
        return dealsliveData;
    }
    @Override
    public void dealsList(List<DealsOfTheDayResponse> dealsList) {
        dealsliveData.setValue(dealsList);
    }

    @Override
    public void categoryList(List<ViewPagerSliderImage> imageList) {

    }

    @Override
    public void error(Exception e) {

    }
}
