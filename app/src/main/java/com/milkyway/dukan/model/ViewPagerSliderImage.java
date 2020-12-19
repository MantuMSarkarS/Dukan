package com.milkyway.dukan.model;

import java.io.Serializable;

public class ViewPagerSliderImage implements Serializable {

    private String id;
    private String image;

    public ViewPagerSliderImage(String id, String image) {
        this.id = id;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ViewPagerSliderImage() {
    }
}
