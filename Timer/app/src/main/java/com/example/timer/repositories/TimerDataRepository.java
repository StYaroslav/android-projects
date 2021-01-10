package com.example.timer.repositories;

import com.example.timer.models.TimerData;

import java.util.ArrayList;

public interface TimerDataRepository {
    ArrayList<TimerData> get();
    void clear();
    void update(TimerData timer);
    int add(TimerData timer);
    void delete(int Id);
}
