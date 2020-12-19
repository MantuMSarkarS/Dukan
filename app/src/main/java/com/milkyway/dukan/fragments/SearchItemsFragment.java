package com.milkyway.dukan.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.milkyway.dukan.R;

public class SearchItemsFragment extends Fragment {

    public SearchItemsFragment() { }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search_items, container, false);
    }
}