package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

import java.util.ArrayList;

public class TimerActivity extends AppCompatActivity implements ColorPickerDialogListener {

    TextView title, preparing, work, rest, cycles, sets, restBetweenSets, calmDown;
    CardView currentColor;
    Button color, addTimer;
    TimerViewModel timerViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        final String type = getIntent().getStringExtra("type");
        final TimerData timer = (TimerData) getIntent().getSerializableExtra("timer");
        timerViewModel = new ViewModelProvider(this, new TimerViewModelFactory(timer)).get(TimerViewModel.class);
        title = findViewById(R.id.titleValue);
        preparing = findViewById(R.id.preparingValue);
        work = findViewById(R.id.workValue);
        rest = findViewById(R.id.restValue);
        cycles = findViewById(R.id.cyclesValue);
        sets = findViewById(R.id.setsValue);
        restBetweenSets = findViewById(R.id.restBetweenSetsValue);
        calmDown = findViewById(R.id.calmDownValue);
        color = findViewById(R.id.changeColor);
        currentColor = findViewById(R.id.currentColor);


        if (type.equals("edit")) {
            title.setText(timerViewModel.title.getValue());
            preparing.setText(String.valueOf(timerViewModel.preparingTime.getValue()));
            work.setText(String.valueOf(timerViewModel.workingTime.getValue()));
            rest.setText(String.valueOf(timerViewModel.restTime.getValue()));
            cycles.setText(String.valueOf(timerViewModel.cyclesAmount.getValue()));
            sets.setText(String.valueOf(timerViewModel.setsAmount.getValue()));
            restBetweenSets.setText(String.valueOf(timerViewModel.restBetweenSets.getValue()));
            calmDown.setText(String.valueOf(timerViewModel.calmDown.getValue()));
            currentColor.setCardBackgroundColor(timerViewModel.color.getValue());
        }


