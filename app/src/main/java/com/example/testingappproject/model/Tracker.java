package com.example.testingappproject.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "trackers", indices = {@Index(value = "headline", unique = true)})
public class Tracker {
    @PrimaryKey(autoGenerate = true)
    public long id;

    private String headline;

    @ColumnInfo(name = "img_res")
    private int imgResource;

    @ColumnInfo(name = "max_points")
    private int maxPoints;


    public Tracker(String headline, int imgResource, int maxPoints) {
        this.headline = headline;
        this.imgResource = imgResource;
        this.maxPoints = maxPoints;
    }

    @Override
    public String toString() {
        return "Tracker{" +
                "headline='" + headline + '\'' +
                ", imgResource=" + imgResource +
                ", maxPoints=" + maxPoints +
                '}';
    }

    public String getHeadline() {
        return headline;
    }

    public int getImgResource() {
        return imgResource;
    }

    public int getMaxPoints() {
        return maxPoints;
    }
}
