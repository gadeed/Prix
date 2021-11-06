package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flexural.developers.prixapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;

public class PrixActivity extends AppCompatActivity {

    private ImageView mTitle, mDropDown, mNetworkLogo, mButtonBack;
    private LinearLayout mButtonMtn, mButtonPrix, mButtonVodacom, mNetworkContainer, mButtonCellC, mButtonTelkom;
    private TextView mNetworkTitle, mSelectNetwork;
    private SingleSelectToggleGroup mPrixAirtime, mMtnAirtime, mVodacomAirtime, mCellCAirtime, mTelkomAirtime;
    private RelativeLayout mButtonBuy;

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
        mSelectNetwork = findViewById(R.id.select_network);

        mNetworkContainer = findViewById(R.id.network_container);
        mButtonBuy = findViewById(R.id.button_buy);
        mCellCAirtime = findViewById(R.id.cellc_airtime);
        mTelkomAirtime = findViewById(R.id.telkom_airtime);
        mButtonBack = findViewById(R.id.button_back);

        init();
        receiveIntent();

    }

    private void init(){
        mNetworkContainer.setOnClickListener(v -> {
            showDialog();

        });

        mButtonBuy.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_dialog_airtime);

            TextView mButtonCancel = dialog.findViewById(R.id.button_cancel);
            TextView mButtonSell = dialog.findViewById(R.id.button_sell);

            mButtonCancel.setOnClickListener(v1 -> dialog.dismiss());

            mButtonSell.setOnClickListener(v1 -> {
                startActivity(new Intent(PrixActivity.this, TestActivity.class));
            });

            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.CENTER);

        });

        mButtonBack.setOnClickListener(v -> onBackPressed());

    }

    private void printReceipt() {
        Toast.makeText(this, "Print Receipt", Toast.LENGTH_SHORT).show();
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog);

        mButtonPrix = dialog.findViewById(R.id.button_prix);
        mButtonMtn = dialog.findViewById(R.id.button_mtn);
        mButtonVodacom = dialog.findViewById(R.id.button_vodacom);
        mButtonCellC = dialog.findViewById(R.id.button_cell_c);
        mButtonTelkom = dialog.findViewById(R.id.button_telkom);

        mButtonPrix.setOnClickListener(v -> {
            Glide.with(PrixActivity.this).load(R.drawable.logo).into(mNetworkLogo);
            mNetworkTitle.setText("Prix");
            mPrixAirtime.setVisibility(View.VISIBLE);
            mMtnAirtime.setVisibility(View.GONE);
            mVodacomAirtime.setVisibility(View.GONE);
            mCellCAirtime.setVisibility(View.GONE);
            mTelkomAirtime.setVisibility(View.GONE);

            dialog.dismiss();

        });

        mButtonMtn.setOnClickListener(v -> {
            Glide.with(PrixActivity.this).load(R.drawable.ic_mtn_logo).into(mNetworkLogo);
            mNetworkTitle.setText("MTN");
            mMtnAirtime.setVisibility(View.VISIBLE);
            mVodacomAirtime.setVisibility(View.GONE);
            mPrixAirtime.setVisibility(View.GONE);
            mTelkomAirtime.setVisibility(View.GONE);
            mCellCAirtime.setVisibility(View.GONE);

            dialog.dismiss();

        });

        mButtonVodacom.setOnClickListener(v -> {
            Glide.with(PrixActivity.this).load(R.drawable.vodacom).into(mNetworkLogo);
            mNetworkTitle.setText("Vodacom");
            mVodacomAirtime.setVisibility(View.VISIBLE);
            mMtnAirtime.setVisibility(View.GONE);
            mPrixAirtime.setVisibility(View.GONE);
            mCellCAirtime.setVisibility(View.GONE);
            mTelkomAirtime.setVisibility(View.GONE);

            dialog.dismiss();

        });

        mButtonCellC.setOnClickListener(v -> {
            Glide.with(PrixActivity.this).load(R.drawable.ic_cell_c).into(mNetworkLogo);
            mNetworkTitle.setText("Cell C");
            mCellCAirtime.setVisibility(View.VISIBLE);
            mMtnAirtime.setVisibility(View.GONE);
            mPrixAirtime.setVisibility(View.GONE);
            mVodacomAirtime.setVisibility(View.GONE);
            mTelkomAirtime.setVisibility(View.GONE);

            dialog.dismiss();
        });

        mButtonTelkom.setOnClickListener(v -> {
            Glide.with(PrixActivity.this).load(R.drawable.ic_telkom).into(mNetworkLogo);
            mNetworkTitle.setText("Telkom");
            mTelkomAirtime.setVisibility(View.VISIBLE);
            mMtnAirtime.setVisibility(View.GONE);
            mPrixAirtime.setVisibility(View.GONE);
            mCellCAirtime.setVisibility(View.GONE);
            mVodacomAirtime.setVisibility(View.GONE);

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
            mNetworkContainer.setVisibility(View.GONE);
            mSelectNetwork.setVisibility(View.GONE);

        } else if (title.equals("prix")){
            mDropDown.setVisibility(View.INVISIBLE);
            Glide.with(PrixActivity.this).load(R.drawable.logo).into(mNetworkLogo);
            mPrixAirtime.setVisibility(View.VISIBLE);
            mSelectNetwork.setVisibility(View.GONE);
            mNetworkContainer.setVisibility(View.GONE);

        } else if (title.equals("network")) {
            mVodacomAirtime.setVisibility(View.VISIBLE);
        } else {

        }
    }
}