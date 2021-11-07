package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.adapters.AirtimeAdapter;
import com.flexural.developers.prixapp.model.Airtime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AirtimeActivity extends AppCompatActivity {

    private RecyclerView mAirtimeRecycler;
    private List<Airtime> airtimeList;
    private AirtimeAdapter airtimeAdapter;

    private String URL = "http://192.168.137.1/prix/airtimeList.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airtime);

        mAirtimeRecycler = findViewById(R.id.recycler_view);

        mAirtimeRecycler.setLayoutManager(new LinearLayoutManager(this));

        airtimeList = new ArrayList<>();

        receiveIntent();

    }

    private void receiveIntent() {
        Intent intent = getIntent();
        String selectedAirtime = intent.getStringExtra("airtime");

        getAirtime(selectedAirtime);


    }

    private void getAirtime(String selectedAirtime) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        String prod_id = object.getString("prod_id");
                        String pin_no = object.getString("pin_no");
                        String serial_no = object.getString("serial_no");
                        String status = object.getString("status");
                        String expired_date = object.getString("expired_date");

                        if (prod_id.equals(selectedAirtime) && status.equals("Available")) {
                            Airtime airtime = new Airtime(prod_id, pin_no, serial_no, status, expired_date);
                            airtimeList.add(airtime);

                            showDialog(selectedAirtime);

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                airtimeAdapter = new AirtimeAdapter(AirtimeActivity.this, airtimeList);
                mAirtimeRecycler.setAdapter(airtimeAdapter);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AirtimeActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(AirtimeActivity.this).add(stringRequest);
    }

    private void showDialog(String selectedAirtime) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_airtime);

        TextView mButtonCancel = dialog.findViewById(R.id.button_cancel);
        TextView mButtonSell = dialog.findViewById(R.id.button_sell);
        TextView mAirtimeAmount = dialog.findViewById(R.id.airtime_amount);

        mAirtimeAmount.setText("R" + selectedAirtime);

        mButtonCancel.setOnClickListener(v1 -> {
            dialog.dismiss();
            onBackPressed();
            finish();
        });

        mButtonSell.setOnClickListener(v1 -> {
            dialog.dismiss();
            startActivity(new Intent(AirtimeActivity.this, TestActivity.class));
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

}