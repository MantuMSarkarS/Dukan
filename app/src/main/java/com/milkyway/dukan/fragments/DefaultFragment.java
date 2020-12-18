package com.milkyway.dukan.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.milkyway.dukan.R;
import com.milkyway.dukan.databinding.FragmentDefaultBinding;

public class DefaultFragment extends Fragment {

    private FragmentDefaultBinding mBinding;

    public DefaultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_default, container, false);
        View view = mBinding.getRoot();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finishAffinity();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.registerButton.setOnClickListener(v->{
            openRegisterFragment();
        });
        mBinding.loginButton.setOnClickListener(v->{
            openLoginFragment();
        });
    }
    private void openRegisterFragment() {
        mBinding.buttonLayout.setVisibility(View.GONE);
        mBinding.bgImage.setVisibility(View.GONE);
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.frameContainer,new RegisterFragment()).commit();
    }

    private void openLoginFragment() {
        mBinding.buttonLayout.setVisibility(View.GONE);
        mBinding.bgImage.setVisibility(View.GONE);
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.frameContainer,new LoginFragment()).commit();
    }
}