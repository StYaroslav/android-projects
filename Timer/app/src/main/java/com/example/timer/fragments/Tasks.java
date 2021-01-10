package com.example.timer.fragments;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.timer.viewModels.ActiveTimerViewModel;
import com.example.timer.R;

import java.util.ArrayList;

public class Tasks extends Fragment {

    ArrayList<String> tasks = new ArrayList<>();
    ArrayAdapter<String> adapter;
    OnItemClicked onItemClicked;
    ListView listView;
    ActiveTimerViewModel activeViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onItemClicked = (OnItemClicked) getActivity();
        activeViewModel = new ViewModelProvider(requireActivity()).get(ActiveTimerViewModel.class);
        fillTasksList();
        adapter = new ArrayAdapter<>(getContext(), R.layout.tasks_item, tasks);
    }

    private void fillTasksList(){
        int counter = 0;
        Resources resources = getResources();
        for (int cycleId = 0; cycleId < activeViewModel.timer.getCyclesAmount(); cycleId++) {
            tasks.add(++counter + ". " + resources.getString(R.string.preparing) + ": " + activeViewModel.timer.getPreparationTime());
            for (int setId = 0; setId < activeViewModel.timer.getSetsAmount(); setId++) {
                tasks.add(++counter + ". " + resources.getString(R.string.work) + ": " + activeViewModel.timer.getWorkingTime());
                tasks.add(++counter + ". " + resources.getString(R.string.rest) + ": " + activeViewModel.timer.getRestTime());
            }
            tasks.add(++counter + ". " + resources.getString(R.string.calmdown)
                    + ": " + activeViewModel.timer.getCalmDown());

            tasks.add(++counter + ". " + resources.getString(R.string.rest_between_cycles) +
                    ": " + activeViewModel.timer.getBetweenSetsRest());
        }
        tasks.remove(tasks.size() - 1);
        tasks.add(resources.getString(R.string.finish));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        listView = view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (activeViewModel.isRunning.getValue()) {
                    activeViewModel.changePhase(position);
                    listView.smoothScrollToPosition(position);
                    onItemClicked.onItemClicked(position);
                }
            }
        });



        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public interface OnItemClicked{
        void onItemClicked(int position);
    }
}

