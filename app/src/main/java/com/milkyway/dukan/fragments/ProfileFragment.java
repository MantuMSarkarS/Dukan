package com.milkyway.dukan.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.milkyway.dukan.R;
import com.milkyway.dukan.activities.MainActivity;
import com.milkyway.dukan.databinding.FragmentProfileBinding;
import com.milkyway.dukan.util.ImageUtil;
import com.milkyway.dukan.util.Session;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding mBinding;
    public String mUsername, mEmail, mMobile, mDob, userId, dob, address;
    private Session mSession;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private AlertDialog.Builder alertDialog;
    private static final int PICK_PHOTO_FOR_AVATAR = 1;
    private StorageReference mStorageRef;
    Uri filePath;
    private DocumentReference mReference;
    private ImageUtil imageUtil;
    private StorageReference islandRef;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    private static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static final int CROP_IMAGE_REQUEST = 654;
    private ProgressDialog bar;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        View view = mBinding.getRoot();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();
        islandRef = mStorageRef.child("profile_images").child(userId);
        imageUtil = new ImageUtil(getContext());
        mSession = new Session(getContext());
        mReference = fStore.collection("Users").document(userId);
        mReference.addSnapshotListener(requireActivity(), (value, error) -> {
            mSession.set("femail", value.getString("email"));
            mSession.set("fname", value.getString("name"));
            mSession.set("fmobile", value.getString("mobile"));
            mSession.set("fdob", value.getString("dob"));
            mSession.set("faddress", value.getString("address"));

            mBinding.userName.setText(value.getString("name"));
            mBinding.email.setText(value.getString("email"));
            mBinding.mobile.setText(value.getString("mobile"));
            mBinding.dob.setText(value.getString("dob"));
            mBinding.address.setText(value.getString("address"));
        });

        islandRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).placeholder(R.drawable.profile)
                .error(R.drawable.profile).fit().centerCrop().into(mBinding.profileImage))
                .addOnFailureListener(exception -> Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show());

        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        if (mSession.get("faddress").equals("")) {
            mBinding.add.setVisibility(View.VISIBLE);
        } else {
            mBinding.add.setVisibility(View.GONE);
            mBinding.edit.setVisibility(View.VISIBLE);
        }
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
        mBinding.camera.setOnClickListener(v -> {
            mSession.set("image_upload", "1");
            mBinding.profileImage.setImageResource(0);
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), PICK_PHOTO_FOR_AVATAR);
            mBinding.upload.setVisibility(View.VISIBLE);
        });
        mBinding.upload.setOnClickListener(v -> {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Updating profile...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Bitmap bitmap = ((BitmapDrawable) mBinding.profileImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = islandRef.putBytes(data);
            uploadTask
                    .addOnFailureListener(exception -> {
                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    })
                    .addOnSuccessListener(taskSnapshot -> {
                        mBinding.upload.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        Picasso.get().load(filePath).into(mBinding.profileImage);
                        Toast.makeText(getContext(), "Uploading Done", Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(snapshot -> {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    });
        });
        mBinding.ivDOB.setOnClickListener(v19 -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), (view1, year, monthOfYear, dayOfMonth) -> {
                dob = dayOfMonth + " " + getMonthName(monthOfYear, Locale.US, false) + " " + year;
                mSession.set("age_enter", "yes");
                mBinding.dob.setText(dob);
            }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
        });
        mBinding.profileImage.setOnClickListener(v15 -> {
            final Dialog myDialog = new Dialog(requireActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            myDialog.setContentView(R.layout.popup_picture);
            ImageView ivClose = myDialog.findViewById(R.id.ivClose);
            ImageView image = myDialog.findViewById(R.id.image);
            islandRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(String.valueOf(uri)).error(R.drawable.profile).into(image))
                    .addOnFailureListener(exception -> Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show());
            ivClose.setOnClickListener(view1 -> myDialog.dismiss());
            Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
        });
        mBinding.add.setOnClickListener(v2 -> {
            mBinding.add.setVisibility(View.GONE);
            mBinding.addCancel.setVisibility(View.VISIBLE);
            mBinding.address.setVisibility(View.GONE);
            mBinding.addressEdit.setVisibility(View.VISIBLE);
            mBinding.addUpdate.setVisibility(View.VISIBLE);
        });
        mBinding.edit.setOnClickListener(v2 -> {
            mBinding.addressEdit.getEditText().setText(mBinding.address.getText().toString());
            mBinding.addCancel.setVisibility(View.VISIBLE);
            mBinding.edit.setVisibility(View.GONE);
            mBinding.addressEdit.setVisibility(View.VISIBLE);
            mBinding.address.setVisibility(View.GONE);
            mBinding.addUpdate.setVisibility(View.VISIBLE);
        });
        mBinding.userEdit.setOnClickListener(v2 -> {
            mBinding.emailEdit.getEditText().setText(mBinding.email.getText().toString());
            mBinding.phoneEdit.getEditText().setText(mBinding.mobile.getText().toString());
            userEditOperation();
        });
        mBinding.addCancel.setOnClickListener(v4 -> {
            hideaddEditMode();
        });
        mBinding.userCancel.setOnClickListener(v5 -> {

            hideUserEditMode();
        });
        mBinding.userUpdate.setOnClickListener(v2 -> {
            bar = new ProgressDialog(getActivity());
            bar.setCancelable(false);
            bar.setMessage("Profile updating...");
            bar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            bar.show();
            hideUserEditMode();
            if (!Objects.requireNonNull(mBinding.emailEdit.getEditText()).getText().toString().equals("")) {
                mEmail = mBinding.emailEdit.getEditText().getText().toString();
            } else {
                mEmail = mSession.get("femail");
            }
            if (!Objects.requireNonNull(mBinding.phoneEdit.getEditText()).getText().toString().equals("")) {
                mMobile = mBinding.phoneEdit.getEditText().getText().toString();
            } else {
                mMobile = mSession.get("fmobile");
            }
            if (!mSession.get("age_enter").equals("")) {
                mDob = dob;
            } else {
                mDob = "";
            }
            mUsername = mSession.get("fname");
            Map<String, Object> user = new HashMap<>();
            user.put("mobile", mMobile);
            user.put("email", mEmail);
            user.put("dob", dob);
            mReference.update(user).addOnSuccessListener(aVoid -> {
                bar.dismiss();
                mBinding.emailEdit.getEditText().getText().clear();
                mBinding.phoneEdit.getEditText().getText().clear();
                mSession.set("dob", dob);
                Toast.makeText(getContext(), "User details updated", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onSuccess: " + userId);
            });
        });
        mBinding.addUpdate.setOnClickListener(v2 -> {
            bar = new ProgressDialog(getActivity());
            bar.setCancelable(false);
            bar.setMessage("Address updating...");
            bar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            bar.show();
            mBinding.addCancel.setVisibility(View.GONE);
            mBinding.edit.setVisibility(View.VISIBLE);
            mBinding.addressEdit.setVisibility(View.VISIBLE);
            mBinding.address.setVisibility(View.GONE);
            mBinding.addUpdate.setVisibility(View.GONE);
            if (!Objects.requireNonNull(mBinding.addressEdit.getEditText()).getText().toString().equals("")) {
                address = mBinding.addressEdit.getEditText().getText().toString();
            } else {
                address = mSession.get("faddress");
            }

            Map<String, Object> user = new HashMap<>();
            user.put("address", address);
            mReference.update(user).addOnSuccessListener(aVoid -> {
                bar.dismiss();
                mBinding.addressEdit.getEditText().getText().clear();
                mBinding.addressEdit.setVisibility(View.GONE);
                mBinding.address.setVisibility(View.VISIBLE);
                mSession.set("faddress", address);
                Toast.makeText(getContext(), "User details updated", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onSuccess: " + userId);
            });
        });
    }

    private void hideaddEditMode() {
        mBinding.addUpdate.setVisibility(View.GONE);
        mBinding.addCancel.setVisibility(View.GONE);
        mBinding.address.setVisibility(View.VISIBLE);
        mBinding.edit.setVisibility(View.VISIBLE);
        mBinding.addressEdit.setVisibility(View.GONE);
    }

    private void hideUserEditMode() {
        mBinding.emailEdit.getEditText().setText(mBinding.email.getText().toString());
        mBinding.phoneEdit.getEditText().setText(mBinding.mobile.getText().toString());
        mBinding.dob.setTextColor(Color.GRAY);
        mBinding.userEdit.setVisibility(View.VISIBLE);
        mBinding.userCancel.setVisibility(View.GONE);
        mBinding.email.setVisibility(View.VISIBLE);
        mBinding.mobile.setVisibility(View.VISIBLE);
        mBinding.emailEdit.setVisibility(View.GONE);
        mBinding.phoneEdit.setVisibility(View.GONE);
        mBinding.ivDOB.setVisibility(View.GONE);
        mBinding.userUpdate.setVisibility(View.GONE);
    }

    private void userEditOperation() {
        mBinding.userCancel.setVisibility(View.VISIBLE);
        mBinding.email.setVisibility(View.GONE);
        mBinding.mobile.setVisibility(View.GONE);
        mBinding.userEdit.setVisibility(View.GONE);
        mBinding.emailEdit.setVisibility(View.VISIBLE);
        mBinding.phoneEdit.setVisibility(View.VISIBLE);
        mBinding.ivDOB.setVisibility(View.VISIBLE);
        mBinding.userUpdate.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PHOTO_FOR_AVATAR && data != null && data.getData() != null) {
                filePath = data.getData();
                Picasso.get().load(filePath).error(R.drawable.profile_p).into(mBinding.profileImage);
            }
        }
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (!isConnected) {
            message = "Sorry! You're Not connected to internet";
            alertDialog.show();
            color = Color.RED;
            View parentView = getActivity().findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    private String getMonthName(final int index, final Locale locale, final boolean shortName) {
        String format = "%tB";

        if (shortName)
            format = "%tb";

        Calendar calendar = Calendar.getInstance(locale);
        calendar.set(Calendar.MONTH, index);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return String.format(locale, format, calendar);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setActionBarTitle("Profile");

        alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setCancelable(false);
        alertDialog.setTitle("No Internet Connection");
        alertDialog.setMessage("We can not detect any internet connection please check your internet connection and try again");
        alertDialog.setPositiveButton("Retry", (dialog, which) -> showSnack(isNetworkAvailable()));
        alertDialog.setNegativeButton("Close", (dialog, which) -> {
            dialog.cancel();
        });
        showSnack(isNetworkAvailable());
    }
    /*
    public boolean textInputemail() {
        String email = mBinding.emailEdit.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            mBinding.emailEdit.setError("Email field can't be empty");
            return false;
        } else {
            mBinding.emailEdit.setError(null);
            return true;
        }
    }

    public boolean textInputdob() {
        String dob = mSession.get("age_enter");
        if (dob.isEmpty()) {
            mBinding.dob.setTextColor(Color.RED);
            return false;
        } else {
            mBinding.emailEdit.setError(null);
            return true;
        }
    }

    public boolean textInputmobile() {
        String email = mBinding.phoneEdit.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            mBinding.phoneEdit.setError("Mobile field can't be empty");
            return false;
        } else {
            mBinding.phoneEdit.setError(null);
            return true;
        }
    }

    public boolean textInputaddress() {
        String email = mBinding.addressEdit.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            mBinding.addressEdit.setError("Address field can't be empty");
            return false;
        } else {
            mBinding.addressEdit.setError(null);
            return true;
        }
    }*/
}