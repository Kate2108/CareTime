package com.example.testingappproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FragmentItem extends Fragment {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView tvHeadline = view.findViewById(R.id.tv_headline);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        TextView tvPoints = view.findViewById(R.id.tv_points);

        Bundle data = getArguments();
        TrackerItem trackerItem = (TrackerItem) data.getSerializable("trackerItem");
        tvHeadline.setText(trackerItem.getHeadline());
        progressBar.setProgress(trackerItem.getProgress());
        tvPoints.setText(trackerItem.getCurrentPoints() + "/" + trackerItem.getMaxPoints());

        super.onViewCreated(view, savedInstanceState);
    }

    public void setSelectedTracker(TrackerItem trackerItem){
        tvHeadline.setText(trackerItem.getHeadline());
        progressBar.setProgress(trackerItem.getProgress());
        tvPoints.setText(trackerItem.getCurrentPoints() + "/" + trackerItem.getMaxPoints());
    }
}