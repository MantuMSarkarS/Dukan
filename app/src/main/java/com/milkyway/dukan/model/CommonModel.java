package com.milkyway.dukan.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommonModel implements Serializable {
    private String Image;
    private String Description;
    private String ModelName;
    private String Modelid;
    private String NewPrice;
    private String OldPrice;
    private List<String> RamSize;
    private String Discount;

    public CommonModel(String image, String description, String modelName, String modelid, String newPrice, String oldPrice, List<String> ramSize, String discount) {
        Image = image;
        Description = description;
        ModelName = modelName;
        Modelid = modelid;
        NewPrice = newPrice;
        OldPrice = oldPrice;
        RamSize = ramSize;
        Discount = discount;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String modelName) {
        ModelName = modelName;
    }

    public String getModelid() {
        return Modelid;
    }

    public void setModelid(String modelid) {
        Modelid = modelid;
    }

    public String getNewPrice() {
        return NewPrice;
    }

    public void setNewPrice(String newPrice) {
        NewPrice = newPrice;
    }

    public String getOldPrice() {
        return OldPrice;
    }

    public void setOldPrice(String oldPrice) {
        OldPrice = oldPrice;
    }

    public List<String> getRamSize() {
        return RamSize;
    }

    public void setRamSize(List<String> ramSize) {
        RamSize = ramSize;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
