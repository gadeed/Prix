package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.adapters.TransferAdapter;
import com.flexural.developers.prixapp.model.Transfer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.flexural.developers.prixapp.activity.LoginScreen.BASE_URL;

public class TransferActivity extends AppCompatActivity {
    private String URL = BASE_URL + "getTransferList.php";
    private String URL_WALLET = BASE_URL + "personalInfo.php";

    private CardView mButtonAddFriend, mShareCode;
    private RecyclerView mRecyclerTransfer;

    private List<Transfer> transferList;
    private TransferAdapter transferAdapter;
    private String shopName, walletAccNo, currentMid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        mButtonAddFriend = findViewById(R.id.button_add_friend);
        mShareCode = findViewById(R.id.button_qr_code);
        mRecyclerTransfer = findViewById(R.id.recycler_view);

        mRecyclerTransfer.setLayoutManager(new GridLayoutManager(this, 3));

        transferList = new ArrayList<>();

        receiveIntent();

    }

    private void loadData(String currentMid) {
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String mid = object.getString("mid");

                        if (mid.equals(currentMid)){
                            String midTr = object.getString("mid_tr");

                            Transfer transfer = new Transfer(currentMid, midTr);
                            transferList.add(transfer);

                            getShopName(midTr);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransferActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(TransferActivity.this).add(request);
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        String shopName = intent.getStringExtra("shopName");
        currentMid = intent.getStringExtra("mid");

        loadData(currentMid);
//        Toast.makeText(this, currentMid, Toast.LENGTH_SHORT).show();

        mButtonAddFriend.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, AddFriendActivity.class);
            intent1.putExtra("mid", currentMid);
            startActivity(intent1);
        });

        mShareCode.setOnClickListener(v -> {
            Intent sendIntent = new Intent(this, ExtraActivity.class);
            sendIntent.putExtra("extra", "qr_code");
            sendIntent.putExtra("shopName", shopName);
            sendIntent.putExtra("mid", currentMid);
            startActivity(sendIntent);
        });
    }

    private void getShopName(String midTr) {
        StringRequest request = new StringRequest(Request.Method.GET, URL_WALLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        walletAccNo = object.getString("id");

                        if (midTr.equals(walletAccNo)){
                            shopName = object.getString("shop_name");

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                transferAdapter = new TransferAdapter(TransferActivity.this, transferList, shopName, midTr, currentMid);
                mRecyclerTransfer.setAdapter(transferAdapter);
                transferAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransferActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(TransferActivity.this).add(request);
    }

}