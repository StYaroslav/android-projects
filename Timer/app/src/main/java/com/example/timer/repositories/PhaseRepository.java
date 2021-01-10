package com.example.timer.repositories;

import com.example.timer.models.Phase;
import com.example.timer.models.TimerData;

import java.util.ArrayList;

public interface PhaseRepository {
    ArrayList<Phase> get(int timerId);
    void add(TimerData timerSequence);
    void delete(int timerId);
    void update(TimerData timerSequence);
}