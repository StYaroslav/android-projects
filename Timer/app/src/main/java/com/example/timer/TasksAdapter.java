package com.example.timer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import android.util.Pair;

import androidx.preference.PreferenceManager;

import java.util.ArrayList;

public class TasksAdapter extends ArrayAdapter<Pair<String, Integer>> {

    ArrayList<Pair<String, Integer>> tasks;

    public TasksAdapter(Context context, ArrayList<Pair<String, Integer>> tasksList) {
        super(context, R.layout.list_item, tasksList);
        this.tasks = tasksList;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pair<String, Integer> phase = tasks.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, null);
        }
        ((TextView) convertView.findViewById(R.id.item))
                .setText(phase.first + ": " + phase.second);

        SharedPreferences editor = PreferenceManager.getDefaultSharedPreferences(getContext());

        try {
            int fontSize = editor.getInt("font", 0);
            ((TextView) convertView.findViewById(R.id.item)).setTextSize(14 + fontSize);
        } catch (Exception ignored) { }

        return convertView;
    }
}
