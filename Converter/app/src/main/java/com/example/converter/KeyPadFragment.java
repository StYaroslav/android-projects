package com.example.converter;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

public class KeyPadFragment extends Fragment implements View.OnClickListener{
    private SharedViewModel viewModel;
    private TextView appVersion;

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

    @SuppressLint("SetTextI18n")
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
        String buttonIndex = translateIdToIndex(view.getId());
        viewModel.setNumber(buttonIndex);
    }


    String translateIdToIndex(int id) {
        String index = "";
        switch (id) {
            case R.id.button1:
                index = "1";
                break;
            case R.id.button2:
                index = "2";
                break;
            case R.id.button3:
                index = "3";
                break;
            case R.id.button4:
                index = "4";
                break;
            case R.id.button5:
                index = "5";
                break;
            case R.id.button6:
                index = "6";
                break;
            case R.id.button7:
                index = "7";
                break;
            case R.id.button8:
                index = "8";
                break;
            case R.id.button9:
                index = "9";
                break;
            case R.id.button10:
                index = "0";
                break;
            case R.id.button11:
                index = "11";
                break;
            case R.id.button12:
                index = "12";
                break;
        }
        return index;
    }
}