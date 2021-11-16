package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.adapters.DataAdapter;
import com.flexural.developers.prixapp.adapters.ElectricityAdapter;
import com.flexural.developers.prixapp.model.Data;
import com.flexural.developers.prixapp.model.Electricity;

import java.util.ArrayList;
import java.util.List;

public class ElectricityActivity extends AppCompatActivity {

    private ImageView mButtonBack;
    private RecyclerView mRecyclerElectricity;

    private ElectricityAdapter electricityAdapter;
    private List<Electricity> electricityList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity);

        mButtonBack = findViewById(R.id.button_back);
        mRecyclerElectricity = findViewById(R.id.recycler_view);

        mRecyclerElectricity.setLayoutManager(new GridLayoutManager(this, 3));

        electricityList = new ArrayList<>();
        electricityAdapter = new ElectricityAdapter(this, electricityList);

        init();
        loadData();

    }

    private void init() {
        mButtonBack.setOnClickListener(v -> onBackPressed());

    }

    private void loadData() {
        electricityList.add(new Electricity("R20"));
        electricityList.add(new Electricity("R30"));
        electricityList.add(new Electricity("R50"));
        electricityList.add(new Electricity("R100"));
        electricityList.add(new Electricity("R200"));
        electricityList.add(new Electricity("R250"));
        electricityList.add(new Electricity("R300"));
        electricityList.add(new Electricity("R500"));
        electricityList.add(new Electricity("R1000"));

        mRecyclerElectricity.setAdapter(electricityAdapter);
        electricityAdapter.notifyDataSetChanged();

    }
}