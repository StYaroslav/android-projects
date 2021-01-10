package com.example.timer.viewModels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.timer.repositories.DbPhaseRepository;
import com.example.timer.repositories.DbTimerDataRepository;
import com.example.timer.models.TimerData;

public class EditTimerViewModel extends ViewModel {
    public MutableLiveData<Integer> id = new MutableLiveData<>();
    public MutableLiveData<String> title = new MutableLiveData<>();
    public MutableLiveData<Integer> preparing = new MutableLiveData<>();
    public MutableLiveData<Integer> work = new MutableLiveData<>();
    public  MutableLiveData<Integer> rest = new MutableLiveData<>();
    public MutableLiveData<Integer> cycles = new MutableLiveData<>();
    public MutableLiveData<Integer> sets = new MutableLiveData<>();
    public MutableLiveData<Integer> restBetweenSets = new MutableLiveData<>();
    public MutableLiveData<Integer> calmDown = new MutableLiveData<>();
    public MutableLiveData<Integer> color = new MutableLiveData<>();

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
        color.setValue(timer.getColor());
    }

    public TimerData getTimer() {
        return new TimerData(id.getValue(), title.getValue(), preparing.getValue(),
                work.getValue(), rest.getValue(), cycles.getValue(), sets.getValue(),
                restBetweenSets.getValue(), calmDown.getValue(), color.getValue());
    }

    public TimerData saveTimer(String type, Context context) {
        DbTimerDataRepository timerDataRepository = new DbTimerDataRepository(context);
        DbPhaseRepository phaseRepository = new DbPhaseRepository(context);
        TimerData timer = getTimer();
        if (type.equals("edit")) {
            timerDataRepository.update(timer);
            phaseRepository.update(timer);
        }
        else {
            timer.setId(timerDataRepository.add(timer));
            phaseRepository.add(timer);
        }
        return timer;
    }
}
