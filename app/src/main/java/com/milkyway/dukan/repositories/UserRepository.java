package com.milkyway.dukan.repositories;

import android.os.AsyncTask;

import com.google.firebase.auth.AuthResult;
import com.milkyway.dukan.database.FirebaseDataBase;
import com.milkyway.dukan.model.UserModel;

public class UserRepository implements FirebaseDataBase.OnFirebaseListener {
    private FirebaseDataBase mFirebaseDataBase;
    private OnRepositoryListener mOnRepositoryListener;

    public UserRepository(OnRepositoryListener mOnRepositoryListener) {
        this.mOnRepositoryListener = mOnRepositoryListener;
        mFirebaseDataBase = new FirebaseDataBase(this);
    }

    public String createUsers(UserModel userModel) {
       return String.valueOf(new InsertUserUsingAsynctask(mFirebaseDataBase).execute(userModel));
    }

    @Override
    public void getResponse(AuthResult result) {
        mOnRepositoryListener.Response(result);
    }

    @Override
    public void error(Exception e) {
        mOnRepositoryListener.Error(e);
    }


    public static class InsertUserUsingAsynctask extends AsyncTask<UserModel, String, String> {
        private FirebaseDataBase mFirebaseDataBase;

        private InsertUserUsingAsynctask(FirebaseDataBase mFirebaseDataBase) {
            this.mFirebaseDataBase = mFirebaseDataBase;
        }

        @Override
        protected String doInBackground(UserModel... userModels) {
            return mFirebaseDataBase.createUsers(userModels[0]);
        }
    }

    public interface OnRepositoryListener {
        void Response(AuthResult result);

        void Error(Exception e);
    }

}
