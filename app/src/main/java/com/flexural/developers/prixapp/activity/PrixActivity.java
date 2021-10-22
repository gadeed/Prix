package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flexural.developers.prixapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;

public class PrixActivity extends AppCompatActivity {

    private ImageView mTitle, mDropDown, mNetworkLogo;
    private LinearLayout mButtonMtn, mButtonPrix, mButtonVodacom;
    private TextView mNetworkTitle;
    private SingleSelectToggleGroup mPrixAirtime, mMtnAirtime, mVodacomAirtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prix);

        mTitle = findViewById(R.id.title);
        mDropDown = findViewById(R.id.drop_down);
        mNetworkTitle = findViewById(R.id.network_title);
        mNetworkLogo = findViewById(R.id.network_logo);
        mPrixAirtime = findViewById(R.id.prix_airtime);
        mMtnAirtime = findViewById(R.id.mtn_airtime);
        mVodacomAirtime = findViewById(R.id.vodacom_airtime);

        init();
        receiveIntent();

    }

    private void init(){
        mDropDown.setOnClickListener(v -> {
            showDialog();

        });
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog);

        mButtonPrix = dialog.findViewById(R.id.button_prix);
        mButtonMtn = dialog.findViewById(R.id.button_mtn);
        mButtonVodacom = dialog.findViewById(R.id.button_vodacom);

        mButtonPrix.setOnClickListener(v -> {
            Glide.with(PrixActivity.this).load(R.drawable.logo).into(mNetworkLogo);
            mNetworkTitle.setText("Prix");
            mPrixAirtime.setVisibility(View.VISIBLE);
            mMtnAirtime.setVisibility(View.GONE);
            mVodacomAirtime.setVisibility(View.GONE);

            dialog.dismiss();

        });

        mButtonMtn.setOnClickListener(v -> {
            Glide.with(PrixActivity.this).load(R.drawable.ic_mtn_logo).into(mNetworkLogo);
            mNetworkTitle.setText("MTN");
            mMtnAirtime.setVisibility(View.VISIBLE);
            mVodacomAirtime.setVisibility(View.GONE);
            mPrixAirtime.setVisibility(View.GONE);

            dialog.dismiss();

        });

        mButtonVodacom.setOnClickListener(v -> {
            Glide.with(PrixActivity.this).load(R.drawable.vodacom).into(mNetworkLogo);
            mNetworkTitle.setText("Vodacom");
            mVodacomAirtime.setVisibility(View.VISIBLE);
            mMtnAirtime.setVisibility(View.GONE);
            mPrixAirtime.setVisibility(View.GONE);

            dialog.dismiss();

        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        if (title.equals("unipin")) {
            Glide.with(this).load(R.drawable.ic_unipin_title).into(mTitle);

        } else if (title.equals("prix")){
            mDropDown.setVisibility(View.INVISIBLE);
            Glide.with(PrixActivity.this).load(R.drawable.logo).into(mNetworkLogo);

        } else if (title.equals("network")) {
            mVodacomAirtime.setVisibility(View.VISIBLE);
        } else {

        }
    }
}