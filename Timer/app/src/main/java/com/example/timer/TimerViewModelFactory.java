package com.example.timer;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TimerViewModelFactory implements ViewModelProvider.Factory {
    TimerData timer;

    public TimerViewModelFactory(TimerData timer) {
        this.timer = timer;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(TimerViewModel.class)){
            return (T) new TimerViewModel(timer);
        }
        throw new IllegalArgumentException("Incorrect ViewModelClass");
    }
}