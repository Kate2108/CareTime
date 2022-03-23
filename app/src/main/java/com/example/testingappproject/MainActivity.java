package com.example.testingappproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements FragmentHome.FromFragmentToActivitySendData {
    BottomNavigationView bottomNavigationView;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

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

    private void openFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        //this is a helper class that replaces the container with the fragment. You can replace or add fragments.
        fragmentTransaction.replace(R.id.frame_layout_content, fragment);
        fragmentTransaction.addToBackStack(null);
        //if you add fragments it will be added to the backStack. If you replace the fragment it will add only the last fragment
        fragmentTransaction.commit();
        // commit() performs the action
    }

    @Override
    public void fragToActSendData(TrackerItem trackerItem) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content, new FragmentItem());
        //here we put clicked TrackedItem in Bundle and start FragmentItem with this Bundle
        FragmentItem fragmentItem = new FragmentItem();
        Bundle args = new Bundle();
        args.putSerializable("trackerItem", trackerItem);
        fragmentItem.setArguments(args);
        //completing our work with replacing fragments
        fragmentTransaction.replace(R.id.frame_layout_content, fragmentItem);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}