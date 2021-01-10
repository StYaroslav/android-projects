package com.example.battleship.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.battleship.R;
import com.example.battleship.adapters.StatisticAdapter;
import com.example.battleship.models.Statistic;
import com.example.battleship.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class StatisticFragment extends DialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        List<Statistic> statistics=  new ArrayList<>();
        DatabaseReference statisticsDatabaseReference = FirebaseDatabase.getInstance().getReference("statistics");

        statisticsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null ){
                    for(DataSnapshot sampleSnapshot:snapshot.getChildren()){
                        statistics.add(sampleSnapshot.getValue(Statistic.class));
                        RecyclerView statisticsRecycler = view.findViewById(R.id.statisticsRecycler);
                        StatisticAdapter statisticsAdapter = new StatisticAdapter(statistics, getContext());
                        statisticsRecycler.setAdapter(statisticsAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}