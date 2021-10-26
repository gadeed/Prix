package com.flexural.developers.prixapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.flexural.developers.prixapp.activity.ElectrictyOptionsActivity;
import com.flexural.developers.prixapp.activity.PaymentActivity;
import com.flexural.developers.prixapp.activity.PrixActivity;
import com.flexural.developers.prixapp.activity.ProfileActivity;
import com.flexural.developers.prixapp.activity.RemitMoneyActivity;
import com.flexural.developers.prixapp.activity.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mButtonPrix, mButtonNetwork, mButtonDataBundles, mButtonElectricity, mButtonPayment;
    private LinearLayout mButtonRemit;
    private ImageView mButtonProfile, mButtonSettings;

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
        mButtonSettings = findViewById(R.id.button_setting);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
        }

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

        mButtonSettings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
