package com.example.testingappproject.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.testingappproject.App;
import com.example.testingappproject.model.TrackerDatePoint;

import java.util.List;

public class MainViewModel extends ViewModel {
    private LiveData<List<TrackerDatePoint>> trackerDatePointLiveData;

    public LiveData<List<TrackerDatePoint>> getTrackerDatePointLiveData(){
        start();
        return trackerDatePointLiveData;
    }

    public synchronized void start(){
        //получаем livedata !!только по последней дате, чтобы лишний раз кучу всего не получать,
        // то есть храним мы всегда данные только по текущему дню, все остальное лежит в базе данных, почти минимализм
        Log.d("toradora", " MVM start()");
        AppDb database = App.getInstance().getDatabase();
        long lastDateId = database.dateDao().getLastDateId();
        trackerDatePointLiveData = database.trackerDatePointDao().getTrackerPoint(lastDateId);
    }
}

