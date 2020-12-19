package com.milkyway.dukan.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.milkyway.dukan.model.SliderImage;
import com.milkyway.dukan.repositories.FirebaseRepository;

import java.util.List;

public class CategoryDetailsViewModel extends ViewModel implements FirebaseRepository.OnFirebaseTaskComplete {
    private FirebaseRepository repository = new FirebaseRepository(this);
    private MutableLiveData<List<SliderImage>> liveData = new MutableLiveData<>();

    public LiveData<List<SliderImage>> getCategoryModelData() {
        return liveData;
    }

    public CategoryDetailsViewModel() {
        repository.getCategoryDetailsList();
    }

    @Override
    public void categoryList(List<SliderImage> imageList) {
        liveData.setValue(imageList);
    }

    @Override
    public void error(Exception e) {

    }
}
