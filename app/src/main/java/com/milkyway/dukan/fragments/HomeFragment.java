package com.milkyway.dukan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.milkyway.dukan.R;
import com.milkyway.dukan.activities.MainActivity;
import com.milkyway.dukan.adapters.CategoryRecyclerViewAdapter;
import com.milkyway.dukan.databinding.FragmentHomeBinding;
import com.milkyway.dukan.model.SliderImage;
import com.milkyway.dukan.util.Session;
import com.milkyway.dukan.viewModel.CategoriesViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FirebaseAuth mAuth;
    FragmentHomeBinding mBinding;
    FirebaseFirestore fStore;
    DocumentReference mFeference;
    ArrayList<SliderImage> categories = new ArrayList<SliderImage>();
    public static final String TAG = "Home Fragment";
    private String mCategoryId;
    CategoriesViewModel viewModel;
    Session session;
    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = mBinding.getRoot();
        viewModel = new ViewModelProvider(requireActivity()).get(CategoriesViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        viewModel.getCategoryModelData().observe(getViewLifecycleOwner(), images -> {

            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            mBinding.recyclerView.setAdapter(new CategoryRecyclerViewAdapter(images, getContext(),"", new CategoryRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(SliderImage imageList, int position) {
                    mCategoryId = images.get(position).getCategoryId();
                    CategoryFragment fragment=new CategoryFragment();
                    Bundle bundle=new Bundle();
                   if(mCategoryId.equals("AC")){
                       bundle.putString("categoryId","AC");
                   }else if(mCategoryId.equals("AP")){
                        bundle.putString("categoryId","AP");
                    }else if(mCategoryId.equals("EC")){
                       bundle.putString("categoryId","EC");
                   }else if(mCategoryId.equals("FD")){
                       bundle.putString("categoryId","FD");
                   }else if(mCategoryId.equals("FS")){
                       bundle.putString("categoryId","FS");
                   }
                    fragment.setArguments(bundle);
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, fragment)
                            .commit();
                }
            }));
        });

        /*PagerAdapter adapter = new CustomPagerAdapter(getActivity(),);
        mBinding.viewPager.setAdapter(adapter);
        mBinding.indicator.setupWithViewPager( mBinding.viewPager, true);*/

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setActionBarTitle("Duकाন");
    }
}