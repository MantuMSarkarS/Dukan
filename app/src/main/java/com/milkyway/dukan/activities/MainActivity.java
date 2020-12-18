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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.milkyway.dukan.R;
import com.milkyway.dukan.fragments.LoginFragment;
import com.milkyway.dukan.util.Session;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    public Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public NavController navController;
    public NavigationView navigationView;
    public String mUsername, mEmail, mMobile, mPassword, userId;
    private Session mSession;
    public TextView name, email;
    FirebaseFirestore fStore;
    private StorageReference islandRef;
    FirebaseStorage storage;
    private StorageReference mStorageRef;
    DocumentReference reference;
    View navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        mAuth = FirebaseAuth.getInstance();
        setupNavigation();

    }

    // Setting Up One Time Navigation
    private void setupNavigation() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navView = navigationView.getHeaderView(0);
        name = navView.findViewById(R.id.user_name);
        email = navView.findViewById(R.id.user_email);
        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        fStore = FirebaseFirestore.getInstance();
        reference = fStore.collection("Users").document(userId);
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();
        islandRef = mStorageRef.child("profile_images").child(userId);
        reference.addSnapshotListener(this, (value, error) -> {
            if(value != null){
                Log.d("name", "setupNavigation: "+value.getString("name"));
                Log.d("email", "setupNavigation: "+value.getString("email"));
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
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.fragment), drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        drawerLayout.closeDrawers();
        int id = menuItem.getItemId();
        switch (id) {
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

    @Override
    protected void onResume() {
        super.onResume();
      /*  reference.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            name.setText(value.getString("name"));
            email.setText(value.getString("email"));
            islandRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.with(getBaseContext()).load(uri)
                    .centerCrop().fit().error(R.id.profile).into((ImageView) navView.findViewById(R.id.profiles_image)))
                    .addOnFailureListener(exception -> Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show());

        });*/
    }
}