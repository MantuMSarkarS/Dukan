package com.milkyway.dukan.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.milkyway.dukan.R;
import com.milkyway.dukan.activities.MainActivity;
import com.milkyway.dukan.databinding.FragmentNoticationBinding;

public class NotificationFragment extends Fragment {


    public NotificationFragment() { }
    FragmentNoticationBinding mBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_notication,container,false);
        View view=mBinding.getRoot();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setActionBarTitle("Notifications");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        mBinding.goBacktoHome.setOnClickListener(v->{
            navController.navigate(R.id.action_noticationFragment_to_homeFragment);
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.navigate(R.id.action_noticationFragment_to_homeFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }
}