package com.milkyway.dukan.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.milkyway.dukan.model.CommonModel;
import com.milkyway.dukan.model.DealsOfTheDayResponse;
import com.milkyway.dukan.model.SliderImage;
import com.milkyway.dukan.model.ViewPagerSliderImage;
import com.milkyway.dukan.repositories.FirebaseRepository;
import com.milkyway.dukan.repositories.ViewPagerFirebaseRepository;

import java.util.List;

public class ViewpagerViewModel extends ViewModel implements ViewPagerFirebaseRepository.OnFirebaseTaskComplete{
    private ViewPagerFirebaseRepository repository = new ViewPagerFirebaseRepository( this);

    private MutableLiveData<List<ViewPagerSliderImage>> liveData = new MutableLiveData<>();


    public LiveData<List<ViewPagerSliderImage>> getViewPagerModelData() {
        return liveData;
    }

    public ViewpagerViewModel() {
        repository.getViewPagerImageList();

    }

    @Override
    public void dealsList(List<CommonModel> dealsList) {

    }

    @Override
    public void categoryList(List<ViewPagerSliderImage> imageList) {
        liveData.setValue(imageList);
    }

    @Override
    public void error(Exception e) { }


}
