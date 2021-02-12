package com.example.timer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity implements
        RecyclerViewAdapter.OnItemDeleteListener,
        RecyclerViewAdapter.OnItemEditListener,
        RecyclerViewAdapter.OnItemPlayListener {

    final int REQUEST_CODE_TIMER_ADD = 1;
    final int REQUEST_CODE_TIMER_EDIT = 2;

    RecyclerView timersRecyclerView;

    FloatingActionButton addButton;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    DbHelper dbHelper;
    ArrayList<TimerData> timers;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        timers = dbHelper.getAllTimers();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        setPreferences();

        timersRecyclerView = findViewById(R.id.timersList);

        RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter(this, timers,
                this, this, this);

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
        } else if (resultCode == RESULT_OK && requestCode == 2){
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, SettingsActivity.class));
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onItemPlay(int position) {
        Intent intent = new Intent(this, RunningTimerActivity.class);
        intent.putExtra("timer", timers.get(position));
        startActivity(intent);
    }

    public void setPreferences() {
        if (sharedPreferences.getBoolean("appTheme", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        if (sharedPreferences.getBoolean("appLocale", false)) {
            LocaleHelper.setLocale(getBaseContext(),"en");
        }
    }
}