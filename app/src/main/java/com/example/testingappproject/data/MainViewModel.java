package com.example.testingappproject.data;

import android.os.Build;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testingappproject.App;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.Tracker;
import com.example.testingappproject.model.TrackerDatePoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainViewModel extends ViewModel {
    private LiveData<List<TrackerDatePoint>> data;

    public LiveData<List<TrackerDatePoint>> getTrackerDatePointLiveData(){
        if (data == null) {
            Thread thread = new Thread(this::loadData);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public void loadData(){

//        we get livedata only by the last date, so that we don't get a bunch of everything once again,
//        that is, we always store data only for the current day, everything else is in the database

//            App.getInstance().getDatabase().trackerDao().getAllTrackersAsList();
//            try {
//                Thread.currentThread().sleep(15000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        AppDb database = App.getInstance().getDatabase();
        Log.d("toradora", "getting live data");
        long lastDateId = database.dateDao().getLastDateId();
        data = database.trackerDatePointDao().getTrackerPoints(lastDateId);
    }
}

