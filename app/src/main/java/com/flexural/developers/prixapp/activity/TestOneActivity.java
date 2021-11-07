package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;

import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.adapters.AirtimeAdapter;
import com.flexural.developers.prixapp.utils.DatabaseList;
import com.flexural.developers.prixapp.utils.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestOneActivity extends AppCompatActivity {

    private RecyclerView mAirtimeRecycler;
    private List<DatabaseList> databaseLists;
    private AirtimeAdapter airtimeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_one);

        mAirtimeRecycler = findViewById(R.id.recycler_view);

        mAirtimeRecycler.setLayoutManager(new LinearLayoutManager(this));

        databaseLists = new ArrayList<>();

        Call<List<DatabaseList>> call = RetrofitClient.getInstance().getApi().getData();
        call.enqueue(new Callback<List<DatabaseList>>() {
            @Override
            public void onResponse(Call<List<DatabaseList>> call, Response<List<DatabaseList>> response) {
                databaseLists = response.body();
                airtimeAdapter = new AirtimeAdapter(TestOneActivity.this, databaseLists);
                mAirtimeRecycler.setAdapter(airtimeAdapter);

            }

            @Override
            public void onFailure(Call<List<DatabaseList>> call, Throwable t) {

            }
        });
    }
}