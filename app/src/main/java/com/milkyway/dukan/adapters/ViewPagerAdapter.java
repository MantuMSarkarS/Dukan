package com.milkyway.dukan.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.milkyway.dukan.R;
import com.milkyway.dukan.model.SliderImage;
import com.milkyway.dukan.model.ViewPagerSliderImage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    List<ViewPagerSliderImage> mCategories;
    LayoutInflater mLayoutInflater;
    Context mContext;
    int custom_position=0;

    public ViewPagerAdapter( Context mContext,List<ViewPagerSliderImage> mCategories) {
        this.mCategories = mCategories;
        this.mContext = mContext;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        if (custom_position>4){
            custom_position=0;
        }
        ViewPagerSliderImage image=mCategories.get(custom_position);
        custom_position++;
        View items=mLayoutInflater.inflate(R.layout.item,container,false);
        ImageView imageView=items.findViewById(R.id.imageViewMain);

        Picasso.with(mContext).load(image.getImage()).placeholder(R.drawable.icon).error(R.drawable.icon).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        container.addView(items);
        return items;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
