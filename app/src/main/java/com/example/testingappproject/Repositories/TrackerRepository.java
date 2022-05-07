package com.example.testingappproject.Repositories;

import com.example.testingappproject.data.TrackerDao;
import com.example.testingappproject.model.Tracker;

import java.util.List;

public class TrackerRepository {
    private TrackerDao trackerDao;

    public TrackerRepository(TrackerDao trackerDao) {
        this.trackerDao = trackerDao;
    }

    public List<Tracker> getAllTrackersAsList(){
        return trackerDao.getAllTrackersAsList();
    }

    public void insertTracker(Tracker tracker){
        trackerDao.insertTracker(tracker);
    }
}
