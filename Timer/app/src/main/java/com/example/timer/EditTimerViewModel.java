package com.example.timer;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditTimerViewModel extends ViewModel {
    MutableLiveData<Integer> id = new MutableLiveData<>();
    MutableLiveData<String> title = new MutableLiveData<>();
    MutableLiveData<Integer> preparing = new MutableLiveData<>();
    MutableLiveData<Integer> work = new MutableLiveData<>();
    MutableLiveData<Integer> rest = new MutableLiveData<>();
    MutableLiveData<Integer> cycles = new MutableLiveData<>();
    MutableLiveData<Integer> sets = new MutableLiveData<>();
    MutableLiveData<Integer> restBetweenSets = new MutableLiveData<>();
    MutableLiveData<Integer> calmDown = new MutableLiveData<>();
    MutableLiveData<Integer> color = new MutableLiveData<>();

    public EditTimerViewModel(TimerData timer) {
        id.setValue(timer.getId());
        title.setValue(timer.getTitle());
        preparing.setValue(timer.getPreparationTime());
        work.setValue(timer.getWorkingTime());
        rest.setValue(timer.getRestTime());
        cycles.setValue(timer.getCyclesAmount());
        sets.setValue(timer.getSetsAmount());
        restBetweenSets.setValue(timer.getBetweenSetsRest());
        calmDown.setValue(timer.getCalmDown());
    }

    public TimerData getTimer() {
        return new TimerData(id.getValue(), title.getValue(), preparing.getValue(),
                work.getValue(), rest.getValue(), cycles.getValue(), sets.getValue(),
                restBetweenSets.getValue(), calmDown.getValue(), 0);
    }

    public TimerData saveTimer(String type, Context context) {
        DbHelper timerDb = new DbHelper(context);
        TimerData timer = getTimer();
        if (type.equals("edit")) {
            timerDb.updateTimer(timer);
        }
        else {
            timer.setId(timerDb.addTimer(timer));
        }
        return timer;
    }
}
