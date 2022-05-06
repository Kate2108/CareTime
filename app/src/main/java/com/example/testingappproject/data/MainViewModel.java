package com.example.testingappproject.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.testingappproject.App;
import com.example.testingappproject.model.TrackerDatePoint;

import java.util.List;

public class MainViewModel extends ViewModel {
    private LiveData<List<TrackerDatePoint>> data;

    public LiveData<List<TrackerDatePoint>> getTrackerDatePointLiveData(){
        if (data == null) {
            loadData();
        }
        return data;
    }

    public void loadData(){
//        we get livedata only by the last date, so that we don't get a bunch of everything once again,
//        that is, we always store data only for the current day, everything else is in the database
        Thread thread = new Thread(() -> {
            Log.d("test", "seeking live data");
            AppDb database = App.getInstance().getDatabase();
            long lastDateId = database.dateDao().getLastDateId();
            data = database.trackerDatePointDao().getTrackerPoint(lastDateId);
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

