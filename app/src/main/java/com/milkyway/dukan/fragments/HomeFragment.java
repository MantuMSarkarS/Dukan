package com.milkyway.dukan.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.milkyway.dukan.R;
import com.milkyway.dukan.activities.MainActivity;
import com.milkyway.dukan.adapters.CategoryRecyclerViewAdapter;
import com.milkyway.dukan.adapters.ViewPagerAdapter;
import com.milkyway.dukan.databinding.FragmentHomeBinding;
import com.milkyway.dukan.model.SliderImage;
import com.milkyway.dukan.model.ViewPagerSliderImage;
import com.milkyway.dukan.util.Session;
import com.milkyway.dukan.viewModel.CategoriesViewModel;
import com.milkyway.dukan.viewModel.ViewpagerViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    private FirebaseAuth mAuth;
    FragmentHomeBinding mBinding;
    FirebaseFirestore fStore;
    DocumentReference mFeference;
    ArrayList<SliderImage> categories = new ArrayList<SliderImage>();
    public static final String TAG = "Home Fragment";
    private String mCategoryId;
    CategoriesViewModel viewModel;
    ViewpagerViewModel viewpagerViewModel;
    Session session;
    ViewPagerAdapter mViewPagerAdapter;
    List<ViewPagerSliderImage> mImages;
    private View[] mBannerDotViews;
    private LinearLayout mBannerDotsLayout;
    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = mBinding.getRoot();
        viewModel = new ViewModelProvider(requireActivity()).get(CategoriesViewModel.class);
        viewpagerViewModel = new ViewModelProvider(requireActivity()).get(ViewpagerViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mBinding.searchItems.setOnClickListener(v->{
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, new SearchItemsFragment())
                    .commit();
        });
        viewModel.getCategoryModelData().observe(getViewLifecycleOwner(), images -> {
            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            mBinding.recyclerView.setAdapter(new CategoryRecyclerViewAdapter(images, getContext(),"", (imageList, position) -> {
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
            }));
        });
        viewpagerViewModel.getViewPagerModelData().observe(getViewLifecycleOwner(),viewPagerSliderImages -> {
            mImages.addAll(viewPagerSliderImages);
            mBannerDotViews = new View[mImages.size()];
            for (int i = 0; i < mImages.size(); i++) {
                // create a new textview
                final View bannerDotView = new View(getContext());
                /*Creating the dynamic dots for banner*/
                LinearLayout.LayoutParams dotLayoutParm=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dotLayoutParm.height = getResources().getDimensionPixelSize(R.dimen.dimen_8dp);
                dotLayoutParm.width = getResources().getDimensionPixelSize(R.dimen.dimen_8dp);
                dotLayoutParm.setMargins(getResources().getDimensionPixelSize(R.dimen.dimen_8dp),0,0,0);
                bannerDotView.setLayoutParams(dotLayoutParm);
                bannerDotView.setBackground(getResources().getDrawable(R.drawable.shape_deselected_dot));

                // add the textview to the linearlayout
                mBannerDotsLayout.addView(bannerDotView);
                // save a reference to the textview for later
                mBannerDotViews[i] = bannerDotView;
            }
            mViewPagerAdapter = new ViewPagerAdapter(getContext(), viewPagerSliderImages);
            mBinding.viewPager.setAdapter(mViewPagerAdapter);
        });

        AutoSwipeBanner();
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeDotBG(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    public void AutoSwipeBanner(){
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                int currentPage=mBinding.viewPager.getCurrentItem();
                if (currentPage == mImages.size()-1) {
                    currentPage = -1;
                }
                mBinding.viewPager.setCurrentItem(currentPage+1, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 500, 3000);

    }
    private void changeDotBG(int position){

        for(int i = 0; i < mImages.size(); i++){
            if(position==i){
                mBannerDotViews[i].setBackground(getResources().getDrawable(R.drawable.shape_selected_dot));
            }else{
                mBannerDotViews[i].setBackground(getResources().getDrawable(R.drawable.shape_deselected_dot));
            }

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setActionBarTitle("Duकाন");
    }
}