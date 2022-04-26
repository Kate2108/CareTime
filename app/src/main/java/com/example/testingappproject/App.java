package com.example.testingappproject;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.testingappproject.data.AppDb;
import com.example.testingappproject.data.DateDao;
import com.example.testingappproject.data.PointDao;
import com.example.testingappproject.data.TrackerDao;
import com.example.testingappproject.data.TrackerDatePointDao;
import com.example.testingappproject.model.Tracker;

//singleton class for interaction with database,
//we should create an instance of database instead of creating it everytime we need it
public class App extends Application {
    private static App instance;
    private TrackerDao trackerDao;
    private DateDao dateDao;
    private PointDao pointDao;
    private TrackerDatePointDao trackerDatePointDao;
    private AppDb database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDb.class, "database").addCallback(new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                trackerDao = database.trackerDao();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //in onCreate method we add "default" values
                        trackerDao.insertTracker(new Tracker("Sleep", R.drawable.sleep, 8));
                        trackerDao.insertTracker(new Tracker("Vitamins", R.drawable.vitamins, 2));
                        trackerDao.insertTracker(new Tracker("Activity", R.drawable.activity, 10));
                        trackerDao.insertTracker(new Tracker("Water", R.drawable.water, 10));
                        Log.d("toradora", "inserted");
                    }
                });
                thread.start();
                super.onCreate(db);
            }
        })
                .build();
        trackerDao = database.trackerDao();
        dateDao = database.dateDao();
        trackerDatePointDao = database.trackerDatePointDao();
        pointDao = database.pointDao();
    }

    public static App getInstance() {
        return instance;
    }

    public TrackerDao getTrackerDao() {
        return trackerDao;
    }

    public DateDao getDateDao(){
        return dateDao;
    }

    public TrackerDatePointDao getTrackerDatePointDao(){
        return trackerDatePointDao;
    }

    public PointDao getPointDao() {
        return pointDao;
    }
}
