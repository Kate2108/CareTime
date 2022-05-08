package com.example.testingappproject.auxiliary;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.testingappproject.App;
import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.Tracker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimerTask;


// УДАЛИТЬ
public class MainTimerTask extends TimerTask {
    private final Context context;

    public MainTimerTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Calendar calendar = new GregorianCalendar();
        Date currentDate = new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

//        addNewDateToBd(currentDate);
        manageDailyQuote();
    }

    private void manageDailyQuote() {
        QuoteOfTheDay quoteOfTheDay = new QuoteOfTheDay();
        String[] quoteWithAuthor = quoteOfTheDay.getQuote();
        Log.d("toradora", quoteWithAuthor[0] + "");
        saveDailyQuote(quoteWithAuthor[0], quoteWithAuthor[1]);
    }

    private void saveDailyQuote(String quote, String author) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("quote", quote);
        editor.putString("quote-author", author);
        editor.apply();
    }

    private void addNewDateToBd(Date newDate) {
        long newDateId = App.getInstance().getDatabase().dateDao().insertDate(newDate);
        List<Tracker> trackerList = App.getInstance().getDatabase().trackerDao().getAllTrackersAsList();
        for (int i = 0; i < trackerList.size(); i++) {
            //проходимся по всем трекерам и к каждому добавляем новый Point относящийся к новой дате
            App.getInstance().getDatabase().pointDao().insertPoint(new Point(trackerList.get(i).id, newDateId, 0));
        }
    }
}
