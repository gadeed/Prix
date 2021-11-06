package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.flexural.developers.prixapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private ImageView mButtonBack;
    private RelativeLayout mButtonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mButtonBack = findViewById(R.id.button_back);
        mButtonLogout = findViewById(R.id.button_logout);

        mButtonLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginScreen.class));
            Snackbar.make(findViewById(android.R.id.content), "Log Out", Snackbar.LENGTH_SHORT).show();
        });

        init();

    }

    private void init() {
        mButtonBack.setOnClickListener(v -> onBackPressed());
    }
}