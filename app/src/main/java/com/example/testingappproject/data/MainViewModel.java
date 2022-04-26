package com.example.testingappproject.data;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.testingappproject.App;
import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.Tracker;
import com.example.testingappproject.model.TrackerDatePoint;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

//here we keep main LiveData
public class MainViewModel extends ViewModel {
    private LiveData<List<TrackerDatePoint>> trackerDatePointLiveData;


    public LiveData<List<TrackerDatePoint>> getTrackerDatePointLiveData(){
        start();
        if(trackerDatePointLiveData == null){
            throw new NullPointerException("trackerDatePointLiveData is null");
        }
        return trackerDatePointLiveData;
    }

    public void start(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //при  первом запуске приложения логично что все бд будут пустыми, но проблема в том,
                // что новые сверку дат мы делаем в MainActivity и если текущая дата не совпадает с сохраненной
                // в SharedPreferences, то добавляем мы новую дату и очки к ней именно там, но там же мы и получаем
                // liveData, которая не должна быть пустой
                Date lastDate = App.getInstance().getTrackerDatePointDao().getLastDate();
                if(lastDate == null){
                    //when app first time execute of course last date will be null
                    Calendar calendar = new GregorianCalendar();
                    Date currentDate = new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
                    addFirstDate(currentDate);
                }
                Log.d("toradora", "looking for livedata");
                //получаем livedata !!только по последней дате, чтобы лишний раз кучу всего не получать,
                //по сути мы получаем то, что только что добавляли в addFirstDate()
                trackerDatePointLiveData = App.getInstance().getTrackerDatePointDao().getTrackerPoint(lastDate.id);
            }
        });
        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    private void addFirstDate(Date currentDate) {
        //записываем в бд новую дату и заодно запоминаем ее id, чтобы потом к ней прицепить Point
        long fistId = App.getInstance().getDateDao().insertDate(currentDate);
        //чтобы к каждому трекеру записать его Point получаем лист трекеров
        List<Tracker> list = App.getInstance().getTrackerDao().getAllTrackersAsList();
        for (int i = 0; i < list.size(); i++) {
            //проходимся по всем трекерам и к каждому добавляем новый Point
            App.getInstance().getPointDao().insertPoint(new Point(list.get(i).id, fistId, 0));
        }
        //теперь в бд есть новая дата и прицепленные к ней Points
    }
}

