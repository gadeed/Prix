package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.flexural.developers.prixapp.R;

public class AddFriendActivity extends AppCompatActivity {

    private ImageView mButtonBack;
    private RelativeLayout mAddShopCode;
    private EditText mShopCode;

    private String shopCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        mButtonBack = findViewById(R.id.button_back);
        mAddShopCode = findViewById(R.id.add_friend);
        mShopCode = findViewById(R.id.shop_code);

        init();

    }

    private void init() {
        mButtonBack.setOnClickListener(v -> onBackPressed());

        mAddShopCode.setOnClickListener(v -> {
            shopCode = mShopCode.getText().toString().trim();

            if (!shopCode.equals("")) {

            } else {
                Toast.makeText(this, "Please Enter the Shop Code", Toast.LENGTH_SHORT).show();
            }
        });
    }

}