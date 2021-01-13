package com.milkyway.dukan.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.milkyway.dukan.R;
import com.milkyway.dukan.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_search);
        mBinding.setActivity(this);
        mBinding.searchItems.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    mBinding.clearText.setVisibility(View.VISIBLE);
                }else {
                    mBinding.clearText.setVisibility(View.GONE);
                }

            }
        });
        mBinding.clearText.setOnClickListener(v->{
            mBinding.searchItems.setText(null);
        });
        mBinding.searchIcon.setOnClickListener(v->{
            Intent intent=new Intent(SearchActivity.this,MainActivity.class);
            startActivity(intent);
            finishAffinity();
            overridePendingTransition(0, 0);
        });
    }
}