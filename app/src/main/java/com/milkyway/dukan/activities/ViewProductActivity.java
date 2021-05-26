package com.milkyway.dukan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.milkyway.dukan.R;
import com.milkyway.dukan.databinding.ActivityViewProductBinding;
import com.milkyway.dukan.fragments.BottomSheetDialog;
import com.milkyway.dukan.model.CommonModel;
import com.milkyway.dukan.model.DealsOfTheDayResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewProductActivity extends AppCompatActivity {

    ActivityViewProductBinding mBinding;
    private Snackbar snackbar;
    CommonModel response;
    BottomSheetDialog mBottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_product);
        mBinding.setActivity(this);
        Intent intent = getIntent();
        try {
            response = (CommonModel) intent.getSerializableExtra("product_details");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        mBinding.brandName.setText(response.getModelName());
        mBinding.productName.setText(response.getDescription());
        mBinding.productPrice.setText(response.getNewPrice());
        mBinding.productOldPrice.setText(response.getOldPrice());
        mBinding.productDiscount.setText(response.getDiscount());
        Picasso.get().load(response.getImage()).error(R.drawable.icon).into(mBinding.viewImage);

        mBinding.productSize.setOnClickListener(v -> {
           /* snackbar = Snackbar.make(v, "", Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
            View customSnackBar = getLayoutInflater().inflate(R.layout.product_size_layout, null);
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
            layout.setPadding(0, 0, 0, 0);
            RelativeLayout sizeBackGround = customSnackBar.findViewById(R.id.product_size_border);
            TextView productSize = customSnackBar.findViewById(R.id.product_size_list);
            sizeBackGround.setOnClickListener(v1 -> {

            });
            layout.addView(customSnackBar,0);
            snackbar.show();*/
            Bundle bundle = new Bundle();

            bundle.putSerializable("ramSize", (ArrayList<String>) response.getRamSize());
            mBottomSheetDialog = new BottomSheetDialog(size -> {
                mBinding.size.setText(size);
                mBottomSheetDialog.dismiss();
            });
            mBottomSheetDialog.setArguments(bundle);
            mBottomSheetDialog.show(getSupportFragmentManager(), "clicked");

        });
        mBinding.backButton.setOnClickListener(v2 -> {
            startActivity(new Intent(ViewProductActivity.this, MainActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        mBinding.cartButton.setOnClickListener(v4 -> {
            startActivity(new Intent(ViewProductActivity.this, MainActivity.class)
                    .putExtra("from", "viewProduct"));
            overridePendingTransition(0, 0);
            finish();
        });
        mBinding.searchButton.setOnClickListener(v4 -> {
            startActivity(new Intent(ViewProductActivity.this, SearchActivity.class));
            overridePendingTransition(0, 0);
        });
        mBinding.shareButton.setOnClickListener(v4 -> {
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
            try {
                startActivity(whatsappIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}