package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.model.Electricity;
import com.google.android.material.snackbar.Snackbar;

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
            Intent intent = new Intent(this, ElectricityActivity.class);
            intent.putExtra("title", "unipin");
            startActivity(intent);
        });

        mButtonPinless.setOnClickListener(v -> {
            Snackbar.make(findViewById(android.R.id.content), "Pinless Meter Service Not Available at the Moment", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
        });

        mButtonBack.setOnClickListener(v -> onBackPressed());

    }
}