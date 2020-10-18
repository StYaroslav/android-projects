package com.example.converter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private MainFragment fragment;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView appVersion = this.findViewById(R.id.appVersion);
        appVersion.setText(BuildConfig.VERSION_CODE + " " + BuildConfig.VERSION_NAME);
        fragment = (MainFragment) this.getSupportFragmentManager().findFragmentById(R.id.fragmentMain);
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
