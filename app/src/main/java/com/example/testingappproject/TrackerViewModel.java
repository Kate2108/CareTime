package com.example.testingappproject;


import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.testingappproject.Repositories.DateRepository;
import com.example.testingappproject.Repositories.PointsRepository;
import com.example.testingappproject.Repositories.TrackerDatePointsRepository;
import com.example.testingappproject.Repositories.TrackerRepository;
import com.example.testingappproject.data.AppDb;
import com.example.testingappproject.data.DateDao;
import com.example.testingappproject.data.PointDao;
import com.example.testingappproject.data.TrackerDao;
import com.example.testingappproject.data.TrackerDatePointDao;
import com.example.testingappproject.model.TrackerDatePoint;

import java.util.List;

public class TrackerViewModel extends ViewModel {
    private Application application;

    private DateRepository dateRep;
    private PointsRepository pointsRep;
    private TrackerDatePointsRepository trackerDPRep;
    private TrackerRepository trackerRep;

    private LiveData<List<TrackerDatePoint>> liveData;

    public TrackerViewModel(Application application) {
        this.application = application;

        TrackerDao trackerDao = AppDb.getDatabase(application).trackerDao();
        DateDao dateDao = AppDb.getDatabase(application).dateDao();
        PointDao pointDao = AppDb.getDatabase(application).pointDao();
        TrackerDatePointDao trackerDatePointDao= AppDb.getDatabase(application).trackerDatePointDao();

        dateRep = new DateRepository(dateDao);
        pointsRep = new PointsRepository(pointDao);
        trackerRep = new TrackerRepository(trackerDao);
        trackerDPRep = new TrackerDatePointsRepository(trackerDatePointDao);

        long lastDateId = dateRep.getLastDateId();
        liveData = trackerDPRep.getTrackerPoint(lastDateId);
    }
}
