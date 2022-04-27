package com.example.testingappproject;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.Tracker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

// we need Service class to do some stuff even when our app is closed
// here we keep timer to add new dates to our data base
public class MyService extends Service {
    private final static long PERIOD_TIMER = 86_400_000;
    private ServiceHandler serviceHandler;
    private Runnable serviceRunnable;


    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // взаимодействие с UI элементами тут

        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("toradora", "MyService: onStartCommand() starting handler");

//        serviceHandler.removeCallbacks(serviceRunnable);
        // Добавляем Runnable-объект serviceRunnable в очередь
        // сообщений, объект должен быть запущен после задержки в PERIOD_TIMER мс
        serviceHandler.postDelayed(serviceRunnable, PERIOD_TIMER);

        // If we get killed, after returning from here, restart
        //возвращаем параметр, которые устанавливает, каким образом обработать перезапуски
        //Service.START_STICKY - стандартное поведение: обработчик onStartCommand() будет вызываться при повторном запуске сервиса после преждевременного завершения работы
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d("toradora", "MyService: onCreate() initialization");
        serviceRunnable = new Runnable() {
            public void run() {
                Calendar calendar = new GregorianCalendar();
                Date currentDate = new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
                addNewDateToBd(currentDate);
                serviceHandler.postDelayed(this, PERIOD_TIMER);
            }
        };
    }

    private void addNewDateToBd(Date newDate) {
        long newDateId = App.getInstance().getDateDao().insertDate(newDate);
        List<Tracker> trackerList = App.getInstance().getTrackerDao().getAllTrackersAsList();
        for (int i = 0; i < trackerList.size(); i++) {
            //проходимся по всем трекерам и к каждому добавляем новый Point относящийся к новой дате
            App.getInstance().getPointDao().insertPoint(new Point(trackerList.get(i).id, newDateId, 0));
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
}