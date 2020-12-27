package com.milkyway.dukan.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.milkyway.dukan.R;
import com.milkyway.dukan.activities.MainActivity;

public class CartFragment extends Fragment {


    public CartFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setActionBarTitle("My Cart");
    }
}