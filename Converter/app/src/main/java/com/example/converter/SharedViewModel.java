package com.example.converter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> initialNumber = new MutableLiveData<>("");
    private MutableLiveData<String> category = new MutableLiveData<>();
    private MutableLiveData<String> initialValue = new MutableLiveData<>();
    private MutableLiveData<String> convertedValue = new MutableLiveData<>();
    private MutableLiveData<String> initialUnit = new MutableLiveData<>();
    private MutableLiveData<String> convertToUnit = new MutableLiveData<>();

    private Boolean hasDot = false;

    public void setNumber(String inputNumber) {
        switch (inputNumber) {
            case "11":
                if (!hasDot) {
                    initialNumber.setValue(initialNumber.getValue() + ".");
                    hasDot = true;
                }
                break;
            case "12":
                if (!Objects.equals(initialNumber.getValue(), "")) {
                    initialNumber.setValue(Objects.requireNonNull(initialNumber.getValue())
                            .substring(0, initialNumber.getValue().length() - 1));
                }
                break;
            default:
                initialNumber.setValue(initialNumber.getValue() + inputNumber);
                break;
        }

        convert();
    }

    public void convert() {
        if (!Objects.equals(initialNumber.getValue(), "")) {
            switch (Objects.requireNonNull(category.getValue())) {
                case "Distance":
                    DistanceConverter distanceConverter = new DistanceConverter();
                    DConverter converter = new DConverter(initialUnit.getValue(), convertToUnit
                            .getValue());
                    convertedValue.setValue(distanceConverter.Convert(converter,
                            initialNumber.getValue()));
                    break;
                case "Weight":
                    WeightConverter weightConverter = new WeightConverter();
                    WConverter wConverter = new WConverter(initialUnit.getValue(), convertToUnit
                            .getValue());
                    convertedValue.setValue(weightConverter.Convert(wConverter,
                            initialNumber.getValue()));
                    break;
                case "Data":
                    DataConverter dataConverter = new DataConverter();
                    Converter dConverter = new Converter(initialUnit.getValue(), convertToUnit
                            .getValue());
                    convertedValue.setValue(dataConverter.Convert(dConverter,
                            initialNumber.getValue()));
                    break;
            }
        } else {
            convertedValue.setValue("");
        }
    }

    public void setCategory(String inputCategory) {
        category.setValue(inputCategory);
    }

    public void setInitialNumber(String value) {
        initialNumber.setValue(value);
    }

    public void setInitialUnit(String unit) {
        initialUnit.setValue(unit);
    }

    public void setConvertToUnit(String unit) {
        convertToUnit.setValue(unit);
    }

    public LiveData<String> getNumber() {
        return initialNumber;
    }

    public LiveData<String> getCategory() {
        return category;
    }

    public LiveData<String> getInitialNumber() {
        return initialNumber;
    }

    public LiveData<String> getConvertedValue() {
        return convertedValue;
    }

    public LiveData<String> getInitialUnit() {
        return initialUnit;
    }

    public LiveData<String> getConvertToUnit() {
        return convertToUnit;
    }
}
