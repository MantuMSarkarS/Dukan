package com.milkyway.dukan.activities;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.milkyway.dukan.R;
import com.milkyway.dukan.databinding.ActivityLoginBinding;
import com.milkyway.dukan.fragments.DefaultFragment;
import com.milkyway.dukan.fragments.LoginFragment;
import com.milkyway.dukan.fragments.RegisterFragment;

public class LoginActivity extends AppCompatActivity {

    private boolean isBackButtonPressed=false;
    private ActivityLoginBinding mBinding;
    private FirebaseAuth mAuth;
    NavController mNavController;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_login);
        mBinding.setActivity(this);
        mAuth = FirebaseAuth.getInstance();
        mNavController = Navigation.findNavController(this, R.id.login_fragment);
        /*getSupportFragmentManager().beginTransaction().attach(new LoginFragment()).commit();
        getSupportFragmentManager().beginTransaction().attach(new RegisterFragment()).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frameContainer,new LoginFragment()).commit();*/
    }


    @Override

    public void onBackPressed() {
        super.onBackPressed();
    }
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.frameContainer);
        fragment.onActivityResult(requestCode, resultCode, data);
    }*/

}