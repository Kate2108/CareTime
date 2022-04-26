package com.example.testingappproject.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Tracker;

//onUpdate = CASCADE?
@Entity(tableName = "points", foreignKeys = {@ForeignKey(entity = Tracker.class, parentColumns = "id", childColumns = "tracker_id", onDelete = CASCADE),
        @ForeignKey(entity = Date.class, parentColumns = "id", childColumns = "date_id", onDelete = CASCADE)},
        indices = {@Index(value = {"tracker_id", "date_id"}, unique = true)})
public class Point {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "tracker_id")
    public long trackerId;

    @ColumnInfo(name = "date_id")
    public long dateId;

    public int points;

    public Point(long trackerId, long dateId, int points) {
        this.points = points;
        this.trackerId = trackerId;
        this.dateId = dateId;
    }

    @Ignore
    public Point(long id, long trackerId, long dateId, int points) {
        this.id = id;
        this.trackerId = trackerId;
        this.dateId = dateId;
        this.points = points;
    }

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id + "," +
                "trackerId=" + trackerId +
                ", dateId=" + dateId +
                ", points=" + points +
                '}';
    }
}