        addTimer = findViewById(R.id.confirmButton);
        addTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (type.equals("add")) {
                    TimerData timerData = new TimerData(1, timerViewModel.title.getValue(), timerViewModel.preparingTime.getValue(), timerViewModel.workingTime.getValue(),
                            timerViewModel.restTime.getValue(), timerViewModel.cyclesAmount.getValue(), timerViewModel.setsAmount.getValue(), timerViewModel.restBetweenSets.getValue(), timerViewModel.calmDown.getValue(),
                            timerViewModel.color.getValue());
                    timerViewModel.addTimer(timerData, getApplicationContext());
                    intent.putExtra("timer", timerData);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                intent.putExtra("timer", timerViewModel.updateTimer(getApplicationContext()));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        final ArrayList<ImageButton> buttonList = new ArrayList<>();
        buttonList.add((ImageButton) findViewById(R.id.preparingMinus));
        buttonList.add((ImageButton) findViewById(R.id.preparingPlus));
        buttonList.add((ImageButton) findViewById(R.id.workMinus));
        buttonList.add((ImageButton) findViewById(R.id.workPlus));
        buttonList.add((ImageButton) findViewById(R.id.restMinus));
        buttonList.add((ImageButton) findViewById(R.id.restPlus));
        buttonList.add((ImageButton) findViewById(R.id.cyclesMinus));
        buttonList.add((ImageButton) findViewById(R.id.cyclesPlus));
        buttonList.add((ImageButton) findViewById(R.id.setsMinus));
        buttonList.add((ImageButton) findViewById(R.id.setsPlus));
        buttonList.add((ImageButton) findViewById(R.id.betweenSetsMinus));
        buttonList.add((ImageButton) findViewById(R.id.betweenSetsPlus));
        buttonList.add((ImageButton) findViewById(R.id.calmDownMinus));
        buttonList.add((ImageButton) findViewById(R.id.calmDownPlus));

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createColorPickerDialog(timerViewModel.id.getValue());
            }
        });

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeValue(view);
            }
        };

        for (ImageButton button :
                buttonList) {
            button.setOnClickListener(buttonClickListener);
        }

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                timerViewModel.title.setValue(title.getText().toString());
            }
        });

        timerViewModel.preparingTime.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                preparing.setText(String.valueOf(integer));
            }
        });

        timerViewModel.workingTime.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                work.setText(String.valueOf(integer));
            }
        });

        timerViewModel.restTime.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                rest.setText(String.valueOf(integer));
            }
        });

        timerViewModel.cyclesAmount.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                cycles.setText(String.valueOf(integer));
            }
        });

        timerViewModel.setsAmount.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                sets.setText(String.valueOf(integer));
            }
        });

        timerViewModel.restBetweenSets.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                restBetweenSets.setText(String.valueOf(integer));
            }
        });

        timerViewModel.calmDown.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                calmDown.setText(String.valueOf(integer));
            }
        });

        timerViewModel.color.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                currentColor.setCardBackgroundColor(integer);
            }

        });
    }

    private void createColorPickerDialog(int id) {
        ColorPickerDialog.newBuilder()
                .setColor(Color.RED)
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .setColorShape(ColorShape.SQUARE)
                .setDialogId(id)
                .show(this);
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        timerViewModel.color.setValue(color);
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

    @SuppressLint("NonConstantResourceId")
    private void changeValue(View view) {
        switch (view.getId()) {
            case R.id.preparingMinus:
                if (timerViewModel.preparingTime.getValue() - 1 >= 0)
                    timerViewModel.preparingTime.setValue(timerViewModel.preparingTime.getValue() - 1);
                break;
            case R.id.workMinus:
                if (timerViewModel.workingTime.getValue() - 1 >= 0)
                    timerViewModel.workingTime.setValue(timerViewModel.workingTime.getValue() - 1);
                break;
            case R.id.restMinus:
                if (timerViewModel.restTime.getValue() - 1 >= 0)
                    timerViewModel.restTime.setValue(timerViewModel.restTime.getValue() - 1);
                break;
            case R.id.cyclesMinus:
                if (timerViewModel.cyclesAmount.getValue() - 1 >= 1)
                    timerViewModel.cyclesAmount.setValue(timerViewModel.cyclesAmount.getValue() - 1);
                break;
            case R.id.setsMinus:
                if (timerViewModel.setsAmount.getValue() - 1 >= 1)
                    timerViewModel.setsAmount.setValue(timerViewModel.setsAmount.getValue() - 1);
                break;
            case R.id.betweenSetsMinus:
                if (timerViewModel.restBetweenSets.getValue() - 1 >= 0)
                    timerViewModel.restBetweenSets.setValue(timerViewModel.restBetweenSets.getValue() - 1);
                break;
            case R.id.calmDownMinus:
                if (timerViewModel.calmDown.getValue() - 1 >= 0)
                    timerViewModel.calmDown.setValue(timerViewModel.calmDown.getValue() - 1);
                break;
            case R.id.preparingPlus:
                timerViewModel.preparingTime.setValue(timerViewModel.preparingTime.getValue() + 1);
                break;
            case R.id.workPlus:
                timerViewModel.workingTime.setValue(timerViewModel.workingTime.getValue() + 1);
                break;
            case R.id.restPlus:
                timerViewModel.restTime.setValue(timerViewModel.restTime.getValue() + 1);
                break;
            case R.id.cyclesPlus:
                timerViewModel.cyclesAmount.setValue(timerViewModel.cyclesAmount.getValue() + 1);
                break;
            case R.id.setsPlus:
                timerViewModel.setsAmount.setValue(timerViewModel.setsAmount.getValue() + 1);
                break;
            case R.id.betweenSetsPlus:
                timerViewModel.restBetweenSets.setValue(timerViewModel.restBetweenSets.getValue() + 1);
                break;
            case R.id.calmDownPlus:
                timerViewModel.calmDown.setValue(timerViewModel.calmDown.getValue() + 1);
                break;
        }
    }
}