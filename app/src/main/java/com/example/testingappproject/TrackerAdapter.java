package com.example.testingappproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrackerAdapter extends RecyclerView.Adapter<TrackerAdapter.TrackerViewHolder>{
    private final ArrayList<TrackerItem> trackers;
    private final OnTrackerItemClickListener onClickListener;

    interface OnTrackerItemClickListener{
        void onTrackerItemClick(TrackerItem trackerItem, int position);
    }

    public TrackerAdapter(ArrayList<TrackerItem> trackers, OnTrackerItemClickListener onClickListener) {
        this.trackers = trackers;
        this.onClickListener = onClickListener;
    }


    @NonNull
    @Override
    public TrackerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item, parent, false);
        return new TrackerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackerViewHolder holder, int position) {
        holder.headline.setText(trackers.get(position).getHeadline());
        System.out.println(trackers.get(position).getHeadline());
        holder.pb.setProgress(trackers.get(position).getProgress());
        holder.itemImg.setImageResource(trackers.get(position).getImgResource());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // вызываем метод слушателя, передавая ему данные
                onClickListener.onTrackerItemClick(trackers.get(position), position );;
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackers.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class TrackerViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView headline;
        ImageView itemImg;
        ProgressBar pb;
        TrackerViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            headline = itemView.findViewById(R.id.item_headline);
            itemImg = itemView.findViewById(R.id.item_img);
            pb = itemView.findViewById(R.id.item_progressBar);
        }
    }
}
