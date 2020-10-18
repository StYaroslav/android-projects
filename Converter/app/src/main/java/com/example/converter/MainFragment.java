package com.example.converter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import static androidx.core.content.ContextCompat.getSystemService;

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
    private ArrayAdapter<CharSequence> dataUnitsArray;
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

        spinnerCategories = rootView.findViewById(R.id.spinnerCategories); // инициализация спинеров
        spinnerInitialUnits = rootView.findViewById(R.id.spinnerInitialUnits);
        spinnerConvertToUnits = rootView.findViewById(R.id.spinnerConvertedUnits);

        textField1 = rootView.findViewById(R.id.TextField1); // инициализация полей отображения начальных и конвертированных данных
        textField2 = rootView.findViewById(R.id.TextField2);

        initArrayAdapters(rootView); // инициализация массивов для заполнения юнитов категорий

        spinnerCategories.setAdapter(categoriesArray);
        spinnerCategories.setOnItemSelectedListener(categoriesListener);
        spinnerInitialUnits.setOnItemSelectedListener(unitsListener);
        spinnerConvertToUnits.setOnItemSelectedListener(unitsListener);

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
                textField1.setText(number);
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

        viewModel.getConvertedValue().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textField2.setText(s);
            }
        });
    }

    public void initArrayAdapters(View rootView) {
        categoriesArray = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.categories, android.R.layout.simple_spinner_item);
        categoriesArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        distanceUnitsArray = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.distanceUnits, android.R.layout.simple_spinner_dropdown_item);
        distanceUnitsArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        weightUnitsArray = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.weightUnits, android.R.layout.simple_spinner_dropdown_item);
        weightUnitsArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataUnitsArray = ArrayAdapter.createFromResource(rootView.getContext(), R.array.dataUnits,
                android.R.layout.simple_spinner_dropdown_item);
        dataUnitsArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void addUnits(ArrayAdapter<CharSequence> units, AdapterView<?> adapterView) {
        spinnerInitialUnits.setAdapter(units);
        spinnerConvertToUnits.setAdapter(units);

        spinnerInitialUnits.setSelection(units.getPosition(selectedInitialUnit), false);
        spinnerConvertToUnits.setSelection(units.getPosition(selectedConvertedUnit), false);

        viewModel.setInitialUnit(spinnerInitialUnits.getSelectedItem().toString());
        viewModel.setConvertToUnit(spinnerConvertToUnits.getSelectedItem().toString());
    }

    public void copyInitValueButtonListener(View view) {
        copy(textField1.getText().toString());
    }

    public void copyConvertedValueButtonListener(View view) {
        copy(textField2.getText().toString());
    }

    public void copy(String text) {
        ClipboardManager manager = getSystemService(Objects.requireNonNull(getContext())
                , ClipboardManager.class);
        ClipData clip = ClipData.newPlainText("text", text);
        assert manager != null;
        manager.setPrimaryClip(clip);
        Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();
    }

    AdapterView.OnItemSelectedListener unitsListener = new AdapterView.OnItemSelectedListener() {
        // Обработчик событий для подкатегорий

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch (adapterView.getId()) {
                case R.id.spinnerInitialUnits:
                    viewModel.setInitialUnit(adapterView.getSelectedItem().toString());
                    break;
                case R.id.spinnerConvertedUnits:
                    viewModel.setConvertToUnit(adapterView.getSelectedItem().toString());
                    break;
            }
            viewModel.convert();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    AdapterView.OnItemSelectedListener categoriesListener = new AdapterView.OnItemSelectedListener() {
        // Обработчик событий для категорий.
        // При выборе определённой категории загружаются возможные юниты для конвертации

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
                case "Data":
                    addUnits(dataUnitsArray, adapterView);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    public void switchButtonListener(View view) {
        // обработчик нажатия switchButton

        String temp = selectedInitialUnit;
        viewModel.setInitialUnit(selectedConvertedUnit);
        spinnerInitialUnits.setSelection(((ArrayAdapter<CharSequence>) spinnerInitialUnits
                .getAdapter()).getPosition(selectedConvertedUnit));
        spinnerConvertToUnits.setSelection(((ArrayAdapter<CharSequence>) spinnerConvertToUnits
                .getAdapter()).getPosition(temp));
        viewModel.setConvertToUnit(temp);
        viewModel.setInitialNumber(textField2.getText().toString());
        viewModel.convert();
    }
}