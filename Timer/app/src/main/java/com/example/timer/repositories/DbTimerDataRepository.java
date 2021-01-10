package com.example.timer.repositories;

import android.content.Context;

import com.example.timer.helpers.DbHelper;
import com.example.timer.models.TimerData;

import java.util.ArrayList;

public class DbTimerDataRepository implements TimerDataRepository {
    private final DbHelper dbHelper;

    public DbTimerDataRepository(Context context){
        dbHelper = new DbHelper(context);
    }

    @Override
    public void update(TimerData timer) {
        dbHelper.updateTimer(timer);
    }

    @Override
    public int add(TimerData timer) {
        return dbHelper.addTimer(timer);
    }

    @Override
    public void delete(int Id) {
        dbHelper.deleteTimer(String.valueOf(Id));
    }

    @Override
    public ArrayList<TimerData> get() {
        return dbHelper.getAllTimers();
    }

    @Override
    public void clear() {
        dbHelper.onUpgrade(
                dbHelper.getWritableDatabase(),
                dbHelper.getWritableDatabase().getVersion(),
                dbHelper.getWritableDatabase().getVersion() + 1);
    }
}
