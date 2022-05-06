package com.example.testingappproject;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.testingappproject.data.AppDb;
import com.example.testingappproject.data.TrackerDao;
import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.Tracker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

//singleton class for interaction with database,
//we should create an instance of database instead of creating it everytime we need it
public class App extends Application {
    private static volatile App instance;
    private static final String DB_NAME = "database";
    private AppDb database;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("test", "App: on Create");
        instance = this;
//        onCreate will be called the first time the database is created, immediately after the tables are created.
//        onOpen is called when the database is opened. Since access to the DAO is only possible
//        after completing these methods, we create a new thread in which we get a reference to
//        the database, then we get the DAO and insert the necessary data
        RoomDatabase.Callback callback = new RoomDatabase.Callback() {
            @Override
             public synchronized void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Log.d("test", "inserting default data");
                Thread thread = new Thread(()-> insertToDb());
                thread.start();
            }
        };
        database = Room.databaseBuilder(this, AppDb.class, DB_NAME).addCallback(callback).build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDb getDatabase() {
        return database;
    }

    public void insertToDb(){
        AppDb database = instance.getDatabase();
        TrackerDao trackerDao = App.getInstance().getDatabase().trackerDao();

        trackerDao.insertTracker(new Tracker("Sleep", R.drawable.sleep, 8));
        trackerDao.insertTracker(new Tracker("Vitamins", R.drawable.vitamins, 2));
        trackerDao.insertTracker(new Tracker("Activity", R.drawable.activity, 10));
        trackerDao.insertTracker(new Tracker("Water", R.drawable.water, 10));

        // получаем текущую дату
        Calendar calendar = new GregorianCalendar();
        Date currentDate = new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        //загружаем новую дату в бд и заодно записываем ее айди

        long newDateId = database.dateDao().insertDate(currentDate);
        List<Tracker> trackerList = trackerDao.getAllTrackersAsList();
        for (int i = 0; i < trackerList.size(); i++) {
            //проходимся по всем трекерам и к каждому добавляем новый Point относящийся к новой дате
            database.pointDao().insertPoint(new Point(trackerList.get(i).id, newDateId, 0));
        }
    }
}
