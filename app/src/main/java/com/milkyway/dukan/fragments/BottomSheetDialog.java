package com.milkyway.dukan.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.milkyway.dukan.R;
import com.milkyway.dukan.adapters.ProductSizeAdapter;

import java.util.ArrayList;

public class BottomSheetDialog extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {

    public BottomSheetDialog() {
    }

    BottomSheetListener mBottomSheetListener;

    public BottomSheetDialog(BottomSheetListener applicationContext) {
        this.mBottomSheetListener= (BottomSheetListener) applicationContext;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_size_layout, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.product_size_layout);
        ArrayList<String> ramsize = (ArrayList<String>) this.getArguments().getSerializable("ramSize");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), GridLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ProductSizeAdapter(getContext(), ramsize, size -> {
            mBottomSheetListener.onBottomClicked(size);
        }));
        return view;
    }

    public interface BottomSheetListener {
        void onBottomClicked(String size);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
