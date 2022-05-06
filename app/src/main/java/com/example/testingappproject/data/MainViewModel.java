package com.example.testingappproject.data;

import android.content.Context;
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
        //получаем livedata !!только по последней дате, чтобы лишний раз кучу всего не получать,
        // то есть храним мы всегда данные только по текущему дню, все остальное лежит в базе данных, почти минимализм
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

