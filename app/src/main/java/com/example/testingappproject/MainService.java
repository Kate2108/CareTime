package com.example.testingappproject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.testingappproject.auxiliary.QuoteOfTheDay;
import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.Tracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

// we need Service class to do some stuff even when our app is closed
// here we keep timer to add new dates to our data base
public class MainService extends Service {
    private final static long PERIOD_TIMER = 86_400_000;
    private Handler serviceHandler;
    private Runnable serviceRunnable;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // получить текущее время
        // из ближайшей полночи вычесть это время
        // чтобы потом трекеры и цитата всегда обновлялись в полночь
        java.util.Date currentTime = new java.util.Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String[] timeText = timeFormat.format(currentTime).split(":");
        long timeUntilMidnight = 24 * 60 * 60 * 1000 - Integer.parseInt(timeText[0]) * 60 * 60 * 1000 - Integer.parseInt(timeText[1]) * 60 * 1000 - Integer.parseInt(timeText[2]) * 1000;

        // Добавляем Runnable-объект serviceRunnable в очередь
        // сообщений, объект должен быть запущен после задержки в PERIOD_TIMER
        serviceHandler.postDelayed(serviceRunnable, timeUntilMidnight);

        // If we get killed, after returning from here, no restart
        //возвращаем параметр, которые устанавливает, каким образом обработать перезапуски
        return Service.START_NOT_STICKY;
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
                Log.d("toradora", "service runnable");
                Calendar calendar = new GregorianCalendar();
                Date currentDate = new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

                Thread threadForInsertion = new Thread(() -> addNewDateToBd(currentDate));
                threadForInsertion.start();
                Thread threadForManagingQuote = new Thread(() -> manageDailyQuote());
                threadForManagingQuote.start();

                serviceHandler.postDelayed(this, PERIOD_TIMER);
            }
        };
    }

    private void addNewDateToBd(Date newDate) {
        synchronized (App.getInstance().getDatabase().trackerDao()) {
            long newDateId = App.getInstance().getDatabase().dateDao().insertDate(newDate);
            Log.d("toradora", "new Date id: " + newDateId);
            List<Tracker> trackerList = App.getInstance().getDatabase().trackerDao().getAllTrackersAsList();
            for (int i = 0; i < trackerList.size(); i++) {
                Log.d("toradora", "new point inserted");
                //проходимся по всем трекерам и к каждому добавляем новый Point относящийся к новой дате
                App.getInstance().getDatabase().pointDao().insertPoint(new Point(trackerList.get(i).id, newDateId, 0));
            }
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
        Log.d("toradora", Arrays.toString(quoteWithAuthor));
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