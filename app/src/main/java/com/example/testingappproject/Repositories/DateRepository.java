package com.example.testingappproject.Repositories;

import com.example.testingappproject.data.DateDao;
import com.example.testingappproject.model.Date;

import java.util.List;

public class DateRepository {
    private DateDao dateDao;

    public DateRepository(DateDao dateDao) {
        this.dateDao = dateDao;
    }

    public Long getLastDateId(){
        return dateDao.getLastDateId();
    }

    public List<Date> getAllDates(){
        return dateDao.getAllDates();
    }

    public long insertDate(Date date){
        return dateDao.insertDate(date);
    }
}
