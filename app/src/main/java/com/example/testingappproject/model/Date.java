package com.example.testingappproject.model;

import android.util.Log;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "dates", indices = {@Index(value = {"day", "month", "year"}, unique = true)})
public class Date {
    @PrimaryKey(autoGenerate = true)
    public long id;

    private int day;
    private int month;
    private int year;

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Date date = (Date) o;
        return day == date.day && month == date.month && year == date.year;
    }


    @Override
    public String toString() {
        return "Date{" +
                "id=" + id + "," +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                '}';
    }

    @Ignore
    public Date(long id, int day, int month, int year) {
        this.id = id;
        this.day = day;
        this.month = month;
        this.year = year;
    }
}
