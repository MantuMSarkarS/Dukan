package com.milkyway.dukan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.milkyway.dukan.R;
import com.milkyway.dukan.fragments.BottomSheetDialog;

import java.util.ArrayList;

public class ProductSizeAdapter extends RecyclerView.Adapter<ProductSizeAdapter.MyViewHolder> {

    Context mContext;
    ProductSizeListner mProductSizeListner;
    String[] ramsize;
    ArrayList<String> list;

    public interface ProductSizeListner {
        void onSizeClicked(String size);
    }

    public ProductSizeAdapter(Context context, ArrayList<String> ramsize, ProductSizeListner mProductSizeListner) {
        this.mContext = context;
        this.list = ramsize;
        this.mProductSizeListner = mProductSizeListner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_size_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.size.setText(list.get(position));
        holder.sizeView.setOnClickListener(v -> {
            String name = list.get(position);
            mProductSizeListner.onSizeClicked(name);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.sizeView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.border_red));
            } else {
                holder.sizeView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.border_red));
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout sizeView;
        TextView size;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sizeView = itemView.findViewById(R.id.product_size_border);
            size = itemView.findViewById(R.id.product_size_name);
        }
    }
}
