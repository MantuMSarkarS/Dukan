package com.milkyway.dukan.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.milkyway.dukan.model.DealsOfTheDayResponse;
import com.milkyway.dukan.model.TopPicksResponse;
import com.milkyway.dukan.model.ViewPagerSliderImage;
import com.milkyway.dukan.repositories.ViewPagerFirebaseRepository;

import java.util.List;

public class TopPicksModel extends ViewModel implements ViewPagerFirebaseRepository.OnFirebaseTaskCompleteForTopPicks {

    private ViewPagerFirebaseRepository dealsrepository = new ViewPagerFirebaseRepository(this);
    private MutableLiveData<List<TopPicksResponse>> dealsliveData = new MutableLiveData<>();


    public TopPicksModel() {
        dealsrepository.getTopPicksItemList();
    }

    public LiveData<List<TopPicksResponse>> getTopPicksModelData() {
        return dealsliveData;
    }

    @Override
    public void getTopPicksList(List<TopPicksResponse> dealsList) {
        dealsliveData.setValue(dealsList);
    }

    @Override
    public void error(Exception e) {

    }
}
