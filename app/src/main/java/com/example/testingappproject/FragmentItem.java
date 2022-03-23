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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentItem extends Fragment{
    private TrackerItem trackerItem;
    private TextView tvHeadline;
    private ProgressBar progressBar;
    private TextView tvPoints;

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
            //we can interact with activity to which our fragment is attached with getActivity method
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view);
            bottomNavigationView.setVisibility(View.GONE);
        }catch (NullPointerException ex){
            System.err.println(ex.getMessage());
        }

        return inflater.inflate(R.layout.fragment_item, container, false);

    }

    @Override
    public void onDestroyView() {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setVisibility(View.VISIBLE);
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //we find our views by id in fragment in onViewCreated method because in onCreate it may produce NullPointerException
        //also in fragments we can't just call findViewById (like in activities), to find view we need to use argument View view from method
        //in which we are going to find views
        tvHeadline = view.findViewById(R.id.tv_headline);
        progressBar = view.findViewById(R.id.progressBar);
        tvPoints = view.findViewById(R.id.tv_points);
        Button btnPlus = view.findViewById(R.id.btn_plus);
        Button btnMinus = view.findViewById(R.id.btn_minus);

        receiveDataFromActivity();

        //to handle click we need to create an OnClickListener
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //with id we will find what View was clicked and do something we want to
                switch (view.getId()){
                    case R.id.btn_plus:
                        trackerItem.plusPoint();
                        receiveDataFromActivity();
                        break;
                    case R.id.btn_minus:
                        trackerItem.minusPoint();
                        receiveDataFromActivity();
                }
            }
        };
        //attaching our onClickListener to buttons
        btnPlus.setOnClickListener(onClickListener);
        btnMinus.setOnClickListener(onClickListener);

        super.onViewCreated(view, savedInstanceState);
    }

    private void receiveDataFromActivity(){
        try{
            //receiving data from MainActivity with Bundle
            Bundle data = getArguments();
            //try-catch because getSerializable may produce NullPointerException
            trackerItem = (TrackerItem) data.getSerializable("trackerItem");
            //information about TrackerItem which was clicked in FragmentHome we display here
            tvHeadline.setText(trackerItem.getHeadline());
            progressBar.setProgress(trackerItem.getProgress());
            tvPoints.setText(trackerItem.getCurrentPoints() + "/" + trackerItem.getMaxPoints());
        }catch (NullPointerException ex){
            System.err.println(ex.getMessage());
        }
    }

}