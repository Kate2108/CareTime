package com.example.testingappproject.auxiliary;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.testingappproject.R;
import com.example.testingappproject.viewpager.UniversalFragment;

import java.util.ArrayList;
import java.util.List;

public class StarterPagerAdapter extends FragmentStateAdapter {
    private String[] healines;
    private String[] descriptions;
    private final int[] images = {R.drawable.vp_track, R.drawable.vp_motivate, R.drawable.vp_improve};

    public StarterPagerAdapter(FragmentActivity fa, Context context) {
        super(fa);

        Resources resources = context.getResources();
        healines = resources.getStringArray(R.array.view_pager_headlines);
        descriptions = resources.getStringArray(R.array.view_pager_descriptions);
    }

    @Override
    public Fragment createFragment(int position) {
        Bundle arguments = new Bundle();
        arguments.putString(UniversalFragment.VIEWPAGER_HEADLINES, healines[position]);
        arguments.putString(UniversalFragment.VIEWPAGER_DESCRIPTIONS, descriptions[position]);
        arguments.putInt(UniversalFragment.VIEWPAGER_IMAGE, images[position]);

        UniversalFragment universalFragment = new UniversalFragment();
        universalFragment.setArguments(arguments);

        return universalFragment;
    }

    @Override
    public int getItemCount() {
        return healines.length;
    }
}
