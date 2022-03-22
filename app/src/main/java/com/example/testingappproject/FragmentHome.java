package com.example.testingappproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    ArrayList<TrackerItem> trackers;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RecyclerView rv = view.findViewById(R.id.recyclerView_trackers);
        //to speed up performance
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        setInitialData();

        TrackerAdapter.OnTrackerItemClickListener trackerClickListener = new TrackerAdapter.OnTrackerItemClickListener() {
            @Override
            public void onTrackerItemClick(TrackerItem trackerItem, int position) {
                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtra(TrackerItem.class.getSimpleName(), trackers.get(position));
                startActivity(intent);
            }
        };

        TrackerAdapter trackerAdapter = new TrackerAdapter(trackers, trackerClickListener);
        rv.setAdapter(trackerAdapter);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void setInitialData(){
        trackers = new ArrayList<>();
        trackers.add(new TrackerItem("Sleep", R.drawable.sleep, 20, 18));
        trackers.add(new TrackerItem("Water", R.drawable.water, 10, 0));
        trackers.add(new TrackerItem("Activity", R.drawable.activity, 10, 5));
        trackers.add(new TrackerItem("Vitamins", R.drawable.vitamins, 5, 5));
    }
}