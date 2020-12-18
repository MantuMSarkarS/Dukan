package com.milkyway.dukan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.milkyway.dukan.R;
import com.milkyway.dukan.model.SliderImage;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.MyViewHolder> {

    List<SliderImage> mCategories;
    OnItemClickListener mListener;
    Context mContext;
    String mFrom;

    public CategoryRecyclerViewAdapter(List<SliderImage> categories, Context context, String from, OnItemClickListener listener) {
        mCategories = categories;
        mContext = context;
        mListener = listener;
        mFrom = from;
    }

    @NonNull
    @Override
    public CategoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_row_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerViewAdapter.MyViewHolder holder, int position) {
        SliderImage imageList = mCategories.get(position);
        if (mFrom != null) {
            if (mFrom.equals("category")) {
                holder.mCategotyItem.setVisibility(View.GONE);
                holder.mCategotyItems.setVisibility(View.VISIBLE);
                holder.mTitles.setText(imageList.getTitle());
                Picasso.with(mContext).load(imageList.getImage()).error(R.drawable.icon).into(holder.mImageViews);
                holder.mCategotyItems.setOnClickListener(v -> {
                    mListener.onItemClick(imageList, position);
                });
            } else {
                //round
                holder.mCategotyItem.setVisibility(View.VISIBLE);
                holder.mCategotyItems.setVisibility(View.GONE);
                holder.mTitle.setText(imageList.getTitle());
                Picasso.with(mContext).load(imageList.getImage()).error(R.drawable.icon).into(holder.mImageView);
                holder.mCategotyItem.setOnClickListener(v -> {
                    mListener.onItemClick(imageList, position);
                });
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(SliderImage imageList, int position);
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle, mTitles;
        CircleImageView mImageView;
        ImageView mImageViews;
        LinearLayout mCategotyItem;
        CardView mCategotyItems;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.image_title);
            mTitles = itemView.findViewById(R.id.image_titles);
            mImageView = itemView.findViewById(R.id.image);
            mImageViews = itemView.findViewById(R.id.images);
            mCategotyItem = itemView.findViewById(R.id.category_id);
            mCategotyItems = itemView.findViewById(R.id.category_ids);
        }
    }
}
