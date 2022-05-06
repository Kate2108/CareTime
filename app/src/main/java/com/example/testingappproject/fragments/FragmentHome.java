package com.example.testingappproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingappproject.R;
import com.example.testingappproject.TrackerAdapter;
import com.example.testingappproject.data.MainViewModel;

public class FragmentHome extends Fragment {
    private OnFragmentSendDataListener fragmentSendDataListener;
    private RecyclerView rv;
    private TrackerAdapter trackerAdapter;
    private MainViewModel viewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentSendDataListener = (OnFragmentSendDataListener) context;
    }

    private void init(View view) {
        rv = view.findViewById(R.id.recyclerView_trackers);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        TrackerAdapter.OnTrackerClickListener trackerClickListener = position -> fragmentSendDataListener.onSendData(position);


        try {
            viewModel.getTrackerDatePointLiveData().observe(getViewLifecycleOwner(), listTrackers -> {
                trackerAdapter = new TrackerAdapter(trackerClickListener, getContext(), listTrackers);
                rv.setAdapter(trackerAdapter);
            });
        } catch (NullPointerException ex) {
            System.err.println(ex.getMessage());
        }
    }


    //to send data between two fragments we need to create an interface which our activity will implement
    public interface OnFragmentSendDataListener {
        void onSendData(int position);
    }


}