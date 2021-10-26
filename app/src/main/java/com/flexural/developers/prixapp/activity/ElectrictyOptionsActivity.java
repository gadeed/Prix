package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.flexural.developers.prixapp.R;

public class ElectrictyOptionsActivity extends AppCompatActivity {

    private RelativeLayout mButtonUniPin, mButtonPinless;
    private ImageView mButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricty_options);

        mButtonUniPin = findViewById(R.id.button_unipin);
        mButtonPinless = findViewById(R.id.button_pinless);
        mButtonBack = findViewById(R.id.button_back);

        init();

    }

    private void init() {
        mButtonUniPin.setOnClickListener(v -> {
            Intent intent = new Intent(this, PrixActivity.class);
            intent.putExtra("title", "unipin");
            startActivity(intent);
        });

        mButtonPinless.setOnClickListener(v -> {
            Intent intent = new Intent(this, ElectricityActivity.class);
            startActivity(intent);
        });

        mButtonBack.setOnClickListener(v -> onBackPressed());

    }
}