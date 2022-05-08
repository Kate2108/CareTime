package com.example.testingappproject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.example.testingappproject.auxiliary.QuoteOfTheDay;
import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.Tracker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

// we need Service class to do some stuff even when our app is closed
// here we keep timer to add new dates to our data base
public class MainService extends Service {
    private final static long PERIOD_TIMER = 86_400_000;
    private Handler serviceHandler;
    private Runnable serviceRunnable;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        serviceHandler.removeCallbacks(serviceRunnable);
        // Добавляем Runnable-объект serviceRunnable в очередь
        // сообщений, объект должен быть запущен после задержки в PERIOD_TIMER
        serviceHandler.postDelayed(serviceRunnable, PERIOD_TIMER);

        // If we get killed, after returning from here, restart
        //возвращаем параметр, которые устанавливает, каким образом обработать перезапуски
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        serviceHandler = new Handler();
        serviceRunnable = new Runnable() {
            public void run() {
                Calendar calendar = new GregorianCalendar();
                Date currentDate = new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
                addNewDateToBd(currentDate);
                manageDailyQuote();
                serviceHandler.postDelayed(this, PERIOD_TIMER);
            }
        };
    }

    private void addNewDateToBd(Date newDate) {
        long newDateId = App.getInstance().getDatabase().dateDao().insertDate(newDate);
        List<Tracker> trackerList = App.getInstance().getDatabase().trackerDao().getAllTrackersAsList();
        for (int i = 0; i < trackerList.size(); i++) {
            //проходимся по всем трекерам и к каждому добавляем новый Point относящийся к новой дате
            App.getInstance().getDatabase().pointDao().insertPoint(new Point(trackerList.get(i).id, newDateId, 0));
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceHandler.removeCallbacks(serviceRunnable);
    }

    private void manageDailyQuote() {
        QuoteOfTheDay quoteOfTheDay = new QuoteOfTheDay();
        String[] quoteWithAuthor = quoteOfTheDay.getQuote();
        saveDailyQuote(quoteWithAuthor[0], quoteWithAuthor[1]);
    }

    private void saveDailyQuote(String quote, String author) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("quote", quote);
        editor.putString("quote-author", author);
        editor.apply();
    }
}