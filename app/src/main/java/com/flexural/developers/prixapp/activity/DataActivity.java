package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.adapters.DataAdapter;
import com.flexural.developers.prixapp.model.Data;

import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity {

    private RecyclerView mRecyclerVodacom, mRecyclerCellC, mRecyclerTelkom;
    private LinearLayout mButtonMtn, mButtonPrix, mButtonVodacom, mNetworkContainer, mButtonCellC, mButtonTelkom;
    private TextView mNetworkTitle, mSelectNetwork;
    private ImageView mTitle, mDropDown, mNetworkLogo, mButtonBack;
    private RelativeLayout mLayoutVodacom, mLayoutCellc, mLayoutTelkom;

    private DataAdapter dataAdapter;
    private List<Data> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        mRecyclerVodacom = findViewById(R.id.recycler_vodacom);
        mNetworkContainer = findViewById(R.id.network_container);
        mNetworkTitle = findViewById(R.id.network_title);
        mSelectNetwork = findViewById(R.id.select_network);
        mNetworkLogo = findViewById(R.id.network_logo);
        mLayoutVodacom = findViewById(R.id.layout_vodacom);
        mLayoutCellc = findViewById(R.id.layout_cellc);
        mLayoutTelkom = findViewById(R.id.layout_telkom);

        mRecyclerCellC = findViewById(R.id.recycler_cellc);
        mRecyclerTelkom = findViewById(R.id.recycler_telkom);
        mButtonBack = findViewById(R.id.button_back);

        mRecyclerVodacom.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerTelkom.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerCellC.setLayoutManager(new GridLayoutManager(this, 3));


        init();
        loadVodacom();

    }

    private void init() {
        mNetworkContainer.setOnClickListener(v -> {
            showDialog();
        });

        mButtonBack.setOnClickListener(v -> onBackPressed());

    }

    private void loadVodacom() {
        dataList = new ArrayList<>();
        dataAdapter = new DataAdapter(this, dataList, "vodacom");

        dataList.add(new Data("R2", "10min", "Power Bundle"));
        dataList.add(new Data("R5", "60min", "Power Bundle"));
        dataList.add(new Data("R5", "50MB", "Power Bundle"));
        dataList.add(new Data("R5", "20MB", "Daily"));
        dataList.add(new Data("R9", "60MB", "Daily"));
        dataList.add(new Data("R12", "50MB", ""));
        dataList.add(new Data("R15", "100MB", "Daily"));
        dataList.add(new Data("R17", "120MB", "Weekly"));
        dataList.add(new Data("R29", "250MB", "Weekly"));
        dataList.add(new Data("R29", "200MB", "Monthly MyMeg"));
        dataList.add(new Data("R49", "350MB", "Monthly MyMeg"));
        dataList.add(new Data("R49", "", "SMS Voucher  "));
        dataList.add(new Data("R49", "500MB", "Weekly"));
        dataList.add(new Data("R69", "500MB", "Monthly MyMeg"));
        dataList.add(new Data("R69", "1GB", "Weekly"));
        dataList.add(new Data("R85", "1GB", "Monthly MyGig"));
        dataList.add(new Data("R99", "2GB", "Weekly"));
        dataList.add(new Data("R149", "2GB", "MyGig"));
        dataList.add(new Data("R199", "5GB", "Weekly"));
        dataList.add(new Data("R229", "3GB", "MyMeg"));
        dataList.add(new Data("R249", "4GB", "MyGig"));
        dataList.add(new Data("R349", "6GB", "MyGig"));
        dataList.add(new Data("R469", "10GB", "MyMeg"));
        dataList.add(new Data("R529", "15GB", "MyGig"));
        dataList.add(new Data("R699", "30GB", "MyGig"));

        mRecyclerVodacom.setAdapter(dataAdapter);
    }

    private void loadCellC() {
        dataList = new ArrayList<>();
        dataAdapter = new DataAdapter(this, dataList, "cellc");

        dataList.add(new Data("R4", "25MB", "OneDay"));
        dataList.add(new Data("R5", "30", "All in One"));
        dataList.add(new Data("R9", "65MB", "OneDay"));
        dataList.add(new Data("R10", "40MB", "30 Days"));
        dataList.add(new Data("R10", "300MB", "WhatsApp 7 Day"));
        dataList.add(new Data("R10", "60MB", "7 Day"));
        dataList.add(new Data("R14", "120MB", "OneDay"));
        dataList.add(new Data("R15", "80MB", ""));
        dataList.add(new Data("R15", "150MB", "7 Day"));
        dataList.add(new Data("R15", "120", "All in One"));
        dataList.add(new Data("R17", "500MB", "OneDay"));
        dataList.add(new Data("R20", "1GB", "OneDay"));
        dataList.add(new Data("R20", "600MB", "WhatsApp 15 Day"));
        dataList.add(new Data("R25", "250MB", "7 Day"));
        dataList.add(new Data("R29", "1GB", "WhatsApp 30 Day"));
        dataList.add(new Data("R29", "150MB", ""));
        dataList.add(new Data("R35", "250MB", ""));
        dataList.add(new Data("R35", "300", "All in One"));
        dataList.add(new Data("R39", "100", "SMS"));
        dataList.add(new Data("R45", "500MB", "7 Days"));
        dataList.add(new Data("R49", "325MB", ""));
        dataList.add(new Data("R65", "1GB", "7 Days"));
        dataList.add(new Data("R80", "800MB", ""));
        dataList.add(new Data("R95", "2GB", "7 Days"));
        dataList.add(new Data("R95", "1GB", "All in One"));
        dataList.add(new Data("R95", "1GB", ""));
        dataList.add(new Data("R149", "5GB", ""));
        dataList.add(new Data("R195", "2.5GB", "All in One"));
        dataList.add(new Data("R299", "3GB", ""));
        dataList.add(new Data("R999", "", "Infinity Prepaid"));

        mRecyclerCellC.setAdapter(dataAdapter);

    }

    private void loadTelkom() {
        dataList = new ArrayList<>();
        dataAdapter = new DataAdapter(this, dataList, "telkom");

        dataList.add(new Data("R5", "7MB", "Hourly"));
        dataList.add(new Data("R7", "35MB + 35MB", "6 Months"));
        dataList.add(new Data("R10", "100MB", "Weekend"));
        dataList.add(new Data("R10.50", "150MB", "Daily"));
        dataList.add(new Data("R14", "75MB", "6 Months"));
        dataList.add(new Data("R19", "200MB", "Weekend"));
        dataList.add(new Data("R29", "150MB + 150MB", "6 Months"));
        dataList.add(new Data("R29", "300MB", "FreeMeBoost"));
        dataList.add(new Data("R29", "500MB", "Weekend"));
        dataList.add(new Data("R39", "500MB", "FreeMeBoost"));
        dataList.add(new Data("R49", "300MB + 300MB", "6 Months"));
        dataList.add(new Data("R49", "3GB + 3GB", "Fourteen Day LTE On-net"));
        dataList.add(new Data("R49", "1GB", "Weekend"));
        dataList.add(new Data("R69", "500MB + 500MB", "6 Months"));
        dataList.add(new Data("R79", "1GB + 1GB", "2 Months"));
        dataList.add(new Data("R89", "1.5GB + 1.5GB", "2 Months"));
        dataList.add(new Data("R99", "800MB", "FreeMeBoost"));
        dataList.add(new Data("R99", "7.5GB + 7.5GB", ""));
        dataList.add(new Data("R139", "1.5GB", "FreeMeBoost"));
        dataList.add(new Data("R139", "2GB + 2GB", "2 Months"));
        dataList.add(new Data("R149", "10GB + 10GB", "LTE"));
        dataList.add(new Data("R189", "3GB", "FreeMeBoost"));
        dataList.add(new Data("R199", "3GB + 3GB", "2 Months"));
        dataList.add(new Data("R249", "20GB + 20GB", "LTE"));
        dataList.add(new Data("R289", "6GB", "FreeMeBoost"));
        dataList.add(new Data("R299", "5GB +5GB", "2 Months"));
        dataList.add(new Data("R359", "40GB + 40GB", "LTE"));
        dataList.add(new Data("R389", "11.5GB", "FreeMeBoost"));
        dataList.add(new Data("R459", "60GB + 60GB", "LTE"));
        dataList.add(new Data("R469", "10GB + 10GB", "2 Months"));
        dataList.add(new Data("R559", "80GB + 80GB", "FreeMeBoost"));
        dataList.add(new Data("R589", "18GB", "FreeMeBoost"));
        dataList.add(new Data("R689", "28GB", "FreeMeBoost"));
        dataList.add(new Data("R699", "20GB", "6 Months"));
        dataList.add(new Data("R759", "120GB + 120GB", "LTE"));
        dataList.add(new Data("R1059", "220GB + 220GB", "LTE"));
        dataList.add(new Data("R1499", "50GB", "6 Months"));
        dataList.add(new Data("R2499", "100GB", "12 Months"));

        mRecyclerTelkom.setAdapter(dataAdapter);

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

        mButtonPrix.setVisibility(View.GONE);
        mButtonMtn.setVisibility(View.GONE);

        mButtonVodacom.setOnClickListener(v -> {
            Glide.with(DataActivity.this).load(R.drawable.vodacom).into(mNetworkLogo);
            mNetworkTitle.setText("Vodacom");

            mLayoutVodacom.setVisibility(View.VISIBLE);
            mLayoutTelkom.setVisibility(View.GONE);
            mLayoutCellc.setVisibility(View.GONE);

            mRecyclerVodacom.setVisibility(View.VISIBLE);
            mRecyclerTelkom.setVisibility(View.GONE);
            mRecyclerCellC.setVisibility(View.GONE);

            loadVodacom();

            dialog.dismiss();

        });

        mButtonCellC.setOnClickListener(v -> {
            Glide.with(DataActivity.this).load(R.drawable.ic_cell_c).into(mNetworkLogo);
            mNetworkTitle.setText("Cell C");

            mLayoutVodacom.setVisibility(View.GONE);
            mLayoutTelkom.setVisibility(View.GONE);
            mLayoutCellc.setVisibility(View.VISIBLE);

            mRecyclerVodacom.setVisibility(View.GONE);
            mRecyclerTelkom.setVisibility(View.GONE);
            mRecyclerCellC.setVisibility(View.VISIBLE);


            loadCellC();

            dialog.dismiss();
        });

        mButtonTelkom.setOnClickListener(v -> {
            Glide.with(DataActivity.this).load(R.drawable.ic_telkom).into(mNetworkLogo);
            mNetworkTitle.setText("Telkom");

            mLayoutVodacom.setVisibility(View.GONE);
            mLayoutTelkom.setVisibility(View.VISIBLE);
            mLayoutCellc.setVisibility(View.GONE);

            mRecyclerVodacom.setVisibility(View.GONE);
            mRecyclerTelkom.setVisibility(View.VISIBLE);
            mRecyclerCellC.setVisibility(View.GONE);

            loadTelkom();

            dialog.dismiss();
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


}