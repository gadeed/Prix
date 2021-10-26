package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.flexural.developers.prixapp.R;

public class SettingsActivity extends AppCompatActivity {

    private ImageView mButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mButtonBack = findViewById(R.id.button_back);

        init();

    }

    private void init() {
        mButtonBack.setOnClickListener(v -> onBackPressed());
    }
}