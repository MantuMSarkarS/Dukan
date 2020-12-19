package com.milkyway.dukan.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.milkyway.dukan.R;
import com.milkyway.dukan.activities.MainActivity;
import com.milkyway.dukan.adapters.CategoryRecyclerViewAdapter;
import com.milkyway.dukan.databinding.FragmentCategoryBinding;
import com.milkyway.dukan.model.SliderImage;
import com.milkyway.dukan.util.Session;
import com.milkyway.dukan.viewModel.CategoriesViewModel;
import com.milkyway.dukan.viewModel.CategoryDetailsViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CategoryFragment extends Fragment {



    public CategoryFragment() { }
    FragmentCategoryBinding mBinding;
    String mCategoryId;
    Session session;
    CategoryDetailsViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false);
        View view = mBinding.getRoot();
        session = new Session(requireContext());
        viewModel = new ViewModelProvider(requireActivity()).get(CategoryDetailsViewModel.class);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.backButton.setOnClickListener(v->{
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, new HomeFragment())
                    .commit();
        });

        Bundle bundle=this.getArguments();
        if(bundle != null){
            mCategoryId=bundle.getString("categoryId");
            if(mCategoryId.equals("AC")){
               mBinding.categoryTitle.setText("All Categories");
            }else if(mCategoryId.equals("AP")){
                mBinding.categoryTitle.setText("Appliances");
            }else if(mCategoryId.equals("EC")){
                mBinding.categoryTitle.setText("Electronics");
            }else if(mCategoryId.equals("FD")){
                mBinding.categoryTitle.setText("Food");
            }else if(mCategoryId.equals("FS")){
                mBinding.categoryTitle.setText("Fashion");
            }
        }

        viewModel.getCategoryModelData().observe(getViewLifecycleOwner(), images -> {
           mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
            CategoryRecyclerViewAdapter adapter=new CategoryRecyclerViewAdapter(images, getContext(),"category", (imageList, position) -> {
                mCategoryId = images.get(position).getCategoryId();
                CategoryFragment fragment=new CategoryFragment();
                Bundle bundle1 =new Bundle();
                if(mCategoryId.equals("AC")){
                    bundle1.putString("categoryId","AC");
                }else if(mCategoryId.equals("AP")){
                    bundle1.putString("categoryId","AP");
                }else if(mCategoryId.equals("EC")){
                    bundle1.putString("categoryId","EC");
                }else if(mCategoryId.equals("FD")){
                    bundle1.putString("categoryId","FD");
                }else if(mCategoryId.equals("FS")){
                    bundle1.putString("categoryId","FS");
                }
                fragment.setArguments(bundle1);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .commit();
            });
            adapter.notifyDataSetChanged();
            mBinding.recyclerView.setAdapter(adapter);

        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

}