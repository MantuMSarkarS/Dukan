package com.milkyway.dukan.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milkyway.dukan.R;
import com.milkyway.dukan.model.DealsOfTheDayResponse;
import com.milkyway.dukan.model.SliderImage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DealsRecyclerViewAdapter extends RecyclerView.Adapter<DealsRecyclerViewAdapter.MyViewHolder> {


    List<DealsOfTheDayResponse> mDeals;
    OnItemClickListener mListener;
    Context mContext;

    public DealsRecyclerViewAdapter(List<DealsOfTheDayResponse> mDeals, Context mContext, OnItemClickListener mListener) {
        this.mDeals = mDeals;
        this.mListener = mListener;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public DealsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.deal_of_the_day_row, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DealsRecyclerViewAdapter.MyViewHolder holder, int position) {
        DealsOfTheDayResponse response=mDeals.get(position);
        holder.mItemPrice.setText(response.getNewPrice());
        holder.mItemOldPrice.setText(response.getOldPrice());
        holder.mItemDetails.setText(response.getModelName());
        holder.mItemSpec.setText(response.getDescription());
        if(response.getOldPrice().equals("") || response.getOldPrice().isEmpty()){
            holder.mrupeeSymbol.setVisibility(View.GONE);
        }
        Picasso.with(mContext).load(response.getImage()).placeholder(R.drawable.icon).error(R.drawable.icon).into(holder.mDealsImage);
    }

    @Override
    public int getItemCount() {
       /* if(mDeals.size()>4){
            return 4;
        }else {*/
            return mDeals.size();
       // }

    }
    public interface OnItemClickListener {
        void onItemClick(DealsOfTheDayResponse imageList, int position);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView mDealsImage;
        TextView mItemPrice,mItemDetails,mItemSpec,mItemOldPrice,mrupeeSymbol;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mDealsImage=itemView.findViewById(R.id.dealsImage);
            mItemPrice=itemView.findViewById(R.id.item_price);
            mItemOldPrice=itemView.findViewById(R.id.item_old_price);
            mItemDetails=itemView.findViewById(R.id.item_name);
            mItemSpec=itemView.findViewById(R.id.item_spec);
            mrupeeSymbol=itemView.findViewById(R.id.oldprice_rupee);
        }
    }
}
