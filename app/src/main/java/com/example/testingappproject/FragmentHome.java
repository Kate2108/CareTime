package com.example.testingappproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    private ArrayList<TrackerItem> trackers;


    @Override
    public void onResume() {
        Toast.makeText(getActivity(), "on resume", Toast.LENGTH_SHORT).show();

        super.onResume();
    }

    //for interaction between Fragment and Activity (when we need to send some data from fragment to activity)
    //we should create interface with one method which arguments will be data that we want to send (TrackerItem)
    //then we need to implement this interface in our activity (MainActivity) and override our method
    interface FromFragmentToActivitySendData {
        void fragToActSendData(TrackerItem trackerItem);
    }
    //we create this to call fragToActSendData when item on Recyclerview will be clicked
    FromFragmentToActivitySendData fromFragmentToActivitySendData;


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

        //handling click on items in RecyclerView
        TrackerAdapter.OnTrackerItemClickListener trackerClickListener = new TrackerAdapter.OnTrackerItemClickListener() {
            @Override
            public void onTrackerItemClick(TrackerItem trackerItem, int position) {
                fromFragmentToActivitySendData.fragToActSendData(trackerItem);
            }
        };

        //setting our trackerClickListener to our RecyclerView
        TrackerAdapter trackerAdapter = new TrackerAdapter(trackers, trackerClickListener);
        rv.setAdapter(trackerAdapter);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;

    }

    private void setInitialData(){
        trackers = new ArrayList<>();
        trackers.add(new TrackerItem("Sleep", R.drawable.sleep, 20, 18));
        trackers.add(new TrackerItem("Water", R.drawable.water, 10, 0));
        trackers.add(new TrackerItem("Activity", R.drawable.activity, 10, 5));
        trackers.add(new TrackerItem("Vitamins", R.drawable.vitamins, 5, 5));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //В методе onAttach мы на вход получаем Activity,
        // к которому присоединен фрагмент.
        // Мы пытаемся привести это Activity к типу интерфейса,
        // чтобы можно было вызывать метод и передать туда trackerItem.
        // Теперь fragToActSendData() ссылается на Activity.
        try {
            fromFragmentToActivitySendData = (FromFragmentToActivitySendData) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FromFragmentToActivitySendData");
        }

    }
}