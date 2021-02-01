package com.milkyway.dukan.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.database.DatabaseUtils;
import android.os.Bundle;

import com.milkyway.dukan.R;
import com.milkyway.dukan.databinding.ActivityViewProductBinding;

public class ViewProductActivity extends AppCompatActivity {

    ActivityViewProductBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mBinding= DataBindingUtil.setContentView(this,R.layout.activity_view_product);
       mBinding.setActivity(this);

    }
}