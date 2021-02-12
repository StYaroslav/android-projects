package com.example.timer;

import android.content.Context;
import android.content.res.Resources;
import android.util.Pair;

import java.util.ArrayList;


public class Phase {
    ArrayList<Pair<String, Integer>> phase = new ArrayList<>();

    public Pair<String, Integer> getPhase(int pos) {
        return this.phase.get(pos);
    }

    public ArrayList<Pair<String, Integer>> createPhase(TimerData timer, Context context) {
        Resources res = context.getResources();
        phase.add(new Pair<String, Integer>(res.getString(R.string.preparing),
                timer.getPreparationTime()));
        for (int i = 0; i < timer.getSetsAmount() + 1; i++) {
            for (int j = 0; j < timer.getCyclesAmount() + 1; j++) {
                this.phase.add(new Pair<String, Integer>(res.getString(R.string.work),
                        timer.getWorkingTime()));
                this.phase.add(new Pair<String, Integer>(res.getString(R.string.rest),
                        timer.getRestTime()));
            }
            this.phase.add(new Pair<String, Integer>(res.getString(R.string.rest), timer.getBetweenSetsRest()));
        }
        return this.phase;
    }
}
