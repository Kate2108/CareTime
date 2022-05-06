package com.example.testingappproject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.testingappproject.App;
import com.example.testingappproject.R;
import com.example.testingappproject.data.MainViewModel;
import com.example.testingappproject.data.PointDao;
import com.example.testingappproject.model.Date;
import com.example.testingappproject.model.Point;
import com.example.testingappproject.model.TrackerDatePoint;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
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

    private long tracker_id;
    private long date_id;

    private MainViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);


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

        Button btnPlus = view.findViewById(R.id.btn_plus);
        Button btnMinus = view.findViewById(R.id.btn_minus);

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
            switch (view.getId()) {
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
                        break;
                    }
            }
            wasChanged = true;
        };

        //attaching our onClickListener to buttons
        btnPlus.setOnClickListener(onClickListener);
        btnMinus.setOnClickListener(onClickListener);

        try {
            displayAllStats();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

    private void displayAllStats() throws InterruptedException {
        List<Point> linkedPoints = new ArrayList<>();
        List<Date> dates = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Date> allDates = App.getInstance().getDatabase().dateDao().getAllDates();
                PointDao pointsDao = App.getInstance().getDatabase().pointDao();
                for (int i = 0; i < allDates.size(); i++) {
                    Log.d("toradora", (pointsDao.getPoint(tracker_id, allDates.get(i).id) == null) + "");
                    linkedPoints.add(pointsDao.getPoint(tracker_id, allDates.get(i).id));
                }
                for (int i = 0; i < allDates.size(); i++) {
                    dates.add(allDates.get(i));
                }
            }
        });
        thread.start();
        thread.join();

        for (int i = 0; i < dates.size(); i++) {
            Log.d("toradora", dates.get(i).toString());
//            displayDateWithLinkedPoints( linkedPoints.get(i), dates.get(i));
        }
        List<Point> inkedPoints = App.getInstance().getDatabase().pointDao().getAllPoints();
        for (int i = 0; i < inkedPoints.size(); i++) {
            Log.d("toradora", inkedPoints.get(i).toString());
        }
        displayDatesStats(dates);

    }

    private void displayDatesStats(List<Date> allDates) {
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
                return getResources().getString(R.string.january);
            case 2:
                return getResources().getString(R.string.february);
            case 3:
                return getResources().getString(R.string.march);
            case 4:
                return getResources().getString(R.string.april);
            case 5:
                return getResources().getString(R.string.may);
            case 6:
                return getResources().getString(R.string.june);
            case 7:
                return getResources().getString(R.string.july);
            case 8:
                return getResources().getString(R.string.august);
            case 9:
                return getResources().getString(R.string.september);
            case 10:
                return getResources().getString(R.string.october);
            case 11:
                return getResources().getString(R.string.november);
            case 12:
                return getResources().getString(R.string.december);
        }
        throw new NullPointerException("month is null or incorrect");
    }

    private void displayDateWithLinkedPoints(Point point, Date date) {
        tvStats.append(date.getDay() + " " + monthToString(date.getMonth()) + "  "
                + point.getPoints() + "/" + maxStateOfPoints);
    }
}