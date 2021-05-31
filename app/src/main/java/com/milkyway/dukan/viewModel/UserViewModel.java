package com.milkyway.dukan.viewModel;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.AuthResult;
import com.milkyway.dukan.model.UserModel;
import com.milkyway.dukan.repositories.UserRepository;

public class UserViewModel extends ViewModel implements UserRepository.OnRepositoryListener {
    private UserRepository mUserRepository;
    private MutableLiveData<AuthResult> mLiveData = new MutableLiveData<>();
    String response;
    public UserViewModel() {
        mUserRepository = new UserRepository(this);
    }

    public String createUserWithEmailAndPassword(UserModel userModel) {
      return mUserRepository.createUsers(userModel);
    }

    @Override
    public void Response(AuthResult result) {
        mLiveData.setValue(result);
    }

    @Override
    public void Error(Exception e) {

    }
}
