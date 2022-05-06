package com.example.testingappproject.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Index;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.testingappproject.App;
import com.example.testingappproject.R;
import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.Tracker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Database(entities = {Tracker.class, Date.class, Point.class}, version = 1, exportSchema = false)
public abstract class AppDb extends RoomDatabase {
    public abstract TrackerDao trackerDao();
    public abstract DateDao dateDao();
    public abstract PointDao pointDao();
    public abstract TrackerDatePointDao trackerDatePointDao();

    private static AppDb INSTANCE;
    private static final String DB_NAME = "database";

    public static AppDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDb.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    AppDb.class,
                                    DB_NAME).addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    TrackerDao trackerDao = INSTANCE.trackerDao();

                                    trackerDao.insertTracker(new Tracker("Sleep", R.drawable.sleep, 8));
                                    trackerDao.insertTracker(new Tracker("Vitamins", R.drawable.vitamins, 2));
                                    trackerDao.insertTracker(new Tracker("Activity", R.drawable.activity, 10));
                                    trackerDao.insertTracker(new Tracker("Water", R.drawable.water, 10));

                                    Calendar calendar = new GregorianCalendar();
                                    Date currentDate = new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

                                    long newDateId = INSTANCE.dateDao().insertDate(currentDate);
                                    List<Tracker> trackerList = trackerDao.getAllTrackersAsList();
                                    for (int i = 0; i < trackerList.size(); i++) {
                                        INSTANCE.pointDao().insertPoint(new Point(trackerList.get(i).id, newDateId, 0));
                                    }
                                }
                            }).build();
                }
            }
        }
        return INSTANCE;
    }

}
