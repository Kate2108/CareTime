package com.example.testingappproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements FragmentHome.OnFragmentSendDataListener {
    BottomNavigationView bottomNavigationView;
    private Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

//        final FragmentManager fragmentManager = getSupportFragmentManager();
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

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //this is a helper class that replaces the container with the fragment. You can replace or add fragments.
        transaction.replace(R.id.frame_layout_content, fragment);
        transaction.addToBackStack(null); //if you add fragments it will be added to the backStack. If you replace the fragment it will add only the last fragment
        transaction.commit(); // commit() performs the action
    }

    private void init(){
        Date date = new Date();
        if(!date.equals(currentDate)){
            //do smth
        }
    }

    @Override
    public void onSendData(TrackerItem trackerItem) {
        FragmentItem fragmentItem = (FragmentItem) getSupportFragmentManager().findFragmentById(R.id.fragment_item);
        if (fragmentItem != null) {
            fragmentItem.setSelectedTracker(trackerItem);
        }
        FragmentItem fragment = new FragmentItem(); // Фрагмент, которым собираетесь заменить первый фрагмент
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction(); // Или getSupportFragmentManager(), если используете support.v4
        transaction.replace(R.id.frame_layout_content, fragment); // Заменяете вторым фрагментом. Т.е. вместо метода `add()`, используете метод `replace()`
        transaction.addToBackStack(null); // Добавляете в backstack, чтобы можно было вернутся обратно
        transaction.commit();
    }
}