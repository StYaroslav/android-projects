package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TimerActivity extends AppCompatActivity {

    TextView title, preparing, work, rest, cycles, sets, restBetweenSets, calmDown, colorView;
    EditTimerViewModel editViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        final Intent intent = getIntent();

        TimerData timer = (TimerData) intent.getSerializableExtra("timer");
        editViewModel = new ViewModelProvider(this, new EditViewModelFactory(timer))
                .get(EditTimerViewModel.class);

        final String type = intent.getStringExtra("type");

        title = findViewById(R.id.titleValue);
        preparing = findViewById(R.id.preparingValue);
        work = findViewById(R.id.workValue);
        rest = findViewById(R.id.restValue);
        cycles = findViewById(R.id.cyclesValue);
        sets = findViewById(R.id.setsValue);
        restBetweenSets = findViewById(R.id.restBetweenSetsValue);
        calmDown = findViewById(R.id.calmDownValue);
        // colorView = findViewById(R.id.workValue);

        final ArrayList<ImageButton> buttons = new ArrayList<>();
        buttons.add((ImageButton) findViewById(R.id.preparingMinus));
        buttons.add((ImageButton) findViewById(R.id.preparingPlus));
        buttons.add((ImageButton) findViewById(R.id.workMinus));
        buttons.add((ImageButton) findViewById(R.id.workPlus));
        buttons.add((ImageButton) findViewById(R.id.restMinus));
        buttons.add((ImageButton) findViewById(R.id.restPlus));
        buttons.add((ImageButton) findViewById(R.id.cyclesMinus));
        buttons.add((ImageButton) findViewById(R.id.cyclesPlus));
        buttons.add((ImageButton) findViewById(R.id.setsMinus));
        buttons.add((ImageButton) findViewById(R.id.setsPlus));
        buttons.add((ImageButton) findViewById(R.id.betweenSetsMinus));
        buttons.add((ImageButton) findViewById(R.id.betweenSetsPlus));
        buttons.add((ImageButton) findViewById(R.id.calmDownMinus));
        buttons.add((ImageButton) findViewById(R.id.calmDownPlus));

        title.setText(editViewModel.title.getValue());
        preparing.setText(editViewModel.preparing.getValue() + "");
        work.setText(editViewModel.work.getValue() + "");
        rest.setText(editViewModel.rest.getValue() + "");
        cycles.setText(editViewModel.cycles.getValue() + "");
        sets.setText(editViewModel.sets.getValue() + "");
        restBetweenSets.setText(editViewModel.restBetweenSets.getValue() + "");
        calmDown.setText(editViewModel.calmDown.getValue() + "");

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editViewModel.title.setValue(title.getText().toString());
            }
        });

        editViewModel.preparing.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                preparing.setText(String.valueOf(integer));
            }
        });

        editViewModel.work.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                work.setText(String.valueOf(integer));
            }
        });

        editViewModel.rest.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                rest.setText(String.valueOf(integer));
            }
        });

        editViewModel.cycles.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                cycles.setText(String.valueOf(integer));
            }
        });

        editViewModel.sets.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                sets.setText(String.valueOf(integer));
            }
        });

        editViewModel.restBetweenSets.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                restBetweenSets.setText(String.valueOf(integer));
            }
        });

        editViewModel.calmDown.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                calmDown.setText(String.valueOf(integer));
            }
        });

        /*editViewModel.color.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                colorView.setCardBackgroundColor(integer);
            }

        });*/

        View.OnClickListener buttonsClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeValue(view);
            }
        };

        for (ImageButton button : buttons) {
            button.setOnClickListener(buttonsClickListener);
        }

        final View.OnClickListener confirmButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("timer", editViewModel.saveTimer(type, getApplicationContext()));
                setResult(RESULT_OK, data);
                finish();
            }
        };

        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(confirmButtonListener);
    }


   /* private void createColorPickerDialog(int id) {
        ColorPickerDialog.newBuilder()
                .setColor(Color.RED)
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .setColorShape(ColorShape.SQUARE)
                .setDialogId(id)
                .show(this);
    }*/

    private void showToast() {
        Toast.makeText(this, getResources().getString(R.string.min_value_toast),
                Toast.LENGTH_SHORT).show();
    }

   /* @Override
    public void onColorSelected(int _id, int color) {
        editViewModel.color.setValue(color);
    }*/

   /* @Override
    public void onDialogDismissed(int dialogId) {

    }*/

    private void changeValue(View view) {
        if (view.getId() == R.id.preparingMinus) {
            if (editViewModel.preparing.getValue() - 1 >= 0)
                editViewModel.preparing.setValue(editViewModel.preparing.getValue() - 1);
            else showToast();
        } else if (view.getId() == R.id.workMinus) {
            if (editViewModel.work.getValue() - 1 >= 0)
                editViewModel.work.setValue(editViewModel.work.getValue() - 1);
            else showToast();
        } else if (view.getId() == R.id.restMinus) {
            if (editViewModel.rest.getValue() - 1 >= 0)
                editViewModel.rest.setValue(editViewModel.rest.getValue() - 1);
            else showToast();
        } else if (view.getId() == R.id.cyclesMinus) {
            if (editViewModel.cycles.getValue() - 1 >= 1)
                editViewModel.cycles.setValue(editViewModel.cycles.getValue() - 1);
            else showToast();
        } else if (view.getId() == R.id.setsMinus) {
            if (editViewModel.sets.getValue() - 1 >= 1)
                editViewModel.sets.setValue(editViewModel.sets.getValue() - 1);
            else showToast();
        } else if (view.getId() == R.id.betweenSetsMinus) {
            if (editViewModel.restBetweenSets.getValue() - 1 >= 0)
                editViewModel.restBetweenSets.setValue(editViewModel.restBetweenSets.getValue() - 1);
            else showToast();
        } else if (view.getId() == R.id.calmDownMinus) {
            if (editViewModel.calmDown.getValue() - 1 >= 0)
                editViewModel.calmDown.setValue(editViewModel.calmDown.getValue() - 1);
            else showToast();
        } else if (view.getId() == R.id.preparingPlus) {
            editViewModel.preparing.setValue(editViewModel.preparing.getValue() + 1);
        } else if (view.getId() == R.id.workPlus) {
            editViewModel.work.setValue(editViewModel.work.getValue() + 1);
        } else if (view.getId() == R.id.restPlus) {
            editViewModel.rest.setValue(editViewModel.rest.getValue() + 1);
        } else if (view.getId() == R.id.cyclesPlus) {
            editViewModel.cycles.setValue(editViewModel.cycles.getValue() + 1);
        } else if (view.getId() == R.id.setsPlus) {
            editViewModel.sets.setValue(editViewModel.sets.getValue() + 1);
        } else if (view.getId() == R.id.betweenSetsPlus) {
            editViewModel.restBetweenSets.setValue(editViewModel.restBetweenSets.getValue() + 1);
        } else if (view.getId() == R.id.calmDownPlus) {
            editViewModel.calmDown.setValue(editViewModel.calmDown.getValue() + 1);
        }
    }
}