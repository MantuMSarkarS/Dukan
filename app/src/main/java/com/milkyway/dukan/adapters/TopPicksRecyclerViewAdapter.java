package com.milkyway.dukan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.milkyway.dukan.R;
import com.milkyway.dukan.model.SliderImage;
import com.milkyway.dukan.model.TopPicksResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopPicksRecyclerViewAdapter extends RecyclerView.Adapter<TopPicksRecyclerViewAdapter.MyViewHolder> {

    List<TopPicksResponse> mCategories;
    OnItemClickListener mListener;
    Context mContext;
    String mFrom;

    public TopPicksRecyclerViewAdapter(List<TopPicksResponse> categories, Context context, String from, OnItemClickListener listener) {
        mCategories = categories;
        mContext = context;
        mListener = listener;
        mFrom = from;
    }

    @NonNull
    @Override
    public TopPicksRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.deal_of_the_day_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopPicksRecyclerViewAdapter.MyViewHolder holder, int position) {
        TopPicksResponse response=mCategories.get(position);
        if(mFrom.equals("toppicks")) {
            holder.mTopPicksRow.setVisibility(View.VISIBLE);
            holder.mTopPicksItemPrice.setText(response.getNewPrice());
            holder.mTopPicksItemOldPrice.setText(response.getOldPrice());
            holder.mTopPicksItemDetails.setText(response.getModelName());
            holder.mDiscount.setText(response.getDiscount());
            if (response.getOldPrice().equals("") || response.getOldPrice().isEmpty()) {
                holder.mTopPicksRupeeSymbol.setVisibility(View.GONE);
            }
            Picasso.get().load(response.getImage()).placeholder(R.drawable.icon).error(R.drawable.icon).into(holder.mTopPicksImage);
            holder.mTopPicksRow.setOnClickListener(v->{
                mListener.onItemClick(response,position);
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TopPicksResponse response, int position);
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mTopPicksImage;
        TextView mTopPicksItemPrice,mTopPicksItemDetails,mDiscount,mTopPicksItemOldPrice,mTopPicksRupeeSymbol;
        RelativeLayout mTopPicksRow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTopPicksRow=itemView.findViewById(R.id.top_pick_row);
            mTopPicksItemDetails=itemView.findViewById(R.id.top_pick_item_name);
            mTopPicksItemOldPrice=itemView.findViewById(R.id.top_pick_item_old_price);
            mTopPicksRupeeSymbol=itemView.findViewById(R.id.top_pick_oldprice_rupee);
            mTopPicksItemPrice=itemView.findViewById(R.id.top_pick_item_price);
            mDiscount=itemView.findViewById(R.id.discount);
            mTopPicksImage=itemView.findViewById(R.id.top_pick_Image);
        }
    }
}
