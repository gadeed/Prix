package com.flexural.developers.prixapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.flexural.developers.prixapp.R;

public class RegistrationActivity extends AppCompatActivity {

    private RelativeLayout mButtonNext;
    private ImageView mButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mButtonNext = findViewById(R.id.button_next);
        mButtonBack = findViewById(R.id.button_back);

        init();

    }

    private void init() {
        mButtonNext.setOnClickListener(v -> {
            startActivity(new Intent(this, DetailsActivity.class));
        });

        mButtonBack.setOnClickListener(v -> {
            onBackPressed();
        });

    }
}