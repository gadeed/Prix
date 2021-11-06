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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    private LinearLayout mEditProfile, mButtonDevice, mButtonTopup, mButtonTransfer, mButtonSales;
    private LinearLayout mButtonHelpCenter;
    private ImageView mButtonDashboard, mButtonSettings;
    private TextView mUsername;

    private String URL = "http://10.198.75.11/prix/personalInfo.php";


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

        init();
        getData();

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

        mButtonTopup.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("menu", "topup");
            startActivity(intent);
        });

        mButtonDashboard.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        mButtonSettings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });

        mButtonTransfer.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("menu", "transfer");
            startActivity(intent);
        });

        mButtonSales.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("menu", "sales");
            startActivity(intent);
        });

        mButtonHelpCenter.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("menu", "help_center");
            startActivity(intent);
        });

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

                        mUsername.setText(firstName + " " + lastName);

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

}