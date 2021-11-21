package com.flexural.developers.prixapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

    private String shopCode = "", currentDate;
    private ProgressDialog progressDialog;

    private String URL = BASE_URL + "addTransferList.php";

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
        String accNo = getIntent().getStringExtra("mid");

        mCode.setText(accNo);

        mAddShopCode.setOnClickListener(v -> {
            shopCode = mShopCode.getText().toString().trim();

            if (!shopCode.equals("") && !accNo.equals("")) {
                if (shopCode.equals(accNo)) {
                    Toast.makeText(this, "You Cannot Add Yourself as a Friend", Toast.LENGTH_SHORT).show();

                } else {
                    progressDialog.show();

                    StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                progressDialog.dismiss();
                                onBackPressed();

                            } else if (response.equals("failure")) {
                                progressDialog.dismiss();
                                Snackbar.make(findViewById(android.R.id.content), response, Snackbar.LENGTH_SHORT).show();

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
                            data.put("accno", accNo);
                            data.put("accnoTr", shopCode);
                            data.put("created", currentDate);
                            return data;

                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(request);
                }

            }  else {
                Toast.makeText(this, "Please Enter the Shop Code", Toast.LENGTH_SHORT).show();
            }
        });

    }

}