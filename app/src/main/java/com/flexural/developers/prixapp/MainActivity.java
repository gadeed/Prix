package com.flexural.developers.prixapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.flexural.developers.prixapp.activity.DataActivity;
import com.flexural.developers.prixapp.activity.ElectrictyOptionsActivity;
import com.flexural.developers.prixapp.activity.PaymentActivity;
import com.flexural.developers.prixapp.activity.PrixActivity;
import com.flexural.developers.prixapp.activity.ProfileActivity;
import com.flexural.developers.prixapp.activity.RemitMoneyActivity;
import com.flexural.developers.prixapp.activity.SettingsActivity;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mButtonPrix, mButtonNetwork, mButtonDataBundles, mButtonElectricity, mButtonPayment;
    private LinearLayout mButtonRemit;
    private ImageView mButtonProfile, mButtonSettings;

    private boolean doubleBackToExitPressedOnce = false;


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
            Intent intent = new Intent(this, DataActivity.class);
            intent.putExtra("dataBundles", "vodacom");
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


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(findViewById(android.R.id.content), "Please Click Back Again to Exit", Snackbar.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
