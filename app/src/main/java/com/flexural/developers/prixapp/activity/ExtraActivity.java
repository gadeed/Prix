package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.flexural.developers.prixapp.R;
import com.google.android.material.snackbar.Snackbar;

public class ExtraActivity extends AppCompatActivity {

    private ImageView mButtonBack;
    private LinearLayout mLayoutTicket;
    private RelativeLayout mButtonTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        mButtonBack = findViewById(R.id.button_back);
        mLayoutTicket = findViewById(R.id.layout_ticket);
        mButtonTicket = findViewById(R.id.button_ticket);

        init();
        receiveIntent();

    }

    private void receiveIntent() {
        Intent intent = getIntent();
        String extra = intent.getStringExtra("extra");

        if (extra.equals("ticket")) {
            mLayoutTicket.setVisibility(View.VISIBLE);

            mButtonTicket.setOnClickListener(v -> {
                Snackbar.make(findViewById(android.R.id.content), "Not connected to mail server", Snackbar.LENGTH_SHORT).show();
            });
        }
    }

    private void init() {
        mButtonBack.setOnClickListener(v -> onBackPressed());
    }
}