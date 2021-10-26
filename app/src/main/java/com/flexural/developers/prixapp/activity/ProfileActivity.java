package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flexural.developers.prixapp.MainActivity;
import com.flexural.developers.prixapp.R;

public class ProfileActivity extends AppCompatActivity {
    private LinearLayout mEditProfile, mButtonDevice, mButtonTopup, mButtonTransfer, mButtonSales;
    private ImageView mButtonDashboard, mButtonSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mEditProfile = findViewById(R.id.edit_profile);
        mButtonDevice = findViewById(R.id.button_device);
        mButtonTopup = findViewById(R.id.button_topup);
        mButtonDashboard = findViewById(R.id.button_dashboard);
        mButtonSettings = findViewById(R.id.button_setting);
        mButtonTransfer = findViewById(R.id.button_transfer);
        mButtonSales = findViewById(R.id.button_sales);

        init();

    }

    private void init() {
        mEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, EditProfileActivity.class));
        });

        mButtonDevice.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("menu", "device");
            startActivity(intent);
        });

        mButtonTopup.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("menu", "topup");
            startActivity(intent);
        });

        mButtonDashboard.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        mButtonSettings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });

        mButtonTransfer.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("menu", "transfer");
            startActivity(intent);
        });

        mButtonSales.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("menu", "sales");
            startActivity(intent);
        });
    }
}