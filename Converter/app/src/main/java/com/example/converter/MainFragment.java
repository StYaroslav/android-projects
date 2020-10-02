package com.example.converter;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
        viewModel.getNumber().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer number) {
                setNumber(number);
            }
        });
    }

    public void setCategory(String category) {
        selectedCategory = category;
    }

    public void setNumber(Integer buttonIndex) {
        if (buttonIndex <= 9) {
            textField1.append(String.valueOf(buttonIndex));
        } else if (buttonIndex == 10) {
            textField1.append("0");
        } else if (buttonIndex == 11) {
            textField1.append(".");
        } else if (buttonIndex == 12) {
            if (textField1.getText().length() != 0) {
                if (textField1.getText().length() == 1) {
                    textField1.setText("");
                    textField2.setText("");
                } else {
                    String text = textField1.getText().toString();
                    textField1.setText(text.substring(0, text.length() - 1));
                }
            }
        }

        convertUnitsForCategory(spinnerCategories.getSelectedItem().toString());

    }

    public void convertUnitsForCategory(String category) {
        String text = textField1.getText().toString();
        if (!text.equals("")) {
            switch (category) {
                case "Distance":
                    DistanceConverter distanceConverter = new DistanceConverter();
                    DConverter converter = new DConverter(spinnerInitialUnits
                            .getSelectedItem().toString(), spinnerConvertToUnits.getSelectedItem()
                            .toString());
                    distanceConverter.Convert(converter, text, textField2);
                    break;
                case "Weight":
                    WeightConverter weightConverter = new WeightConverter();
                    WConverter wConverter = new WConverter(spinnerInitialUnits
                            .getSelectedItem().toString(), spinnerConvertToUnits.getSelectedItem()
                            .toString());
                    weightConverter.Convert(wConverter, text, textField2);
                case "Currency":
                    break;
            }
        }
    }

    public void addUnits(ArrayAdapter<CharSequence> units, AdapterView<?> adapterView) {
        spinnerInitialUnits.setAdapter(units);
        spinnerConvertToUnits.setAdapter(units);

        spinnerInitialUnits.setSelection(0, false);
        spinnerConvertToUnits.setSelection(0, false);

        spinnerInitialUnits.setOnItemSelectedListener(unitsListener);
        spinnerConvertToUnits.setOnItemSelectedListener(unitsListener);
    }

    AdapterView.OnItemSelectedListener unitsListener = new AdapterView.OnItemSelectedListener() { // Обработчик событий для подкатегорий

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
            switch (selectedCategory) {
                case "Distance":
                    addUnits(distanceUnitsArray, adapterView);
                    break;
                case "Weight":
                    addUnits(weightUnitsArray, adapterView);
                    break;
                case "Currency":
                    ArrayAdapter<CharSequence> currencyUnits = ArrayAdapter.createFromResource(
                            adapterView.getContext(),
                            R.array.currencyUnits, android.R.layout.simple_spinner_item);
                    addUnits(currencyUnits, adapterView);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
}