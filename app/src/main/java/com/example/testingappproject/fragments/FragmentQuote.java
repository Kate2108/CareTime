package com.example.testingappproject.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testingappproject.R;

public class FragmentQuote extends Fragment {
    private SharedPreferences preferences;
    private TextView tvQuote;
    private TextView tvQuoteAuthor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quote, container, false);
        tvQuote = view.findViewById(R.id.tv_quote);
        tvQuoteAuthor = view.findViewById(R.id.tv_quote_author);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvQuote.setText(preferences.getString("quote", "The dream was always running ahead of me. To catch up, to live for a moment" +
                " in unison with it, that always was the miracle."));
        tvQuoteAuthor.setText(preferences.getString("quote-author", "Anais Nin"));
    }
}