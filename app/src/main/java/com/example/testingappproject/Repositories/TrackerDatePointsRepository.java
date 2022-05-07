package com.example.testingappproject.Repositories;

import androidx.lifecycle.LiveData;

import com.example.testingappproject.data.TrackerDatePointDao;
import com.example.testingappproject.model.TrackerDatePoint;

import java.util.List;

public class TrackerDatePointsRepository {
    private TrackerDatePointDao trackerDatePointDao;

    public TrackerDatePointsRepository(TrackerDatePointDao trackerDatePointDao) {
        this.trackerDatePointDao = trackerDatePointDao;
    }

    public long getPointId(long tracker_id, long date_id){
        return trackerDatePointDao.getPointId(tracker_id, date_id);
    }

    public LiveData<List<TrackerDatePoint>> getTrackerPoint(long last_date_id){
        return trackerDatePointDao.getTrackerPoint(last_date_id);
    }
}
