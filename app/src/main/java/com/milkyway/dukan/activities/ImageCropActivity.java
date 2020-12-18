package com.milkyway.dukan.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.milkyway.dukan.R;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

public class ImageCropActivity extends AppCompatActivity implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener {

    //create instances
    private CropImageView cropImageView;
    private final String TAG = "lovelock" +  "ImageCropActiv";

    private Button doneBtn;

    private ImageButton rotateBtn;

    private Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_crop);

        //bind view
        cropImageView = findViewById(R.id.crop_image_view);

        //init instances
        cropImageView.setFixedAspectRatio(false);
        cropImageView.setOnSetImageUriCompleteListener(this);
        cropImageView.setOnCropImageCompleteListener(this);

        //bind views
        doneBtn = findViewById(R.id.done_btn);

        rotateBtn = findViewById(R.id.rotate_btn);

        cancelBtn = findViewById(R.id.cancel_btn);

        //get image url from bundle passed to this from calling activity
        Uri imageUri = getIntent().getParcelableExtra("image_uri");

        //if image uri is null nothing can be done so close this activity
        if (imageUri == null) {
            Log.d(TAG, "Can't load image");
            Toast.makeText(this, "Can't load image. Try again", Toast.LENGTH_SHORT).show();
            finish();
        }

        //set image uri to crop image view
        cropImageView.setImageUriAsync(imageUri);

        //when done btn is clicked get cropped image
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.getCroppedImageAsync();
            }
        });

        //rotate the image
        rotateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(90);
            }
        });

        //cancel button finishes the activity
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        if (result.getError() == null) {
            Intent intent = new Intent();
            if (result.getUri() != null) {
                //just pass the uri to intent if its not null
                intent.putExtra("image_uri", result.getUri());
            } else {
                //otherwise we have bitmap
//                bitmap = result.getBitmap();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                result.getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), result.getBitmap(), "Title", null);
                intent.putExtra("image_uri", path);
                Log.e("TAG", "onCropImageComplete @@@@@@@@@@@@@@@@@@@@: "+path);
            }
            setResult(RESULT_OK, intent);
        } else {
            Log.e(TAG, "Failed to crop image" + result.getError().getMessage());
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            // Toast.makeText(this, "Image load successful", Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Failed to load image by URI" + error.getMessage());
            Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}