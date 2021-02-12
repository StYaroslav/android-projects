package com.example.timer;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class TimerService extends Service {

    private Binder binder;
    private CountDownTimer countDownTimer;
    private int phase, tasksCount;
    private long currentSeconds;
    private int[] phases;

    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentSeconds = intent.getLongExtra("currentSeconds", 0);
        phase = intent.getIntExtra("phase", 0);
        tasksCount = intent.getIntExtra("tasksCount", 0);
        phases = intent.getIntArrayExtra("phases");
        startTimer();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new Binder();
    }

    public int getPhase() {
        return this.phase;
    }

    public long getCurrentSeconds() {
        return this.currentSeconds;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void nextStage() {
        if (phase + 1 <= tasksCount) {
            int currentPhaseSeconds = phases[phase];
            currentSeconds = currentPhaseSeconds * 1000 + 1000;
            countDownTimer.cancel();
            startTimer();
        } else {
            phase++;
            countDownTimer.cancel();
        }
    }

    public void cancelTimer() {
        countDownTimer.cancel();
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(currentSeconds, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long millisUntilFinished) {
                currentSeconds = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                            }
                        },
                        1000);
                phase++;
                nextStage();
            }
        }.start();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class Binder extends android.os.Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }
}
