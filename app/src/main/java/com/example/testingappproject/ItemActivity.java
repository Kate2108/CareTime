package com.example.testingappproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_item);

        TextView tvHeadline = findViewById(R.id.tv_headline);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        TrackerItem trackerItem;

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            trackerItem = (TrackerItem) arguments.getSerializable(TrackerItem.class.getSimpleName());
            tvHeadline.setText(trackerItem.getHeadline());
            progressBar.setProgress(trackerItem.getProgress());
        }

    }

    @Override
    public void onClick(View view) {
    }
}