package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flexural.developers.prixapp.R;

public class RemitMoneyActivity extends AppCompatActivity {

    private ImageView mButtonBack;
    private LinearLayout mButtonMamaMoney, mButtonHelloPaisa, mButtonEcoCash, mButtonRemitMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remit_money);

        mButtonBack = findViewById(R.id.button_back);
        mButtonMamaMoney = findViewById(R.id.button_mama_money);
        mButtonEcoCash = findViewById(R.id.button_ecocash);
        mButtonHelloPaisa = findViewById(R.id.button_hello_paisa);
        mButtonRemitMoney = findViewById(R.id.button_remit_money);

        init();

    }

    private void init() {
        mButtonBack.setOnClickListener(v -> onBackPressed());

        mButtonMamaMoney.setOnClickListener(v -> {
            startActivity(new Intent(this, ReferenceActivity.class));
        });

        mButtonEcoCash.setOnClickListener(v -> {
            startActivity(new Intent(this, ReferenceActivity.class));
        });

        mButtonHelloPaisa.setOnClickListener(v -> {
            startActivity(new Intent(this, ReferenceActivity.class));
        });

        mButtonRemitMoney.setOnClickListener(v -> {
            startActivity(new Intent(this, ReferenceActivity.class));
        });

    }
}