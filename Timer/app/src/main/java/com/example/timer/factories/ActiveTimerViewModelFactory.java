package com.example.timer.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.timer.models.Phase;
import com.example.timer.models.TimerData;
import com.example.timer.viewModels.ActiveTimerViewModel;

import java.util.ArrayList;

public class ActiveTimerViewModelFactory implements ViewModelProvider.Factory {

    private final TimerData timer;
    private final ArrayList<Phase> phaseList;
    public ActiveTimerViewModelFactory(TimerData timer, ArrayList<Phase> phaseList){
        this.timer = timer;
        this.phaseList = phaseList;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ActiveTimerViewModel.class)){
            return (T) new ActiveTimerViewModel(timer, phaseList);
        }
        throw new IllegalArgumentException("Incorrect ViewModel class");
    }
}