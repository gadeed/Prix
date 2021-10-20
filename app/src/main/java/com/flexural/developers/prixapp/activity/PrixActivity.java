package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.flexural.developers.prixapp.R;

public class PrixActivity extends AppCompatActivity {

    private ImageView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prix);

        mTitle = findViewById(R.id.title);

        receiveIntent();

    }

    private void receiveIntent() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        if (title.equals("unipin")) {
            Glide.with(this).load(R.drawable.ic_unipin_title).into(mTitle);

        } else {

        }
    }
}