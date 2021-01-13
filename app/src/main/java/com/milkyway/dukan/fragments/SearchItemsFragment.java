package com.milkyway.dukan.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.milkyway.dukan.R;
import com.milkyway.dukan.databinding.FragmentSearchItemsBinding;

public class SearchItemsFragment extends Fragment {

    public SearchItemsFragment() { }

    FragmentSearchItemsBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_search_items, container, false);
        View view=mBinding.getRoot();


        return view;
    }

}