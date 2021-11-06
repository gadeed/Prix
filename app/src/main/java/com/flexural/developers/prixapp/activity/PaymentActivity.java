package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flexural.developers.prixapp.R;

import java.sql.Ref;

public class PaymentActivity extends AppCompatActivity {

    private ImageView mButtonBack;
    private LinearLayout mButtonLottoStar, mButtonHollywoodBets, mButtonDstv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mButtonBack = findViewById(R.id.button_back);
        mButtonLottoStar = findViewById(R.id.button_lotto_star);
        mButtonHollywoodBets = findViewById(R.id.button_hollywood_bets);
        mButtonDstv = findViewById(R.id.button_dstv);

        init();

    }

    private void init() {
        mButtonBack.setOnClickListener(v -> onBackPressed());

        mButtonLottoStar.setOnClickListener(v -> {
            startActivity(new Intent(this, ReferenceActivity.class));
        });

        mButtonHollywoodBets.setOnClickListener(v -> {
            startActivity(new Intent(this, ReferenceActivity.class));
        });

        mButtonDstv.setOnClickListener(v -> {
            startActivity(new Intent(this, ReferenceActivity.class));
        });
    }
}