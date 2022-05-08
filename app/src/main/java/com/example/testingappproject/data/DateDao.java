package com.example.testingappproject.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.testingappproject.model.Date;

import java.util.List;

@Dao
public interface DateDao {
    @Query("SELECT id FROM dates ORDER BY dates.id DESC LIMIT 1")
    Long getLastDateId();

    @Query("SELECT * FROM dates")
    List<Date> getAllDates();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertDate(Date date);

    @Query("DELETE FROM dates")
    void clearDates();

}
