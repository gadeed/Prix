package com.flexural.developers.prixapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.flexural.developers.prixapp.activity.ElectrictyOptionsActivity;
import com.flexural.developers.prixapp.activity.PaymentActivity;
import com.flexural.developers.prixapp.activity.PrixActivity;
import com.flexural.developers.prixapp.activity.ProfileActivity;
import com.flexural.developers.prixapp.activity.RemitMoneyActivity;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mButtonPrix, mButtonNetwork, mButtonDataBundles, mButtonElectricity, mButtonPayment;
    private LinearLayout mButtonRemit;
    private ImageView mButtonProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonPrix = findViewById(R.id.button_prix);
        mButtonNetwork = findViewById(R.id.button_network);
        mButtonDataBundles = findViewById(R.id.button_data_bundles);
        mButtonElectricity = findViewById(R.id.button_electricity);
        mButtonPayment = findViewById(R.id.button_payment);
        mButtonRemit = findViewById(R.id.button_remit);
        mButtonProfile = findViewById(R.id.button_profile);

        init();

    }

    private void init() {
        mButtonPrix.setOnClickListener(v -> {
            Intent intent = new Intent(this, PrixActivity.class);
            intent.putExtra("title", "prix");
            startActivity(intent);
        });

        mButtonNetwork.setOnClickListener(v -> {
            Intent intent = new Intent(this, PrixActivity.class);
            intent.putExtra("title", "network");
            startActivity(intent);
        });

        mButtonDataBundles.setOnClickListener(v -> {
            Intent intent = new Intent(this, PrixActivity.class);
            intent.putExtra("title", "data");
            startActivity(intent);
        });

        mButtonElectricity.setOnClickListener(v -> {
            startActivity(new Intent(this, ElectrictyOptionsActivity.class));
        });

        mButtonPayment.setOnClickListener(v -> {
            startActivity(new Intent(this, PaymentActivity.class));
        });

        mButtonRemit.setOnClickListener(v -> {
            startActivity(new Intent(this, RemitMoneyActivity.class));
        });

        mButtonProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

    }
}