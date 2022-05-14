package com.example.testingappproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.testingappproject.viewpager.DepthViewPagerTransformer;
import com.example.testingappproject.viewpager.StarterPagerAdapter;
import com.example.testingappproject.fragments.FragmentHome;
import com.example.testingappproject.fragments.FragmentItem;
import com.example.testingappproject.fragments.FragmentQuote;
import com.example.testingappproject.settings.FragmentSettings;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements FragmentHome.OnFragmentSendDataListener {
    private FragmentManager fragmentManager;
    public static ViewPager2 mPager;
    private static final String keyIsColdBoot = "isColdBoot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, MainService.class);
        startService(intent);


        mPager = findViewById(R.id.view_pager);
        mPager.setVisibility(View.VISIBLE);
        StarterPagerAdapter pagerAdapter = new StarterPagerAdapter(this, this);
        mPager.setPageTransformer(new DepthViewPagerTransformer());
        mPager.setAdapter(pagerAdapter);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getBoolean(keyIsColdBoot, true)){
//            mPager = findViewById(R.id.view_pager);
//            mPager.setVisibility(View.VISIBLE);
//            StarterPagerAdapter pagerAdapter = new StarterPagerAdapter(this, this);
//            mPager.setPageTransformer(new DepthViewPagerTransformer());
//            mPager.setAdapter(pagerAdapter);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean(keyIsColdBoot, false);
//            editor.apply();
        }


        setListenerOnBottomNavigationView();
    }


    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //this is a helper class that replaces the container with the fragment. You can replace or add fragments.
        fragmentTransaction.replace(R.id.frame_layout_content, fragment);
        fragmentTransaction.addToBackStack(null);
        //if you add fragments it will be added to the backStack. If you replace the fragment it will add only the last fragment
        fragmentTransaction.commit();
        // commit() performs the action
    }

    private void setListenerOnBottomNavigationView() {
        //to switch between fragments and to interact with we need FragmentManager and FragmentTransaction
        fragmentManager = getSupportFragmentManager();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        final Fragment fragmentHome = new FragmentHome();
        final Fragment fragmentSettings = new FragmentSettings();
        final Fragment fragmentQuote = new FragmentQuote();

        bottomNavigationView.setOnItemSelectedListener(item -> {
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
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    @Override
    public void onSendData(int position) {
        FragmentItem fragmentItem = new FragmentItem();
        openFragment(fragmentItem);
        fragmentItem.setSelectedItem(position);
    }

}