package com.example.timer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditTimerViewModel extends ViewModel {
    MutableLiveData<Integer> id = new MutableLiveData<>();
    MutableLiveData<String> title = new MutableLiveData<>();
    MutableLiveData<Integer> preparingTime = new MutableLiveData<>();
    MutableLiveData<Integer> workingTime = new MutableLiveData<>();
    MutableLiveData<Integer> restTime = new MutableLiveData<>();
    MutableLiveData<Integer> cyclesAmount = new MutableLiveData<>();
    MutableLiveData<Integer> setsAmount = new MutableLiveData<>();
    MutableLiveData<Integer> restBetweenSets = new MutableLiveData<>();
    MutableLiveData<Integer> calmDown = new MutableLiveData<>();
}
