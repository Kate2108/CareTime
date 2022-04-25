package com.example.testingappproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import com.example.testingappproject.fragments.FragmentHome;
import com.example.testingappproject.fragments.FragmentItem;
import com.example.testingappproject.fragments.FragmentQuote;
import com.example.testingappproject.fragments.FragmentSettings;
import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.Tracker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements FragmentHome.OnFragmentSendDataListener {
    BottomNavigationView bottomNavigationView;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private Date savedDate;
    private Date currentDate;
    private SharedPreferences sharedPreferences;
    public static final String APP_CURRENT_DATE = "currentdate";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        try{
            getSupportActionBar().hide();
        }catch (NullPointerException ex){
            System.err.println(ex.getMessage());
        }
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(APP_CURRENT_DATE, Context.MODE_PRIVATE);
        savedDate = new Date(sharedPreferences.getInt("day", -1),
                sharedPreferences.getInt("month", -1),
                sharedPreferences.getInt("year", -1));

        Calendar calendar = new GregorianCalendar();
        currentDate = new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        if(!savedDate.equals(currentDate)){
            rewriteSavedDate();
            addNewDateToBd(savedDate);
        }
        setListenerOnBottomNavigationView();
    }

    private void addNewDateToBd(Date newDate) {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //загружаем новую дату в бд и заодно записываем ее айди
//                long newDateId = App.getInstance().getDateDao().insertDate(newDate);
//                List<Tracker> trackerList = App.getInstance().getTrackerDao().getAllTrackersAsList();
//                for (int i = 0; i < trackerList.size(); i++) {
//                    //проходимся по всем трекерам и к каждому добавляем новый Point относящийся к новой дате
//                    App.getInstance().getPointDao().insertPoint(new Point(trackerList.get(i).id, newDateId, 0));
//                }
//            }
//        });
        Thread thread = new Thread(() ->{
            long newDateId = App.getInstance().getDateDao().insertDate(newDate);
            List<Tracker> trackerList = App.getInstance().getTrackerDao().getAllTrackersAsList();
            for (int i = 0; i < trackerList.size(); i++) {
                //проходимся по всем трекерам и к каждому добавляем новый Point относящийся к новой дате
                App.getInstance().getPointDao().insertPoint(new Point(trackerList.get(i).id, newDateId, 0));
            }});
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void rewriteSavedDate() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        savedDate = currentDate;
        editor.putInt("day", savedDate.day);
        editor.putInt("month", savedDate.month);
        editor.putInt("year", savedDate.year);
        editor.apply();
    }

    private void openFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        //this is a helper class that replaces the container with the fragment. You can replace or add fragments.
        fragmentTransaction.replace(R.id.frame_layout_content, fragment);
        fragmentTransaction.addToBackStack(null);
        //if you add fragments it will be added to the backStack. If you replace the fragment it will add only the last fragment
        fragmentTransaction.commit();
        // commit() performs the action
    }

    private void setListenerOnBottomNavigationView(){
        //to switch between fragments and to interact with we need FragmentManager and FragmentTransaction
        fragmentManager = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        final Fragment fragmentHome = new FragmentHome();
        final Fragment fragmentSettings = new FragmentSettings();
        final Fragment fragmentQuote = new FragmentQuote();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        openFragment(fragmentHome);
                        return true;
                    case R.id.settings:
                        openFragment(fragmentSettings);
                        return true;
                    case R.id.quote:
                        openFragment(fragmentQuote);
                        return true;
                }
                return false;
            }
        });;
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    @Override
    public void onSendData(int position) {
        FragmentItem fragmentItem = new FragmentItem();
        if(fragmentItem != null) {
            openFragment(fragmentItem);
            fragmentItem.setSelectedItem(position);
        }
    }
}