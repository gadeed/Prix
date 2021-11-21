package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flexural.developers.prixapp.MainActivity;
import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.model.Sales;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.flexural.developers.prixapp.activity.LoginScreen.BASE_URL;

public class ProfileActivity extends AppCompatActivity {
    private LinearLayout mEditProfile, mButtonDevice, mButtonTopup, mButtonTransfer, mButtonSales;
    private LinearLayout mButtonHelpCenter, mButtonStatement;
    private ImageView mButtonDashboard, mButtonSettings;
    private TextView mUsername, mMerchantNumber;

    private String URL = BASE_URL + "personalInfo.php";
    private String URL_WALLET = BASE_URL + "merchantWallet.php";

    private String shopName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mEditProfile = findViewById(R.id.edit_profile);
        mButtonDevice = findViewById(R.id.button_device);
        mButtonTopup = findViewById(R.id.button_topup);
        mButtonDashboard = findViewById(R.id.button_dashboard);
        mButtonSettings = findViewById(R.id.button_setting);
        mButtonTransfer = findViewById(R.id.button_transfer);
        mButtonSales = findViewById(R.id.button_sales);
        mButtonHelpCenter = findViewById(R.id.button_help_center);

        mUsername = findViewById(R.id.user_name);
        mMerchantNumber = findViewById(R.id.merchant_number);
        mButtonStatement = findViewById(R.id.button_statements);

        init();
        getData();

    }

    private void getData(){
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String firstName = object.getString("first_name");
                        String lastName = object.getString("last_name");
                        shopName = object.getString("shop_name");

                        mUsername.setText(shopName);

                        StringRequest request = new StringRequest(Request.Method.GET, URL_WALLET, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray array = new JSONArray(response);
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        String acc_no = object.getString("acc_no");

                                        mMerchantNumber.setText(acc_no);

                                        mButtonTopup.setOnClickListener(v -> {
                                            Intent intent = new Intent(ProfileActivity.this, MenuActivity.class);
                                            intent.putExtra("menu", "topup");
                                            intent.putExtra("shopName", shopName);
                                            intent.putExtra("mid", acc_no);
                                            startActivity(intent);
                                        });

                                        mButtonTransfer.setOnClickListener(v -> {
                                            Intent intent = new Intent(ProfileActivity.this, TransferActivity.class);
                                            intent.putExtra("menu", "transfer");
                                            intent.putExtra("shopName", shopName);
                                            intent.putExtra("mid", acc_no);
                                            startActivity(intent);
                                        });

                                        mButtonStatement.setOnClickListener(v -> {
                                            Intent intent = new Intent(ProfileActivity.this, MenuActivity.class);
                                            intent.putExtra("menu", "statement");
                                            intent.putExtra("shopName", shopName);
                                            intent.putExtra("mid", acc_no);
                                            startActivity(intent);
                                        });

                                        mButtonSales.setOnClickListener(v -> {
                                            Intent intent = new Intent(ProfileActivity.this, SalesActivity.class);
                                            intent.putExtra("shopName", shopName);
                                            startActivity(intent);
                                        });

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ProfileActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                        Volley.newRequestQueue(ProfileActivity.this).add(request);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ProfileActivity.this).add(request);
    }

    private void init() {
        mEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, EditProfileActivity.class));
        });

        mButtonDevice.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("menu", "device");
            startActivity(intent);
        });

        mButtonDashboard.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        mButtonSettings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });

        mButtonHelpCenter.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("menu", "help_center");
            startActivity(intent);
        });

    }


}