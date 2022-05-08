package com.example.testingappproject.settings;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;

import com.example.testingappproject.R;

public class CustomPreferenceCategory extends PreferenceCategory {
    public CustomPreferenceCategory(Context context) {
        super(context);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs,
                                    int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView txt = (TextView) holder.findViewById(android.R.id.title);
        txt.setTextColor(Color.WHITE);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.redhatdisplay_bold);
        txt.setTypeface(typeface);
    }
}
