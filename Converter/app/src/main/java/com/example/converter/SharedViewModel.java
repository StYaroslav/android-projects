package com.example.converter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Integer> number = new MutableLiveData<>();
    private MutableLiveData<String> category = new MutableLiveData<>();

    public void setNumber(Integer inputNumber) {
        number.setValue(inputNumber);
    }

    public void setCategory (String inputCategory) {
        category.setValue(inputCategory);
    }

    public LiveData<Integer> getNumber() {
        return number;
    }

    public LiveData<String> getCategory() {
        return category;
    }
}
