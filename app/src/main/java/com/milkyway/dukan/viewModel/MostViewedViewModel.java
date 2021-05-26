package com.milkyway.dukan.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.milkyway.dukan.model.CommonModel;
import com.milkyway.dukan.model.DealsOfTheDayResponse;
import com.milkyway.dukan.model.ViewPagerSliderImage;
import com.milkyway.dukan.repositories.ViewPagerFirebaseRepository;

import java.util.List;

public class MostViewedViewModel extends ViewModel implements ViewPagerFirebaseRepository.OnFirebaseTaskCompleteForDeals {
    private ViewPagerFirebaseRepository dealsrepository = new ViewPagerFirebaseRepository(this);
    private MutableLiveData<List<CommonModel>> mostViewliveData = new MutableLiveData<>();

    public MostViewedViewModel() {
        dealsrepository.getMostViewItemList();
    }

    public LiveData<List<CommonModel>> getMostViewedModelData() {
        return mostViewliveData;
    }

    @Override
    public void getMostViewedList(List<CommonModel> dealsList) {
        mostViewliveData.setValue(dealsList);
    }

    @Override
    public void error(Exception e) {

    }
}
