package com.flexural.developers.prixapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flexural.developers.prixapp.MainActivity;
import com.flexural.developers.prixapp.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.flexural.developers.prixapp.activity.LoginScreen.BASE_URL;

public class AddFriendActivity extends AppCompatActivity {

    private ImageView mButtonBack;
    private RelativeLayout mAddShopCode;
    private EditText mShopCode;
    private TextView mCode;

    private String shopCode = "", currentDate, currentMid;
    private ProgressDialog progressDialog;

    private String URL = BASE_URL + "addTransferList.php";
    private String URL_WALLET = BASE_URL + "merchantWallet.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        mButtonBack = findViewById(R.id.button_back);
        mAddShopCode = findViewById(R.id.add_friend);
        mShopCode = findViewById(R.id.shop_code);
        mCode = findViewById(R.id.my_code);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Friend...");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDate = df.format(Calendar.getInstance().getTime());

        init();
        receiveIntent();

    }

    private void init() {
        mButtonBack.setOnClickListener(v -> onBackPressed());

    }

    private void receiveIntent() {
        currentMid = getIntent().getStringExtra("mid");

        getAccNo(currentMid);

        mAddShopCode.setOnClickListener(v -> {
            shopCode = mShopCode.getText().toString().trim();

            if (!shopCode.equals("") && !currentMid.equals("")) {
                if (shopCode.equals(currentMid)) {
                    Toast.makeText(this, "You Cannot Add Yourself as a Friend", Toast.LENGTH_SHORT).show();

                } else {
                    progressDialog.show();
                    getMid(shopCode);
                }

            }  else {
                Toast.makeText(this, "Please Enter the Shop Code", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getAccNo(String currentMid){
        StringRequest request = new StringRequest(Request.Method.GET, URL_WALLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String mid = object.getString("mid");

                        if (mid.equals(currentMid)) {
                            String accNo = object.getString("acc_no");
                            mCode.setText(accNo);

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddFriendActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(AddFriendActivity.this).add(request);
    }

    private void getMid(String currentAccNo){
        StringRequest request = new StringRequest(Request.Method.GET, URL_WALLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String accNo = object.getString("acc_no");

                        if (accNo.equals(currentAccNo)) {
                            String mid = object.getString("mid");
                            postInfo(mid);

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddFriendActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(AddFriendActivity.this).add(request);
    }

    private void postInfo(String mid) {
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    progressDialog.dismiss();
                    onBackPressed();
                    Toast.makeText(AddFriendActivity.this, "Friend Successfully Added to List", Toast.LENGTH_SHORT).show();

                } else if (response.equals("failure")) {
                    progressDialog.dismiss();
                    Snackbar.make(findViewById(android.R.id.content), response.toString(), Snackbar.LENGTH_INDEFINITE).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), error.toString().trim(), Snackbar.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("mid", currentMid);
                data.put("mid_tr", mid);
//                data.put("created", currentDate);
                return data;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

}