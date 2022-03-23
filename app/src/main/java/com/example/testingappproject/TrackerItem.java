package com.example.testingappproject;

import java.io.Serializable;

public class TrackerItem implements Serializable {
    private final String headline;
    private int progress;
    private final int imgResource;
    private String[] datesArray;
    private int[] pointsArray;
    private int maxPoints;
    private int currentPoints;

    public String getHeadline() {
        return headline;
    }

    public int getProgress() {
        return progress;
    }

    public int getImgResource() {
        return imgResource;
    }

    public TrackerItem(String headline, int imgResource, int maxPoints, int currentPoints) {
        this.headline = headline;
        this.imgResource = imgResource;
        this.maxPoints = maxPoints;
        this.currentPoints = currentPoints;
        datesArray = new String[7];
        pointsArray = new int[7];
        progress = (int) (currentPoints / Double.parseDouble(String.valueOf(maxPoints)) * 100);
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public boolean plusPoint(){
        if(currentPoints < maxPoints){
            currentPoints++;
            reCalculateProgress();
            return true;
        }else{
            return false;
        }
    }
    public boolean minusPoint(){
        if(currentPoints > 0){
            currentPoints--;
            reCalculateProgress();
            return true;
        }else{
            return false;
        }
    }

    private void reCalculateProgress(){
        progress = (int) (currentPoints / Double.parseDouble(String.valueOf(maxPoints)) * 100);
    }
}