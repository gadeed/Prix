package com.flexural.developers.prixapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.flexural.developers.prixapp.R;

public class RegistrationActivity extends AppCompatActivity {

    private RelativeLayout mButtonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mButtonNext = findViewById(R.id.button_next);

        mButtonNext.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }
}