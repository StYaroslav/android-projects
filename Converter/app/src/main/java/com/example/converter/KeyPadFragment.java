package com.example.converter;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

public class KeyPadFragment extends Fragment implements View.OnClickListener{
    private SharedViewModel viewModel;

    public KeyPadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
                .get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_key_pad, container, false);

        int[] numberButtons =  {R.id.button1, R.id.button2, R.id.button3, R.id.button4,
                R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button10,
                R.id.button11, R.id.button12};

        for (int button: numberButtons) {
            rootView.findViewById(button).setOnClickListener(this);
        }

        return rootView;
    }

    @Override
    public void onClick(View view) {
        int buttonIndex = translateIdToIndex(view.getId());
        viewModel.setNumber(buttonIndex);
    }


    int translateIdToIndex(int id) {
        int index = -1;
        switch (id) {
            case R.id.button1:
                index = 1;
                break;
            case R.id.button2:
                index = 2;
                break;
            case R.id.button3:
                index = 3;
                break;
            case R.id.button4:
                index = 4;
                break;
            case R.id.button5:
                index = 5;
                break;
            case R.id.button6:
                index = 6;
                break;
            case R.id.button7:
                index = 7;
                break;
            case R.id.button8:
                index = 8;
                break;
            case R.id.button9:
                index = 9;
                break;
            case R.id.button10:
                index = 10;
                break;
            case R.id.button11:
                index = 11;
                break;
            case R.id.button12:
                index = 12;
                break;
        }
        return index;
    }
}