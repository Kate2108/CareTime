package com.example.testingappproject;

import java.io.Serializable;

public class TrackerItem implements Serializable {
    private String headline;
    private int progress;
    private int imgResource;

    public String getHeadline() {
        return headline;
    }

    public int getProgress() {
        return progress;
    }

    public int getImgResource() {
        return imgResource;
    }

    public TrackerItem(String headline, int progress, int imgResource) {
        this.headline = headline;
        this.progress = progress;
        this.imgResource = imgResource;
    }
}