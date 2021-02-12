package com.example.timer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RunningTimerActivity extends AppCompatActivity {

    TimerService timerService;
    ServiceConnection serviceConnection;
    ListView tasksList;
    ArrayList<Pair<String, Integer>> phases;
    Phase phase;
    boolean isRunning = true, isBounded;
    CountDownTimer countDownTimer;
    TextView countDownText;
    long currentSeconds;
    TasksAdapter tasksAdapter;
    int phaseNumber = 0;
    ImageButton pauseButton, nextButton, backButton;
    Intent serviceIntent;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_timer);

        phase = new Phase();

        TimerData timer = (TimerData) getIntent().getSerializableExtra("timer");
        phases = phase.createPhase(timer, this);
        tasksList = findViewById(R.id.tasksList);
        tasksAdapter = new TasksAdapter(this, phases);
        tasksList.setAdapter(tasksAdapter);
        mp = MediaPlayer.create(this, R.raw.whistle);

        pauseButton = findViewById(R.id.pauseButton);
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                phaseNumber++;
                nextStage();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                phaseNumber -= 2;
                nextStage();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    isRunning = false;
                    pauseButton.setImageResource(android.R.drawable.ic_media_play);
                    currentSeconds = Long.parseLong(countDownText.getText().toString()) * 1000 + 1000;
                    countDownTimer.cancel();
                } else {
                    isRunning = true;
                    pauseButton.setImageResource(android.R.drawable.ic_media_pause);
                    startTimer();
                }

            }
        });

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                timerService = ((TimerService.Binder) service).getService();
                isBounded = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBounded = false;
            }
        };

        Pair<String, Integer> currentPhaseSeconds = (Pair<String, Integer>) tasksList.getAdapter().getItem(0);
        currentSeconds = currentPhaseSeconds.second * 1000 + 1000;

        countDownText = findViewById(R.id.countdownText);
        serviceIntent = new Intent(this, TimerService.class);

        nextStage();
    }

    public void nextStage() {
        if (phaseNumber < 0) {
            phaseNumber = 0;
        }
        if (phaseNumber + 1 <= tasksAdapter.getCount()) {
            tasksList.setSelection(phaseNumber);
            Pair<String, Integer> currentPhaseSeconds = (Pair<String, Integer>) tasksList.getAdapter().getItem(phaseNumber);
            currentSeconds = currentPhaseSeconds.second * 1000 + 1000;
            if (isRunning) {
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                            }
                        },
                        1200);
                startTimer();
            } else {
                countDownText.setText(String.valueOf((int) (currentSeconds - 1)));
            }
        } else {
            countDownText.setText("0");
        }
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(currentSeconds, 1200) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long millisUntilFinished) {
                currentSeconds = millisUntilFinished;
                countDownText.setText(String.valueOf((int) (millisUntilFinished / 1000)));
            }

            @Override
            public void onFinish() {
                mp.start();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                            }
                        },
                        1200);
                phaseNumber++;
                nextStage();
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        int[] phasesList = new int[phases.size()];
        for (int i = 0; i < phases.size(); i++) {
            phasesList[i] = phases.get(i).second;
        }
        serviceIntent.putExtra("phase", phaseNumber);
        serviceIntent.putExtra("currentSeconds", currentSeconds);
        serviceIntent.putExtra("tasksCount", tasksAdapter.getCount());
        serviceIntent.putExtra("phases", phasesList);
        countDownTimer.cancel();
        startService(serviceIntent);
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isBounded) return;
        phaseNumber = timerService.getPhase();
        currentSeconds = timerService.getCurrentSeconds();
        unbindService(serviceConnection);
        stopService(serviceIntent);
        isBounded = false;
        tasksList.setSelection(phaseNumber);
        timerService.cancelTimer();
        startTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBounded) {
            unbindService(serviceConnection);
            stopService(serviceIntent);
            isBounded = false;
        }
    }

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.quit_title)
                .setMessage(R.string.quit_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startTimer();
                        dialog.dismiss();
                    }
                })
                .show();
    }
}