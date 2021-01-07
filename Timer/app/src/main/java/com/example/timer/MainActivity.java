package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        RecyclerViewAdapter.OnItemDeleteListener {

    RecyclerView timersRecyclerView;

    FloatingActionButton addButton;

    DbHelper dbHelper;
    SQLiteDatabase db;
    ArrayList<TimerData> timers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        //TimerData timer = new TimerData(1, "Анжумания", 1, 1, 1, 1, 1, 1, 1, 0);

        //dbHelper.addTimer(timer);

        timers = dbHelper.getAllTimers();

        timersRecyclerView = findViewById(R.id.timersList);

        RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter(this, timers, this);

        timersRecyclerView.setAdapter(viewAdapter);
        timersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemDelete(int position) {
        dbHelper.deleteTimer(String.valueOf(timers.get(position).getId()));
        timers.remove(position);
        timersRecyclerView.getRecycledViewPool().clear();
    }
}