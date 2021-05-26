package com.milkyway.dukan.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.milkyway.dukan.R;
import com.milkyway.dukan.databinding.ActivityMainBinding;
import com.milkyway.dukan.util.Session;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    public String mUsername, mEmail, mMobile, mPassword, userId, from;
    private Session mSession;
    public TextView name, email;
    FirebaseFirestore fStore;
    private StorageReference islandRef;
    FirebaseStorage storage;
    private StorageReference mStorageRef;
    DocumentReference reference;
    ActivityMainBinding mBinding;
    View navView;
    public NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setActivity(this);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        mNavController = Navigation.findNavController(this, R.id.fragment);
        mAuth = FirebaseAuth.getInstance();
        setupNavigation();
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (bundle.getString("from").equals("viewProduct")) {
                mNavController.navigate(R.id.cartFragment);
            }
        }
    }

    // Setting Up One Time Navigation
    private void setupNavigation() {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        navView = mBinding.navView.getHeaderView(0);
        name = navView.findViewById(R.id.user_name);
        email = navView.findViewById(R.id.user_email);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mBinding.drawerLayout);
        NavigationUI.setupWithNavController(mBinding.navView, mNavController);
        mBinding.navView.setNavigationItemSelectedListener(this);
        userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        fStore = FirebaseFirestore.getInstance();
        reference = fStore.collection("Users").document(userId);
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();
        islandRef = mStorageRef.child("profile_images").child(userId);
        reference.addSnapshotListener(this, (value, error) -> {
            if (value != null) {
                Log.d("name", "setupNavigation: " + value.getString("name"));
                Log.d("email", "setupNavigation: " + value.getString("email"));
                name.setText(value.getString("name"));
                email.setText(value.getString("email"));
                islandRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri)
                        .centerCrop().fit().into((ImageView) navView.findViewById(R.id.profiles_image)))
                        .addOnFailureListener(exception -> Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mBinding.drawerLayout.closeDrawers();
        mBinding.toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_notification) {
                mNavController.navigate(R.id.noticationFragment);
            } else if (id == R.id.menu_cart) {
                mNavController.navigate(R.id.cartFragment);
            }
            return false;
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.fragment), mBinding.drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        mBinding.drawerLayout.closeDrawers();
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.home:
                mNavController.navigate(R.id.homeFragment);
                break;
            case R.id.categories:
                mNavController.navigate(R.id.categoryFragment);
                break;
            case R.id.deals:
                mNavController.navigate(R.id.cartFragment);
                break;
            case R.id.cart:
                mNavController.navigate(R.id.cartFragment);
                break;
            case R.id.orders:
                mNavController.navigate(R.id.orderFragment);
                break;
            case R.id.notification:
                mNavController.navigate(R.id.noticationFragment);
                break;
            case R.id.profile:
                mNavController.navigate(R.id.profileFragment);
                break;
            case R.id.privacyPolicy:
                mNavController.navigate(R.id.PPFragment);
                break;
            case R.id.help:
                mNavController.navigate(R.id.helpCenterFragment);
                break;
            case R.id.singout:
                mAuth.signOut();
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                //new LoginActivity().getSupportFragmentManager().beginTransaction().attach(new LoginFragment()).commit();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}