package com.milkyway.dukan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SliderImage implements Serializable {

    private String categoryId;
    private String title;
    private String image;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public SliderImage(String categoryId, String title, String image) {
        this.categoryId = categoryId;
        this.title = title;
        this.image = image;
    }

    public SliderImage() {
    }
}
