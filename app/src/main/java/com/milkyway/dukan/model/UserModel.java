package com.milkyway.dukan.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Serializable {
    @SerializedName("address")
    private String mAddress;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("mobile")
    private String mMobile;
    @SerializedName("dob")
    private String mDob;
    @SerializedName("name")
    private String mName;
    @SerializedName("password")
    private String mPassword;

    public UserModel(String mAddress, String mEmail, String mMobile, String mDob, String mName, String mPassword) {
        this.mAddress = mAddress;
        this.mEmail = mEmail;
        this.mMobile = mMobile;
        this.mDob = mDob;
        this.mName = mName;
        this.mPassword = mPassword;
    }

    public UserModel(String mEmail, String mPassword) {
        this.mEmail = mEmail;
        this.mPassword = mPassword;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmMobile() {
        return mMobile;
    }

    public void setmMobile(String mMobile) {
        this.mMobile = mMobile;
    }

    public String getmDob() {
        return mDob;
    }

    public void setmDob(String mDob) {
        this.mDob = mDob;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
