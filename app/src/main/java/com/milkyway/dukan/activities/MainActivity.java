package com.milkyway.dukan.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
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
import com.milkyway.dukan.fragments.CartFragment;
import com.milkyway.dukan.fragments.NoticationFragment;
import com.milkyway.dukan.util.Session;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    public NavController navController;
    public String mUsername, mEmail, mMobile, mPassword, userId;
    private Session mSession;
    public TextView name, email;
    FirebaseFirestore fStore;
    private StorageReference islandRef;
    FirebaseStorage storage;
    private StorageReference mStorageRef;
    DocumentReference reference;
    View navView;
    ActivityMainBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        mBinding.setActivity(this);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        mAuth = FirebaseAuth.getInstance();
        setupNavigation();
        mBinding.notification.setOnClickListener(v->{
            Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new NoticationFragment()).commit();
            //navController.navigate(R.id.noticationFragment);
        });
        mBinding.cart.setOnClickListener(v->{
            Toast.makeText(this, "Cart", Toast.LENGTH_SHORT).show();
            //navController.navigate(R.id.cartFragment);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new CartFragment()).commit();
            navController.removeOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                    destination.removeAction(R.id.noticationFragment);
                }
            });
        });
    }

    // Setting Up One Time Navigation
    private void setupNavigation() {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        navView = mBinding.navView.getHeaderView(0);
        name = navView.findViewById(R.id.user_name);
        email = navView.findViewById(R.id.user_email);
        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mBinding.drawerLayout);
        NavigationUI.setupWithNavController(mBinding.navView, navController);
        mBinding.navView.setNavigationItemSelectedListener(this);
        userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        fStore = FirebaseFirestore.getInstance();
        reference = fStore.collection("Users").document(userId);
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();
        islandRef = mStorageRef.child("profile_images").child(userId);
        reference.addSnapshotListener(this, (value, error) -> {
            if (value != null) {
                name.setText(value.getString("name"));
                email.setText(value.getString("email"));
                islandRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.with(getBaseContext()).load(uri)
                        .centerCrop().fit().error(R.id.profile).into((ImageView) navView.findViewById(R.id.profiles_image)))
                        .addOnFailureListener(exception -> Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
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
    public void setActionBarArrow(boolean arrow,boolean hamburger) {
        getSupportActionBar().setDisplayShowHomeEnabled(arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(hamburger);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        mBinding.drawerLayout.closeDrawers();
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.home:
                navController.navigate(R.id.homeFragment);
                break;
            case R.id.todays_deal:
                navController.navigate(R.id.dealsFragment);
                break;
            case R.id.cart:
                navController.navigate(R.id.cartFragment);
                break;
            case R.id.orders:
                navController.navigate(R.id.orderFragment);
                break;
            case R.id.notification:
                navController.navigate(R.id.noticationFragment);
                break;
            case R.id.profile:
                navController.navigate(R.id.profileFragment);
                break;
            case R.id.privacyPolicy:
                navController.navigate(R.id.PPFragment);
                break;
            case R.id.help:
                navController.navigate(R.id.helpCenterFragment);
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
}