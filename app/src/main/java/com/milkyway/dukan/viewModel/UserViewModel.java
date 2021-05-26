package com.milkyway.dukan.viewModel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.milkyway.dukan.model.User;
import com.milkyway.dukan.repositories.UserRepository;

import java.util.List;

public class UserViewModel  extends AndroidViewModel implements UserRepository.OnFirebaseTaskComplete {
    private static final String TAG = "UserViewModel";
     UserRepository userRepository = new UserRepository(this);
     MutableLiveData<List<User>> createUserData = new MutableLiveData<>();

    public UserViewModel(@NonNull  Application application) {
        super(application);
    }

    /*public UserViewModel(Application application){
        super(application);
        userRepository = new UserRepository(application);
    }*/
    @Override
    public void createUser(String success) {
        Log.d(TAG, "createUser: "+success);
    }
    public void insertUserTable(User users) {
        new InsertUserIntoAsynctask(userRepository).execute(users);
    }
    public static class InsertUserIntoAsynctask extends AsyncTask<User, Void, Void> {

        UserRepository userRepository;
        public InsertUserIntoAsynctask(UserRepository userRepository) {
            this.userRepository=userRepository;
        }

        @Override
        protected Void doInBackground(User... users) {
            userRepository.createUserToServer(users);
            return null;
        }
    }
}
