package com.example.testingappproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentItem extends Fragment{

    public FragmentItem() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //hiding bottom navigation view
        try{
            //this block was wrapped in a try-catch construction because method findViewById may produce NullPointerException
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view);
            bottomNavigationView.setVisibility(View.GONE);
        }catch (NullPointerException ex){
            System.err.println(ex.getMessage());
        }

        return inflater.inflate(R.layout.fragment_item, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView tvHeadline = view.findViewById(R.id.tv_headline);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        TextView tvPoints = view.findViewById(R.id.tv_points);
        Button btnPlus = view.findViewById(R.id.btn_plus);
        Button btnMinus = view.findViewById(R.id.btn_minus);

        try{
            //receiving data from MainActivity with Bundle
            Bundle data = getArguments();
            //try-catch because getSerializable may produce NullPointerException
            TrackerItem trackerItem = (TrackerItem) data.getSerializable("trackerItem");
            //information about TrackerItem which was clicked in FragmentHome we display here
            tvHeadline.setText(trackerItem.getHeadline());
            progressBar.setProgress(trackerItem.getProgress());
            tvPoints.setText(trackerItem.getCurrentPoints() + "/" + trackerItem.getMaxPoints());
        }catch (NullPointerException ex){
            System.err.println(ex.getMessage());
        }
        super.onViewCreated(view, savedInstanceState);
    }

}