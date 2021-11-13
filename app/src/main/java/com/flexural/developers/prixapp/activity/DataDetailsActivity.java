package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flexural.developers.prixapp.R;

public class DataDetailsActivity extends AppCompatActivity {

    private TextView mHeader, mPrice, mNetwork;
    private RelativeLayout mBackground;
    private Button mButtonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_details);

        mHeader = findViewById(R.id.header);
        mPrice = findViewById(R.id.price);
        mNetwork = findViewById(R.id.network);
        mBackground = findViewById(R.id.background);
        mButtonCancel = findViewById(R.id.button_cancel);

        init();
        receiveIntent();

    }

    private void init() {
        mButtonCancel.setOnClickListener(v -> onBackPressed());
    }

    private void receiveIntent() {
        String dataPrice = getIntent().getStringExtra("dataPrice");
        String network = getIntent().getStringExtra("network");
        String description = getIntent().getStringExtra("description");
        String dataAmount = getIntent().getStringExtra("dataAmount");

        mPrice.setText(dataPrice);
        mHeader.setText(dataAmount + " " + description + " " + dataPrice);

        if (network.equals("vodacom")) {
            mBackground.setBackgroundColor(Color.RED);
            mNetwork.setText("Vodacom");

        } else if (network.equals("telkom")) {
            mBackground.setBackgroundColor(Color.BLUE);
            mNetwork.setText("Telkom");

        } else if (network.equals("cellc")) {
            mBackground.setBackgroundColor(Color.BLACK);
            mNetwork.setText("Cell C");

        }

    }
}