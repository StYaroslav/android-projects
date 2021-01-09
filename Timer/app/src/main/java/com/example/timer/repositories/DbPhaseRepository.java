package com.example.timer.repositories;

import android.content.Context;

import com.example.timer.helpers.DbHelper;
import com.example.timer.models.Phase;
import com.example.timer.models.TimerData;

import java.util.ArrayList;

public class DbPhaseRepository implements PhaseRepository {
    private final DbHelper dbHelper;

    public DbPhaseRepository(Context context){
        dbHelper = new DbHelper(context);
    }

    @Override
    public ArrayList<Phase> get(int timerId) {
        return dbHelper.getPhasesById(timerId);
    }

    @Override
    public void add(TimerData timer) {
        dbHelper.addPhases(getCreatedPhaseList(timer), timer.getId());
    }


    @Override
    public void delete(int timerId) {
        dbHelper.deletePhases(timerId);
    }

    @Override
    public void update(TimerData timer) {
        dbHelper.deletePhases(timer.getId());
        dbHelper.addPhases(getCreatedPhaseList(timer), timer.getId());
    }

    private ArrayList<Phase> getCreatedPhaseList(TimerData timer){
        ArrayList<Phase> phaseList = new ArrayList<>();
        for (int cycle = 0; cycle < timer.getCyclesAmount(); cycle++) {
            phaseList.add(new Phase(0, timer.getPreparationTime(), "Preparing"));
            for (int set = 0; set < timer.getSetsAmount(); set++) {
                phaseList.add(new Phase(0, timer.getWorkingTime(), "Work"));
                phaseList.add(new Phase(0, timer.getRestTime(), "Rest"));
            }
            phaseList.add(new Phase(0, timer.getCalmDown(), "Calmdown"));
            phaseList.add(new Phase(0, timer.getBetweenSetsRest(), "Rest"));
        }
        if(phaseList.size() > 0) phaseList.remove(phaseList.size() - 1);
        return phaseList;
    }
}