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

}
