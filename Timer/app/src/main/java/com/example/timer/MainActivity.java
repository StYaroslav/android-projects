package com.example.timer;

import androidx.annotation.Nullable;
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
import java.util.Timer;

public class MainActivity extends AppCompatActivity implements
        RecyclerViewAdapter.OnItemDeleteListener, RecyclerViewAdapter.OnItemEditListener {

    final int REQUEST_CODE_TIMER_ADD = 1;
    final int REQUEST_CODE_TIMER_EDIT = 2;

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

        RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter(this, timers,
                this, this);

        timersRecyclerView.setAdapter(viewAdapter);
        timersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                intent.putExtra("timer", new TimerData());
                intent.putExtra("type", "add");
                startActivityForResult(intent, REQUEST_CODE_TIMER_ADD);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            timers.add((TimerData) data.getSerializableExtra("timer"));
            timersRecyclerView.getAdapter().notifyDataSetChanged();
        } else {
            TimerData timer = (TimerData) data.getSerializableExtra("timer");
            for (int i = 0; i < timers.size(); i++) {
                if (timers.get(i).getId() == timer.getId()) {
                    timers.set(i, timer);
                    timersRecyclerView.getAdapter().notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    public void onItemDelete(int position) {
        dbHelper.deleteTimer(String.valueOf(timers.get(position).getId()));
        timers.remove(position);
        timersRecyclerView.getRecycledViewPool().clear();
    }

    @Override
    public void onItemEdit(int position) {
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra("type", "edit");
        intent.putExtra("timer", timers.get(position));
        startActivityForResult(intent, REQUEST_CODE_TIMER_EDIT);
    }
}