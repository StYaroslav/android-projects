package com.example.timer.services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.timer.ActiveTimerActivity;
import com.example.timer.models.Phase;
import com.example.timer.R;
import com.example.timer.models.TimerData;
import com.example.timer.constants.Constants;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TimerService extends Service {
    private final int NOTIFICATION_ID = 1;

    ExecutorService executorService;
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;

    IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public TimerService getServerInstance() {
            return TimerService.this;
        }
    }

    public void hideNotification() {
        stopSelf();
        notificationManager.cancel(NOTIFICATION_ID);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();

        Intent intent = new Intent(this, ActiveTimerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent previousIntent = new Intent(Constants.ACTION_PREV);
        Intent pauseIntent = new Intent(Constants.ACTION_PAUSE);
        Intent nextIntent = new Intent(Constants.ACTION_NEXT);

        PendingIntent pendingPreviousIntent = PendingIntent.getBroadcast(this, 0,
                previousIntent, 0);

        PendingIntent pendingPauseIntent = PendingIntent.getBroadcast(this, 0,
                pauseIntent, 0);

        PendingIntent pendingNextIntent = PendingIntent.getBroadcast(this, 0,
                nextIntent, 0);


        notificationBuilder = new NotificationCompat.Builder(this, "TimerChannel")
                .setContentTitle(getResources().getString(R.string.notification_title))
                .setContentText(getResources().getString(R.string.notification_text))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent)
                .addAction(0, "Previous", pendingPreviousIntent)
                .addAction(0, "Pause", pendingPauseIntent)
                .addAction(0, "Next", pendingNextIntent);
        Notification notification = notificationBuilder.build();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public TimerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (executorService != null) {
            executorService.shutdownNow();
        }

        executorService = Executors.newFixedThreadPool(1);

        TimerData timer = (TimerData) intent.getSerializableExtra("timer");
        ArrayList<Phase> phases = (ArrayList<Phase>) intent.getSerializableExtra("phases");
        int phase = intent.getIntExtra(Constants.ARG_PHASE, 0);
        int counterValue = intent.getIntExtra(Constants.ARG_COUNTER, 0);
        TimerRun timerRun = new TimerRun(timer, phases, phase, counterValue,
                getApplicationContext(), notificationBuilder, notificationManager);
        executorService.execute(timerRun);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        executorService.shutdownNow();
        stopSelf();
    }

    private void createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(
                "TimerChannel",
                "Timer notification",
                NotificationManager.IMPORTANCE_DEFAULT);

        notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);
    }
}

class TimerRun implements Runnable {
    TimerData timer;
    ArrayList<Phase> phases;
    Context context;
    MediaPlayer mediaPlayer;

    NotificationCompat.Builder builder;
    NotificationManager notificationManager;

    int currentPhase;
    int counterValue;
    int progress;
    int progressMax;

    public TimerRun(TimerData timer, ArrayList<Phase> phases,
                    int currentPhase, int counterValue, Context context,
                    NotificationCompat.Builder builder, NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
        this.builder = builder;
        this.timer = timer;
        this.phases = phases;
        this.context = context;
        this.currentPhase = currentPhase;
        this.counterValue = counterValue != 0 ? counterValue : phases.get(currentPhase).getTime();
        this.progressMax = getMaxProgress();
        this.progress = getCurrentProgress(currentPhase);
    }

    private int getCurrentProgress(int currentPhase) {
        int progress = progressMax;
        for (int phaseId = phases.size() - 1; phaseId >= currentPhase; phaseId--) {
            progress -= phases.get(phaseId).getTime();
        }
        return progress;
    }

    private int getMaxProgress() {
        int maxProgress = 0;
        for (Phase phase : phases) {
            maxProgress += phase.getTime();
        }
        return maxProgress;
    }

    @Override
    public void run() {
        try {
            startCycle();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startCycle() throws InterruptedException {
        Intent intent = new Intent(Constants.BROADCAST_ACTION);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        while (currentPhase <= phases.size()) {
            TimeUnit.SECONDS.sleep(1);
            int command = Step();
            handleCommand(command, intent);
            intent.putExtra(Constants.PARAM_COMMAND, command);
            context.sendBroadcast(intent);
            progress += 1;
            builder.setProgress(progressMax, progress, false);
            notificationManager.notify(1, builder.build());
        }
    }

    private void handleCommand(int command, Intent intent) {
        switch (command) {
            case Constants.COMMAND_TICK: {
                if (counterValue >= 0 && counterValue <= 3) {
                    mediaPlayer = MediaPlayer.create(context, R.raw.tick);
                    mediaPlayer.start();
                }
                break;
            }
            case Constants.COMMAND_CHANGE: {
                playSound();
                intent.putExtra(Constants.ARG_PHASE, currentPhase);
                intent.putExtra(Constants.ARG_COUNTER, counterValue);
                break;
            }
            case Constants.COMMAND_STOP: {
                builder.setProgress(0, 0, false)
                        .setContentText("Тренировка завершена");
                notificationManager.notify(1, builder.build());
                break;
            }
        }
    }

    private void playSound() {
        switch (phases.get(currentPhase).getName()) {
            case "Work": {
                mediaPlayer = MediaPlayer.create(context, R.raw.whistling);
                mediaPlayer.start();
                break;
            }
            case "Cooldown":
            case "Rest": {
                mediaPlayer = MediaPlayer.create(context, R.raw.gong);
                 mediaPlayer.start();
                break;
            }
        }
    }

    private int Step() {
        if (counterValue > 1) {
            counterValue--;
            return Constants.COMMAND_TICK;
        } else {
            currentPhase++;
            if (currentPhase == phases.size()) {
                mediaPlayer = MediaPlayer.create(context, R.raw.finish);
                 mediaPlayer.start();
                return Constants.COMMAND_STOP;
            } else if (currentPhase < phases.size()) {
                counterValue = phases.get(currentPhase).getTime();
                return Constants.COMMAND_CHANGE;
            } else return 0;
        }
    }
}