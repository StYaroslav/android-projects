package com.example.converter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private MainFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView appVersion = this.findViewById(R.id.appVersion);
        setAppVersion(appVersion);
        fragment = (MainFragment) this.getSupportFragmentManager().findFragmentById(R.id.fragmentMain);
    }

    @SuppressLint("SetTextI18n")
    public void setAppVersion(TextView text) {
        if (BuildConfig.FLAVOR.equals("pro")) {
            text.setText("Converter Pro" + " " +BuildConfig.VERSION_CODE + " " + BuildConfig.VERSION_NAME);
        } else {
            text.setText("Converter" + " " +BuildConfig.VERSION_CODE + " " + BuildConfig.VERSION_NAME);
        }
    }

    public void switchButtonOnClick(View view) {
        assert fragment != null;
        fragment.switchButtonListener(view);
    }

    public void copyInitValue(View view) {
        assert fragment != null;
        fragment.copyInitValueButtonListener(view);
    }

    public void copyConvertedValue(View view) {
        assert fragment != null;
        fragment.copyConvertedValueButtonListener(view);
    }
}
