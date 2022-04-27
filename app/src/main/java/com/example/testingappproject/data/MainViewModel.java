package com.example.testingappproject.data;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.testingappproject.App;
import com.example.testingappproject.model.TrackerDatePoint;

import java.util.List;

//here we keep main LiveData
public class MainViewModel extends ViewModel {
    private LiveData<List<TrackerDatePoint>> trackerDatePointLiveData;

    public LiveData<List<TrackerDatePoint>> getTrackerDatePointLiveData(){
        // we need start method  here to get last information from database
        Thread thread = new Thread(() -> {
            start();
        });
        thread.start();
        //join() это плохо, я знаю, но как тут без него, я не знаю
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("toradora", "MainViewModel: getting trackerLiveData LiveData ? null: " + (trackerDatePointLiveData == null));
        return trackerDatePointLiveData;
    }

    public void start(){
        //получаем livedata !!только по последней дате, чтобы лишний раз кучу всего не получать,
        // то есть храним мы всегда данные только по текущему дню, все остальное лежит в базе данных, почти минимализм
        long lastDateId = App.getInstance().getDateDao().getLastDateId();
        trackerDatePointLiveData = App.getInstance().getTrackerDatePointDao().getTrackerPoint(lastDateId);
    }
}

