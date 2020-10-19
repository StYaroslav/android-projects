package com.example.converter.pro;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.converter.MainFragment;
import com.example.converter.R;

import java.util.Objects;

import static androidx.core.content.ContextCompat.getSystemService;


public class ProMainFragment extends MainFragment {

    public ProMainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        assert rootView != null;
        Button switchButton = rootView.findViewById(R.id.switchButton);
        Button copyInitValue = rootView.findViewById(R.id.copyInitValue);
        Button copyConvertedValue = rootView.findViewById(R.id.copyConvertedValue);

        switchButton.setOnClickListener(switchButtonListener);
        copyInitValue.setOnClickListener(copyInitValueButtonListener);
        copyConvertedValue.setOnClickListener(copyConvertedValueButtonListener);

        return rootView;
    }

    Button.OnClickListener switchButtonListener = new Button.OnClickListener() {

        @Override
        public void onClick(View view) {
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
    };

    Button.OnClickListener copyInitValueButtonListener = new Button.OnClickListener() {

        @Override
        public void onClick(View view) {
            copy(textField1.getText().toString());
        }
    };

    Button.OnClickListener copyConvertedValueButtonListener = new Button.OnClickListener() {

        @Override
        public void onClick(View view) {
            copy(textField2.getText().toString());
        }
    };

    public void copy(String text) {
        ClipboardManager manager = getSystemService(Objects.requireNonNull(getContext())
                , ClipboardManager.class);
        ClipData clip = ClipData.newPlainText("text", text);
        assert manager != null;
        manager.setPrimaryClip(clip);
        Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();
    }
}