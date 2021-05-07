package com.milkyway.dukan.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.milkyway.dukan.R;
import com.milkyway.dukan.databinding.ActivityViewProductBinding;

public class ViewProductActivity extends AppCompatActivity {

    ActivityViewProductBinding mBinding;
    private Snackbar snackbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_product);
        mBinding.setActivity(this);
        mBinding.productSize.setOnClickListener(v -> {
            snackbar = Snackbar.make(this, v, "", Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
            View customSnackBar = getLayoutInflater().inflate(R.layout.product_size_layout, null);
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
            layout.setPadding(0, 0, 0, 0);
            RelativeLayout sizeBackGround = customSnackBar.findViewById(R.id.product_size_border);
            TextView productSize = customSnackBar.findViewById(R.id.product_size_list);
            sizeBackGround.setOnClickListener(v1 -> {

            });
            layout.addView(customSnackBar,0);
            snackbar.show();
        });
        mBinding.backButton.setOnClickListener(v2->{
            startActivity(new Intent(ViewProductActivity.this,MainActivity.class));
            overridePendingTransition(0,0);
            finish();
        });
        mBinding.cartButton.setOnClickListener(v4->{
            /*getSupportFragmentManager().beginTransaction().replace()*/
        });
    }
}