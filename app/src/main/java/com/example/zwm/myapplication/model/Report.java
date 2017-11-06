package com.example.zwm.myapplication.model;

import android.graphics.drawable.Drawable;

/**
 * Created by zwm12 on 2017/11/6.
 */

public class Report {
    private int imgDrawable;
    private String carType;
    private String shorting;
    private String summary;

    public int getImgDrawable() {
        return imgDrawable;
    }

    public void setImgDrawable(int imgDrawable) {
        this.imgDrawable = imgDrawable;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getShorting() {
        return shorting;
    }

    public void setShorting(String shorting) {
        this.shorting = shorting;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
