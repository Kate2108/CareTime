package com.example.testingappproject.viewpager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testingappproject.MainActivity;
import com.example.testingappproject.R;


public class UniversalFragment extends Fragment {
    public static final String VIEWPAGER_HEADLINES = "view_pager_headlines";
    public static final String VIEWPAGER_DESCRIPTIONS = "view_pager_descriptions";
    public final static String VIEWPAGER_IMAGE = "viewpager image";
    private String curHeadline;
    private String curDescription;
    private int  curImageId;
    private TextView tvHeadline;
    private ImageView ivPicture;
    private TextView tvDescription;
    private Button btnSkip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_universal, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            curHeadline = arguments.getString(VIEWPAGER_HEADLINES);
            curDescription = arguments.getString(VIEWPAGER_DESCRIPTIONS);
            curImageId = arguments.getInt(VIEWPAGER_IMAGE);
        }

        tvHeadline = view.findViewById(R.id.uf_headline);
        ivPicture = view.findViewById(R.id.uf_image_view);
        tvDescription = view.findViewById(R.id.uf_description);
        btnSkip = view.findViewById(R.id.btn_skip);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        display();

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mPager.setVisibility(View.GONE);
            }
        });
    }

    private void display() {
        tvHeadline.setText(curHeadline);
        ivPicture.setImageResource(curImageId);
        tvDescription.setText(curDescription);
    }

    private void skip(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }
}