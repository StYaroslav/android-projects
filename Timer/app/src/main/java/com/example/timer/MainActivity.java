package com.example.timer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.timer.constants.Constants;
import com.example.timer.helpers.LocaleHelper;
import com.example.timer.models.TimerData;
import com.example.timer.repositories.DbPhaseRepository;
import com.example.timer.repositories.DbTimerDataRepository;
import com.example.timer.repositories.PhaseRepository;
import com.example.timer.repositories.TimerDataRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        RecyclerViewAdapter.OnItemDeleteListener, RecyclerViewAdapter.OnTimerEditListener {

    RecyclerView timersRecyclerView;
    RecyclerViewAdapter viewAdapter;

    FloatingActionButton addButton;

    TimerDataRepository timerDataRepository;
    PhaseRepository phaseRepository;

    ArrayList<TimerData> timers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerDataRepository = new DbTimerDataRepository(getApplicationContext());
        phaseRepository = new DbPhaseRepository(getApplicationContext());

        timers = timerDataRepository.get();

        timersRecyclerView = findViewById(R.id.timersList);

        viewAdapter = new RecyclerViewAdapter(MainActivity.this, timers, this, this);

        timersRecyclerView.setAdapter(viewAdapter);
        timersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                intent.putExtra("timer", new TimerData());
                intent.putExtra("type", "add");
                startActivityForResult(intent, Constants.ADD_DATA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == Constants.ADD_DATA) {
            if (resultCode == RESULT_OK && data != null) {
                timers.add((TimerData) data.getSerializableExtra("timer"));
                timersRecyclerView.getAdapter().notifyDataSetChanged();
            }
        } else if (requestCode == Constants.EDIT_DATA && resultCode == RESULT_OK && data != null) {
            TimerData timer = (TimerData) data.getSerializableExtra("timer");
            if (timer != null) {
                for (int index = 0; index < timers.size(); index++) {
                    if (timers.get(index).getId() == (timer.getId())) {
                        timers.set(index, timer);
                        timersRecyclerView.getAdapter().notifyDataSetChanged();
                        break;
                    }
                }
            }

        } else if(requestCode == Constants.SETTINGS && resultCode == RESULT_OK){
            timerDataRepository.clear();
            timers.clear();
            viewAdapter.setTimer(timers);
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivityForResult(intent, Constants.SETTINGS);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemDelete(int position) {
        timerDataRepository.delete(timers.get(position).getId());
        timers.remove(position);
        timersRecyclerView.getRecycledViewPool().clear();
    }

    @Override
    public void onTimerEditClick(int position) {
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra("timer", timers.get(position));
        intent.putExtra("type", "edit");
        startActivityForResult(intent, Constants.EDIT_DATA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocaleHelper.setLocale(MainActivity.this, LocaleHelper.getLanguage(MainActivity.this));
        timersRecyclerView.getAdapter().notifyDataSetChanged();
    }
}