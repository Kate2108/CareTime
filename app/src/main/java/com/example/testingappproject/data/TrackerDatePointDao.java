package com.example.testingappproject.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.testingappproject.model.TrackerDatePoint;
import com.example.testingappproject.model.Date;

import java.util.List;

@Dao
public interface TrackerDatePointDao {

    @Query("SELECT * FROM dates ORDER BY dates.id DESC LIMIT 1")
    Date getLastDate();

    @Query("SELECT id FROM points WHERE tracker_id LIKE :tracker_id AND date_id LIKE :date_id")
    long getPointId(long tracker_id, long date_id);

    @Query("SELECT * FROM trackers, points WHERE tracker_id=trackers.id AND date_id LiKE :last_date_id")
    LiveData<List<TrackerDatePoint>> getTrackerPoint(long last_date_id);
}
