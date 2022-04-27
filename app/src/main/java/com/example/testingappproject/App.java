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
import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.Tracker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
        // onCreate будет вызываться при первом создании базы данных, сразу после создания таблиц.
        // onOpen вызывается при открытии базы данных. Поскольку доступ к DAO возможен только
        // после завершения этих методов, мы создаём новый поток, в котором получаем ссылку на
        // базу данных, затем получаем DAO и вставляем необходимые данные.
        database = Room.databaseBuilder(this, AppDb.class, "database").addCallback(new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("toradora", "App: entering");
//                        trackerDao = database.trackerDao();
                        trackerDao = getInstance().getTrackerDao();
                        Log.d("toradora", "App: got tracker dao");
                        //in onCreate method we add "default" values: trackers and "starter" date and points
                        trackerDao.insertTracker(new Tracker("Sleep", R.drawable.sleep, 8));
                        Log.d("toradora", "App: got tracker");
                        trackerDao.insertTracker(new Tracker("Vitamins", R.drawable.vitamins, 2));
                        Log.d("toradora", "App: got tracker");
                        trackerDao.insertTracker(new Tracker("Activity", R.drawable.activity, 10));
                        Log.d("toradora", "App: got tracker");
                        trackerDao.insertTracker(new Tracker("Water", R.drawable.water, 10));
                        Log.d("toradora", "App: trackers inserted");
                        //причины, по которым я выбрала добавлять сразу все тут, а не потом отдельно Points в MainActivity (то есть
                        // без задержки при первом запуске таймера) описаны мою на листочке(трудно нормально расписать это здесь в комментариях, легче и лучше объяснить, что я имею в виду),
                        // в кратце, это просто избегание лишних зависимостей
                        // получаем текущую дату
                        Calendar calendar = new GregorianCalendar();
                        Date currentDate = new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
                        //загружаем новую дату в бд и заодно записываем ее айди
                        long newDateId = App.getInstance().getDateDao().insertDate(currentDate);

                        List<Tracker> trackerList = App.getInstance().getTrackerDao().getAllTrackersAsList();
                        for (int i = 0; i < trackerList.size(); i++) {
                            //проходимся по всем трекерам и к каждому добавляем новый Point относящийся к новой дате
                            App.getInstance().getPointDao().insertPoint(new Point(trackerList.get(i).id, newDateId, 0));
                            Log.d("toradora", "App: points inserted");
                        }
                    }
                });
                thread.start();
                //join() это плохо, я знаю, но как тут без него, я не знаю
//                try {
//                    thread.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                super.onCreate(db);
            }
        }).build();
        // инициализируем dao (dao  нужны для запросов к бд: получить трекер какой-нибудь и тд)
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
