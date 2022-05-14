package com.example.testingappproject.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.testingappproject.App;
import com.example.testingappproject.R;
import com.example.testingappproject.data.MainViewModel;
import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.TrackerDatePoint;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class FragmentItem extends Fragment {
    private TextView tvHeadline;
    private ProgressBar progressBar;
    private TextView tvPoints;
    private TextView tvStatsDates;
    private TextView tvStats;
    private int position;
    private int currentStateOfPoints;
    private int maxStateOfPoints;
    private boolean wasChanged = false;
    private Handler handler;

    private long tracker_id;
    private long date_id;

    private MainViewModel viewModel;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        handler = new Handler();
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        //hiding bottom navigation view
        try {
            //this block was wrapped in a try-catch construction because method findViewById may produce NullPointerException
            //we can interact with activity to which our fragment is attached with getActivity method
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view);
            bottomNavigationView.setVisibility(View.GONE);
        } catch (NullPointerException ex) {
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
        tvStatsDates = view.findViewById(R.id.tv_dates_stats);
        tvStats = view.findViewById(R.id.tv_stats);

        ImageButton btnPlus = view.findViewById(R.id.btn_plus);
        ImageButton btnMinus = view.findViewById(R.id.btn_minus);

        viewModel.getTrackerDatePointLiveData().observe(getViewLifecycleOwner(), trackerDatePoints -> {
            TrackerDatePoint tracker = trackerDatePoints.get(position);
            tracker_id = tracker.tracker_id;
            date_id = tracker.date_id;
            currentStateOfPoints = tracker.points;
            maxStateOfPoints = tracker.max_points;
            tvHeadline.setText(tracker.headline);
            setTvPoints(tracker.points);
            setProgressPoints(currentStateOfPoints, maxStateOfPoints);
        });

        //to handle click we need to create an OnClickListener
        View.OnClickListener onClickListener = v -> {
            //with id we will find what View was clicked and do something we want to
            switch (v.getId()) {
                case R.id.btn_plus:
                    if (currentStateOfPoints < maxStateOfPoints) {
                        currentStateOfPoints++;
                        setProgressPoints(currentStateOfPoints, maxStateOfPoints);
                        setTvPoints(currentStateOfPoints);
                    }
                    break;
                case R.id.btn_minus:
                    if (currentStateOfPoints > 0) {
                        currentStateOfPoints--;
                        setProgressPoints(currentStateOfPoints, maxStateOfPoints);
                        setTvPoints(currentStateOfPoints);
                    }
            }
            wasChanged = true;
        };

        btnPlus.setOnClickListener(onClickListener);
        btnMinus.setOnClickListener(onClickListener);


        Thread thread = new Thread(this::getAllLinkedPointsAndDates);
        thread.start();

        super.onViewCreated(view, savedInstanceState);
    }

    public void setSelectedItem(int position) {
        this.position = position;
    }

    private void setProgressPoints(int points, int max_points) {
        progressBar.setProgress((int) Math.round(points * 100.0 / max_points));
    }

    private void setTvPoints(int newPoints) {
        tvPoints.setText(newPoints + "/" + maxStateOfPoints);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (wasChanged) {
            updateDatabase();
        }
    }

    private void updateDatabase() {
        Thread thread = new Thread(() -> {
            long point_id = App.getInstance().getDatabase().trackerDatePointDao().getPointId(tracker_id, date_id);
            App.getInstance().getDatabase().pointDao().updatePoint(new Point(point_id, tracker_id, date_id, currentStateOfPoints));
        });
        thread.start();
    }

    private void getAllLinkedPointsAndDates() {
        Thread thread = new Thread(() -> {
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Point> linkedPoints = App.getInstance().getDatabase().pointDao().getPointsLinkedToTracker(tracker_id);
        List<Date> allDates = App.getInstance().getDatabase().dateDao().getAllDates();
        handler.post(() -> {
            displayMonitoringDates(allDates);
            displayDateWithLinkedPoints(linkedPoints, allDates);
        });
    }


    private void displayMonitoringDates(List<Date> allDates) {
        String firstDateMonth = monthToString(allDates.get(0).getMonth());
        String lastDateMonth = monthToString(allDates.get(allDates.size() - 1).getMonth());
        tvStatsDates.setText(allDates.get(0).getDay() + " "
                + firstDateMonth +
                " - " + allDates.get(allDates.size() - 1).getDay()
                + " " + lastDateMonth);
    }

    private String monthToString(int month) {
        switch (month) {
            case 1:
                return "january";
            case 2:
                return "february";
            case 3:
                return "march";
            case 4:
                return "april";
            case 5:
                return "may";
            case 6:
                return "june";
            case 7:
                return "july";
            case 8:
                return "august";
            case 9:
                return "september";
            case 10:
                return "october";
            case 11:
                return "november";
            case 12:
                return "december";
        }
        throw new NullPointerException("Invalid number of month");
    }

    private void displayDateWithLinkedPoints(List<Point> points, List<Date> dates) {
        for (int i = 0; i < points.size() - 1; i++) {
            tvStats.append(dates.get(i).getDay() + " " +
                    monthToString(dates.get(i).getMonth()) + ": " +
                    points.get(i).getPoints() + "/" +
                    maxStateOfPoints
            );
        }
        // за последний день не учитываем, потому что еще будут изменения
    }
}