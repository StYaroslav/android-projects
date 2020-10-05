package com.example.converter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> initialNumber = new MutableLiveData<>();
    private MutableLiveData<String> category = new MutableLiveData<>();
    private MutableLiveData<Double> initialValue = new MutableLiveData<>();
    private MutableLiveData<String> initialUnit = new MutableLiveData<>();
    private MutableLiveData<String> convertToUnit = new MutableLiveData<>();

    private String number = "";
    private Boolean hasDot = false;

    public void setNumber(String inputNumber) {
        switch (inputNumber) {
            case "11":
                if (!hasDot){
                    number += ".";
                    hasDot = true;
                }
                break;
            case "12":
                number = number.substring(0, number.length() - 1);
                break;
            default:
                number += inputNumber;
                break;
        }
        if (!number.equals("")) {
            initialNumber.setValue(number);
        } else {
            initialNumber.setValue(null);
        }
    }

    public void setCategory (String inputCategory) {
        category.setValue(inputCategory);
    }

    public void setInitialValue(Double value) {
        initialValue.setValue(value);
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

    public LiveData<Double> getInitialValue() {
        return initialValue;
    }

    public LiveData<String> getInitialUnit() {
        return initialUnit;
    }

    public LiveData<String> getConvertToUnit() {
        return convertToUnit;
    }
}
