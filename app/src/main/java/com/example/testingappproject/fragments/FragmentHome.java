package com.example.testingappproject.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.testingappproject.App;
import com.example.testingappproject.data.MainViewModel;
import com.example.testingappproject.R;
import com.example.testingappproject.TrackerAdapter;
import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.Tracker;
import com.example.testingappproject.model.TrackerDatePoint;

import java.util.List;

public class FragmentHome extends Fragment {
    //to send data between two fragments we nedd to create an interface which our activity will implement
    public interface OnFragmentSendDataListener {
        void onSendData(int position);
    }

    private OnFragmentSendDataListener fragmentSendDataListener;

    private RecyclerView rv;
    private TrackerAdapter trackerAdapter;
    private MainViewModel mainViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mainViewModel = obtainViewModel(getActivity());
        Log.d("toradora", "FragmentHome: obtainViewModel in onCreateView");
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentSendDataListener = (OnFragmentSendDataListener) context;
    }


    private void init(View view){
        rv = view.findViewById(R.id.recyclerView_trackers);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        TrackerAdapter.OnTrackerClickListener trackerClickListener = position -> {
            fragmentSendDataListener.onSendData(position);
        };

        try {
            Log.d("toradora", "FragmentHome: loading liveData to adapter");
            mainViewModel.getTrackerDatePointLiveData().observe(getViewLifecycleOwner(), listTrackers -> {
                trackerAdapter = new TrackerAdapter(trackerClickListener, getContext(), listTrackers);
                rv.setAdapter(trackerAdapter);
            });
        }catch (NullPointerException ex){
            System.err.println(ex.getMessage());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mainViewModel.start();
            }
        });
    }
    public MainViewModel obtainViewModel(FragmentActivity fragmentActivity){
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        return mainViewModel;
    }

}