package com.example.timer;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Timer;

public class TimerViewModel extends ViewModel {
    MutableLiveData<Integer> id = new MutableLiveData<>(1);
    MutableLiveData<String> title = new MutableLiveData<>("");
    MutableLiveData<Integer> preparingTime = new MutableLiveData<>(0);
    MutableLiveData<Integer> workingTime = new MutableLiveData<>(0);
    MutableLiveData<Integer> restTime = new MutableLiveData<>(0);
    MutableLiveData<Integer> cyclesAmount = new MutableLiveData<>(1);
    MutableLiveData<Integer> setsAmount = new MutableLiveData<>(1);
    MutableLiveData<Integer> restBetweenSets = new MutableLiveData<>(0);
    MutableLiveData<Integer> calmDown = new MutableLiveData<>(0);
    MutableLiveData<Integer> color = new MutableLiveData<>(16604928);

    public TimerViewModel(TimerData timer) {
        this.id.setValue(timer.getId());
        this.title.setValue(timer.getTitle());
        this.preparingTime.setValue(timer.getPreparationTime());
        this.workingTime.setValue(timer.getWorkingTime());
        this.restTime.setValue(timer.getRestTime());
        this.cyclesAmount.setValue(timer.getCyclesAmount());
        this.setsAmount.setValue(timer.getSetsAmount());
        this.restBetweenSets.setValue(timer.getBetweenSetsRest());
        this.calmDown.setValue(timer.getCalmDown());
        this.color.setValue(timer.getColor());
    }

    public TimerData getTimer () {
        return new TimerData(this.id.getValue(), this.title.getValue(), this.preparingTime.getValue(), this.workingTime.getValue(),
                this.restTime.getValue(), this.cyclesAmount.getValue(), this.setsAmount.getValue(), this.restBetweenSets.getValue(),
                this.calmDown.getValue(), this.color.getValue());
    }

    public void addTimer(TimerData timer, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        dbHelper.addTimer(timer);
    }

    public TimerData updateTimer( Context context) {
        TimerData timer = getTimer();
        DbHelper dbHelper = new DbHelper(context);
        dbHelper.updateTimer(timer);
        return timer;
    }
}
