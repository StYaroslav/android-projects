package com.example.timer;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EditViewModelFactory implements ViewModelProvider.Factory {
    TimerData timer;

    public EditViewModelFactory(TimerData timer) {
        this.timer = timer;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(EditTimerViewModel.class)){
            return (T) new EditTimerViewModel(timer);
        }
        throw new IllegalArgumentException("Incorrect ViewModelClass");
    }
}
