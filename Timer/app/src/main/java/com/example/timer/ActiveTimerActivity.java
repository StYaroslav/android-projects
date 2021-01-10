package com.example.timer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;

import com.example.timer.constants.*;
import com.example.timer.factories.ActiveTimerViewModelFactory;
import com.example.timer.fragments.Tasks.OnItemClicked;
import com.example.timer.helpers.NotificationReceiver;
import com.example.timer.helpers.TimerReceiver;
import com.example.timer.models.Phase;
import com.example.timer.models.TimerData;
import com.example.timer.repositories.DbPhaseRepository;
import com.example.timer.repositories.PhaseRepository;
import com.example.timer.services.TimerService;
import com.example.timer.viewModels.ActiveTimerViewModel;

public class ActiveTimerActivity extends AppCompatActivity implements OnItemClicked {

    Intent serviceIntent;
    TimerService timerService;

    ActiveTimerViewModel activeViewModel;
    TimerReceiver broadcastReceiver;

    PhaseRepository sqLitePhaseRepository;

    ListView tasksListView;
    TextView stageNameText;
    TextView stageText;
    ImageButton buttonPrev;
    ImageButton buttonNext;
    ImageButton buttonBack;
    ImageButton buttonPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        sqLitePhaseRepository = new DbPhaseRepository(getApplicationContext());
        TimerData timer = (TimerData) intent.getSerializableExtra("timer");
        ArrayList<Phase> phaseList = sqLitePhaseRepository.get(timer.getId());
        activeViewModel = new ViewModelProvider(this, new ActiveTimerViewModelFactory(timer, phaseList)).get(ActiveTimerViewModel.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_timer);

        final Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        tasksListView = findViewById(R.id.list_view);

        stageNameText = findViewById(R.id.stageName);
        stageText = findViewById(R.id.stageText);

        buttonPrev = findViewById(R.id.prevButton);
        buttonNext = findViewById(R.id.nextButton);
        buttonBack = findViewById(R.id.backButton);
        buttonPause = findViewById(R.id.pauseButton);

        buttonPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activeViewModel.isPaused.setValue(!activeViewModel.isPaused.getValue());
                buttonPause.setBackgroundColor(80000000);
            }
        });

        buttonBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        buttonPrev.setOnClickListener(navigationButtonOnClick);
        buttonNext.setOnClickListener(navigationButtonOnClick);
        getSupportActionBar().hide();

        broadcastReceiver = new TimerReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int command = intent.getIntExtra(Constants.PARAM_COMMAND, 0);
                handleCommand(command, intent);
            }
        };

        NotificationReceiver notificationReceiver = new NotificationReceiver(){
            @Override
            public void onReceive(Context context, @NonNull Intent intent) {
                handleAction(intent.getAction());
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_PREV);
        filter.addAction(Constants.ACTION_PAUSE);
        filter.addAction(Constants.ACTION_NEXT);
        registerReceiver(notificationReceiver, filter);

        IntentFilter intentFilter  = new IntentFilter(Constants.BROADCAST_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

        serviceIntent = new Intent(getBaseContext(), TimerService.class);

        activeViewModel.isPaused.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPaused) {
                if (activeViewModel.isRunning.getValue()) {
                    if (isPaused) {
                        stopService(serviceIntent);
                        buttonPause.setImageResource(R.drawable.play);
                    } else {
                        buttonPause.setImageResource(R.drawable.pause);
                        restartService();
                    }
                }
            }
        });

        activeViewModel.isRunning.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isRunning) {
                if(!isRunning){
                    timerService.hideNotification();
                    stopService(new Intent(serviceIntent));
                    Toast.makeText(ActiveTimerActivity.this,
                            R.string.training_end, Toast.LENGTH_LONG).show();
                }
            }
        });

        activeViewModel.currentPhase.observe(this, new Observer<Integer>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(Integer phase) {
                ListView lv = findViewById(R.id.list_view);
                lv.setSelection(phase);
                stageText.setText((phase + 1) + "/" + (activeViewModel.phaseList.size()));
                stageNameText.setText(activeViewModel.phaseList.get(phase).getName());
                changeColor(phase);
            }
        });
    }

    OnClickListener navigationButtonOnClick = new OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            if (activeViewModel.isRunning.getValue()) {
                switch (v.getId()) {
                    case R.id.prevButton: {
                        activeViewModel.changePhase(activeViewModel.currentPhase.getValue() - 1);
                        break;
                    }
                    case R.id.nextButton: {
                        activeViewModel.changePhase(activeViewModel.currentPhase.getValue() + 1);
                        break;
                    }

                }
                Intent serviceIntent = new Intent(getBaseContext(), TimerService.class);
              //  MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.select);
                //mediaPlayer.start();

                activeViewModel.isPaused.setValue(false);

                serviceIntent.putExtra(Constants.ARG_TIMER, activeViewModel.getTimer());
                serviceIntent.putExtra(Constants.ARG_PHASES, activeViewModel.getPhases());
                serviceIntent.putExtra(Constants.ARG_PHASE, activeViewModel.currentPhase.getValue());

                startService(serviceIntent);
            }
        }
    };

    private void restartService(){
        final Intent serviceIntent = new Intent(getBaseContext(), TimerService.class);
        serviceIntent.putExtra(Constants.ARG_TIMER, activeViewModel.getTimer());
        serviceIntent.putExtra(Constants.ARG_PHASES, activeViewModel.getPhases());
        serviceIntent.putExtra(Constants.ARG_PHASE, activeViewModel.currentPhase.getValue());
        serviceIntent.putExtra(Constants.ARG_COUNTER, activeViewModel.counterValue.getValue());
        startService(serviceIntent);
        bindService(serviceIntent, mConnection, BIND_ADJUST_WITH_ACTIVITY);
    }

    private void handleAction(String action) {
        switch (action) {
            case Constants.ACTION_PREV: {
                buttonPrev.performClick();
                break;
            }
            case Constants.ACTION_PAUSE: {
                buttonPause.performClick();
                break;
            }
            case Constants.ACTION_NEXT: {
                buttonNext.performClick();
                break;
            }
        }
    }

    private void handleCommand(int command, Intent intent){
        switch (command){
            case Constants.COMMAND_TICK:{
                activeViewModel.counterValue.postValue(activeViewModel.counterValue.getValue() - 1);
                break;
            }
            case Constants.COMMAND_STOP:{
                activeViewModel.isRunning.postValue(false);
                break;
            }
            case Constants.COMMAND_CHANGE:{
                activeViewModel.currentPhase.
                        postValue(intent.getIntExtra(Constants.ARG_PHASE, 0));

                activeViewModel.counterValue.
                        postValue(intent.getIntExtra(Constants.ARG_COUNTER, 0));
                break;
            }
        }
    }
    @Override
    public void onBackPressed() {
        if(activeViewModel.isRunning.getValue()){
            openQuitDialog();
        }
        else{
            super.onBackPressed();
        }

    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                ActiveTimerActivity.this);
        quitDialog.setTitle(R.string.quit_title);

        quitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        quitDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        quitDialog.show();
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) { }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TimerService.LocalBinder mLocalBinder = (TimerService.LocalBinder)service;
            timerService = mLocalBinder.getServerInstance();
        }
    };

    private void changeColor(Integer phase) {
        final View background = findViewById(android.R.id.content);
        final Window window = this.getWindow();

        if (phase < activeViewModel.phaseList.size()) {
            Phase currentPhase = activeViewModel.phaseList.get(phase);
            switch (currentPhase.getName()) {
                case "Preparing": {
                    background.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
                    window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.darkGreen, null));
                    break;
                }
                case "Work": {
                    background.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
                    window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.darkRed, null));
                    break;
                }
                case "Rest": {
                    background.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.blue, null));
                    window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.darkBlue, null));
                    break;
                }
                default: {
                    background.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.purple, null));
                    window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.darkPurple, null));
                    break;
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        Intent serviceIntent = new Intent(getBaseContext(), TimerService.class);
        timerService.hideNotification();
        stopService(serviceIntent);
        super.onDestroy();
    }

    @Override
    public void onItemClicked(int position) {
        if (position < activeViewModel.phaseList.size()) {
            Intent serviceIntent = new Intent(getBaseContext(), TimerService.class);
            //MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.select);
            //mediaPlayer.start();

            activeViewModel.isPaused.setValue(false);

            serviceIntent.putExtra(Constants.ARG_TIMER, activeViewModel.getTimer());
            serviceIntent.putExtra(Constants.ARG_PHASES, activeViewModel.getPhases());
            serviceIntent.putExtra(Constants.ARG_PHASE, position);

            startService(serviceIntent);
        } else {
            activeViewModel.isRunning.setValue(false);
        }
    }
}