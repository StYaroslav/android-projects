package com.example.converter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Objects;

public class MainFragment extends Fragment {

    private SharedViewModel viewModel;
    private TextView textField1;
    private TextView textField2;
    private Spinner spinnerCategories;
    private Spinner spinnerInitialUnits;
    private Spinner spinnerConvertToUnits;
    private ArrayAdapter<CharSequence> categoriesArray;
    private ArrayAdapter<CharSequence> distanceUnitsArray;
    private ArrayAdapter<CharSequence> weightUnitsArray;
    private String selectedCategory;
    private String selectedInitialUnit;
    private String selectedConvertedUnit;
    private String initialValue;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        spinnerCategories = (Spinner) rootView.findViewById(R.id.spinnerCategories);
        spinnerInitialUnits = (Spinner) rootView.findViewById(R.id.spinnerInitialUnits);
        spinnerConvertToUnits = (Spinner) rootView.findViewById(R.id.spinnerConvertedUnits);

        categoriesArray = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.categories, android.R.layout.simple_spinner_item);
        categoriesArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(categoriesArray);

        distanceUnitsArray = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.distanceUnits, android.R.layout.simple_spinner_dropdown_item);
        distanceUnitsArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        weightUnitsArray = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.weightUnits, android.R.layout.simple_spinner_dropdown_item);
        weightUnitsArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        textField1 = (TextView) rootView.findViewById(R.id.TextField1);
        textField2 = (TextView) rootView.findViewById(R.id.TextField2);

        spinnerCategories.setOnItemSelectedListener(categoriesListener);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
                .get(SharedViewModel.class);
        viewModel.getNumber().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String number) {
                initialValue = number;
                setNumber(number);
            }
        });

        viewModel.getCategory().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                selectedCategory = s;
            }
        });

        viewModel.getInitialUnit().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                selectedInitialUnit = s;
            }
        });

        viewModel.getConvertToUnit().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                selectedConvertedUnit = s;
            }
        });
    }

    public void setNumber(String number) {
        textField1.setText(number);
        convertUnitsForCategory(selectedCategory);
    }

    public void convertUnitsForCategory(String category) {
        if (!initialValue.equals("")) {
            switch (category) {
                case "Distance":
                    DistanceConverter distanceConverter = new DistanceConverter();
                    DConverter converter = new DConverter(selectedInitialUnit, selectedConvertedUnit);
                    distanceConverter.Convert(converter, initialValue, textField2);
                    break;
                case "Weight":
                    WeightConverter weightConverter = new WeightConverter();
                    WConverter wConverter = new WConverter(selectedInitialUnit, selectedConvertedUnit);
                    weightConverter.Convert(wConverter, initialValue, textField2);
                case "Currency":
                    break;
            }
        }
    }

    public void addUnits(ArrayAdapter<CharSequence> units, AdapterView<?> adapterView) {
        spinnerInitialUnits.setAdapter(units);
        spinnerConvertToUnits.setAdapter(units);

        spinnerInitialUnits.setSelection(0, false);
        spinnerConvertToUnits.setSelection(1, false);

        viewModel.setInitialUnit(spinnerInitialUnits.getSelectedItem().toString());
        viewModel.setConvertToUnit(spinnerConvertToUnits.getSelectedItem().toString());

        spinnerInitialUnits.setOnItemSelectedListener(unitsListener);
        spinnerConvertToUnits.setOnItemSelectedListener(unitsListener);
    }

    AdapterView.OnItemSelectedListener unitsListener = new AdapterView.OnItemSelectedListener() { // Обработчик событий для подкатегорий

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch (view.getId()) {
                case R.id.spinnerInitialUnits:
                    viewModel.setInitialUnit(adapterView.getSelectedItem().toString());
                    break;
                case R.id.spinnerConvertedUnits:
                    viewModel.setConvertToUnit(adapterView.getSelectedItem().toString());
                    break;
            }
            convertUnitsForCategory(selectedCategory);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    AdapterView.OnItemSelectedListener categoriesListener = new AdapterView.OnItemSelectedListener() { // Обработчик событий для категорий. При выборе определённой категории загружаются возможные юниты для конвертации
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            selectedCategory = adapterView.getItemAtPosition(i).toString();
            viewModel.setCategory(selectedCategory);
            switch (selectedCategory) {
                case "Distance":
                    addUnits(distanceUnitsArray, adapterView);
                    break;
                case "Weight":
                    addUnits(weightUnitsArray, adapterView);
                    break;
                case "Currency":
                    /*ArrayAdapter<CharSequence> currencyUnits = ArrayAdapter.createFromResource(
                            adapterView.getContext(),
                            R.array.currencyUnits, android.R.layout.simple_spinner_item);
                    addUnits(currencyUnits, adapterView);*/
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
}