package com.example.testingappproject.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Tracker;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DateDao {

    @Query("SELECT * FROM dates")
    List<Date> getAllDates();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertDate(Date date);

    @Query("DELETE FROM dates")
    void clearDates();

}
