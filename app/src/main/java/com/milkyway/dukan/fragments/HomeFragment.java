package com.milkyway.dukan.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.milkyway.dukan.R;
import com.milkyway.dukan.activities.MainActivity;
import com.milkyway.dukan.activities.SearchActivity;
import com.milkyway.dukan.activities.ViewProductActivity;
import com.milkyway.dukan.adapters.CategoryRecyclerViewAdapter;
import com.milkyway.dukan.adapters.DealsRecyclerViewAdapter;
import com.milkyway.dukan.adapters.TopPicksRecyclerViewAdapter;
import com.milkyway.dukan.adapters.ViewPagerAdapter;
import com.milkyway.dukan.databinding.FragmentHomeBinding;
import com.milkyway.dukan.model.DealsOfTheDayResponse;
import com.milkyway.dukan.model.SliderImage;
import com.milkyway.dukan.model.ViewPagerSliderImage;
import com.milkyway.dukan.util.Session;
import com.milkyway.dukan.viewModel.CategoriesViewModel;
import com.milkyway.dukan.viewModel.DealsViewModel;
import com.milkyway.dukan.viewModel.MostViewedViewModel;
import com.milkyway.dukan.viewModel.TopPicksModel;
import com.milkyway.dukan.viewModel.ViewpagerViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    FragmentHomeBinding mBinding;
    ArrayList<SliderImage> categories = new ArrayList<SliderImage>();
    public static final String TAG = "Home Fragment";
    private String mCategoryId;
    public int currentPage = 0;
    CategoriesViewModel viewModel;
    ViewpagerViewModel mViewpagerViewModel;
    DealsViewModel mDealsViewModel;
    MostViewedViewModel mMostViewedViewModel;
    TopPicksModel mTopPicksModel;
    Session session;
    ViewPagerAdapter mViewPagerAdapter;
    List<ViewPagerSliderImage> mImages;
    public View[] mBannerDotViews;
    public LinearLayout mBannerDotsLayout;
    public int custom_position = 0;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = mBinding.getRoot();
        viewModel = new ViewModelProvider(requireActivity()).get(CategoriesViewModel.class);
        mViewpagerViewModel = new ViewModelProvider(requireActivity()).get(ViewpagerViewModel.class);
        mDealsViewModel = new ViewModelProvider(requireActivity()).get(DealsViewModel.class);
        mMostViewedViewModel = new ViewModelProvider(requireActivity()).get(MostViewedViewModel.class);
        mTopPicksModel = new ViewModelProvider(requireActivity()).get(TopPicksModel.class);
        return view;
    }


   /* public void prepareDots(int custom_position) {
        if (mBinding.bannerDotsLayout.getChildCount() > 0) {
            mBinding.bannerDotsLayout.removeAllViews();
        }
        ImageView imageView[] = new ImageView[5];
        for (int i = 0; i < 5; i++) {
            imageView[i] = new ImageView(getContext());
            if (i == custom_position) {
                imageView[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active));
            } else {
                imageView[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.inactive));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 0, 4);
            mBinding.bannerDotsLayout.addView(imageView[i], params);
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setActionBarTitle("Duकाন");
       /* prepareDots(custom_position++);
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position > 4) {
                    custom_position = 0;
                }
                prepareDots(custom_position++);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
       /* mBinding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (scrollY > oldScrollY) {
                System.out.println("End of NestedScrollView");
            } else {
                System.out.println("End of NestedScrollView 1");
            }
        });*/
        mBinding.allDeals.setOnClickListener(view->{
            ViewProductListFragment fragment = new ViewProductListFragment();
            Bundle bundle = new Bundle();

            fragment.setArguments(bundle);
            getChildFragmentManager().beginTransaction().replace(R.id.mainContainer,new ViewProductListFragment()).commit();
        });
        mBinding.searchItems.setOnClickListener(v -> {
           /* requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, new SearchItemsFragment())
                    .commit();*/
            Intent intent=new Intent(requireActivity(), SearchActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(0, 0);

        });
        mBinding.progressBar.setVisibility(View.VISIBLE);
        viewModel.getCategoryModelData().observe(getViewLifecycleOwner(), images -> {
            if(images!=null){
                mBinding.recyclerView.setVisibility(View.VISIBLE);
                mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                mBinding.recyclerView.setAdapter(new CategoryRecyclerViewAdapter(images, getContext(), "", (imageList, position) -> {
                    mCategoryId = images.get(position).getCategoryId();
                    CategoryFragment fragment = new CategoryFragment();
                    Bundle bundle = new Bundle();
                    if (mCategoryId.equals("AC")) {
                        bundle.putString("categoryId", "AC");
                    } else if (mCategoryId.equals("AP")) {
                        bundle.putString("categoryId", "AP");
                    } else if (mCategoryId.equals("EC")) {
                        bundle.putString("categoryId", "EC");
                    } else if (mCategoryId.equals("FD")) {
                        bundle.putString("categoryId", "FD");
                    } else if (mCategoryId.equals("FS")) {
                        bundle.putString("categoryId", "FS");
                    }
                    fragment.setArguments(bundle);
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, fragment)
                            .commit();
                }));
            }
        });
        mViewpagerViewModel.getViewPagerModelData().observe(getViewLifecycleOwner(), viewPagerSliderImages -> {
            if (viewPagerSliderImages!=null){
                mBinding.viewPager.setVisibility(View.VISIBLE);
                mImages = viewPagerSliderImages;
                final Handler handler = new Handler();
                Timer swipeTimer = new Timer();
                final Runnable Update = () -> {
                    if (currentPage == Integer.MAX_VALUE) {
                        currentPage = 0;
                    }
                    mBinding.viewPager.setCurrentItem(currentPage++, true);
                };
                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 500, 3000);
                mViewPagerAdapter = new ViewPagerAdapter(getContext(), mImages);
                mBinding.viewPager.setAdapter(mViewPagerAdapter);
            }

        });
        mDealsViewModel.getDealsModelData().observe(getViewLifecycleOwner(), dealsList -> {
            if(dealsList!=null){
                mBinding.dealsTitle.setVisibility(View.VISIBLE);
                mBinding.dealsLay.setVisibility(View.VISIBLE);
                mBinding.dealsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                mBinding.dealsRecyclerView.setAdapter(new DealsRecyclerViewAdapter(dealsList, getContext(),"deals", (imageList, position) -> {
                    Intent intent=new Intent(requireActivity(), ViewProductActivity.class);
                    intent.putExtra("product_id",imageList.getModelid());
                    startActivity(intent);
                    requireActivity().overridePendingTransition(0, 0);
                }));
                mMostViewedViewModel.getMostViewedModelData().observe(getViewLifecycleOwner(), mostViewList -> {
                    if (mostViewList!=null){
                        mBinding.mostViewTitle.setVisibility(View.VISIBLE);
                        mBinding.mostViewLay.setVisibility(View.VISIBLE);
                        mBinding.mostViewedRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        mBinding.mostViewedRecyclerView.setAdapter(new DealsRecyclerViewAdapter(mostViewList, getContext(),"most_view" , (imageList, position) -> {
                            Intent intent=new Intent(requireActivity(), ViewProductActivity.class);
                            intent.putExtra("product_id",mostViewList.get(position).getModelid()    );
                            startActivity(intent);
                            requireActivity().overridePendingTransition(0, 0);
                        }));
                        mTopPicksModel.getTopPicksModelData().observe(getViewLifecycleOwner(), topPicksList -> {
                            if (topPicksList!=null){
                                mBinding.progressBar.setVisibility(View.GONE);
                                mBinding.topPickTitle.setVisibility(View.VISIBLE);
                                mBinding.topPickLay.setVisibility(View.VISIBLE);
                                mBinding.topPicksRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                                mBinding.topPicksRecyclerView.setAdapter(new TopPicksRecyclerViewAdapter(topPicksList, getContext(),"toppicks" , (imageList, position) -> {
                                    Intent intent=new Intent(requireActivity(), ViewProductActivity.class);
                                    intent.putExtra("product_id",topPicksList.get(position).getModelid());
                                    startActivity(intent);
                                    requireActivity().overridePendingTransition(0, 0);
                                }));
                            }
                        });
                    }
                });
            }

        });


        /*FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast

                        Log.d(TAG, token);
                       // Toast.makeText(getContext(), token, Toast.LENGTH_SHORT).show();
                    }
                });*/
    }
}