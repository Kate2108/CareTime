package com.example.testingappproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingappproject.model.TrackerDatePoint;

import java.util.List;

public class TrackerAdapter extends RecyclerView.Adapter<TrackerAdapter.ViewHolder>{
    public interface OnTrackerClickListener{
        void onTrackerClick(int position);
    }

    private final OnTrackerClickListener onClickListener;

    private List<TrackerDatePoint> trackers;
    private final LayoutInflater inflater;

    public TrackerAdapter(OnTrackerClickListener onClickListener, Context context, List<TrackerDatePoint> trackers) {
        this.onClickListener = onClickListener;
        this.trackers = trackers;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public TrackerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycleview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackerAdapter.ViewHolder holder, int position) {
        TrackerDatePoint tracker = trackers.get(position);
        holder.headline.setText(tracker.headline);
        holder.pb.setProgress((int) Math.round(tracker.points*100.0/tracker.max_points));
        holder.itemImg.setImageResource(tracker.img_res);

        holder.itemImg.setOnClickListener(view -> {
            onClickListener.onTrackerClick(trackers.indexOf(tracker));
        });
    }

    @Override
    public int getItemCount() {
        return trackers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final CardView cv;
        final TextView headline;
        final ImageView itemImg;
        final ProgressBar pb;
        ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            headline = itemView.findViewById(R.id.item_headline);
            itemImg = itemView.findViewById(R.id.item_img);
            pb = itemView.findViewById(R.id.item_progressBar);
        }
    }


}
