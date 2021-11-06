package com.flexural.developers.prixapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.model.User;
import com.flexural.developers.prixapp.utils.LocationAddress;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

public class DetailsActivity extends AppCompatActivity {

    private RelativeLayout mButtonNext;
    private EditText mInputName, mInputSurname, mPhoneNumber;
    private ImageView mButtonBack;

    private String first_name, last_name, phone;

//    private String URL = "http://10.198.75.11/prix/personalInfo.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mButtonNext = findViewById(R.id.button_next);
        mPhoneNumber = findViewById(R.id.phone_number);
        mInputName = findViewById(R.id.input_name);
        mInputSurname = findViewById(R.id.input_surname);

        mButtonBack = findViewById(R.id.button_back);

        first_name = last_name = phone = "";

        init();



    }

    private void init() {
        mButtonNext.setOnClickListener(v -> {
            first_name = mInputName.getText().toString().trim();
            last_name = mInputSurname.getText().toString().trim();
            phone = mPhoneNumber.getText().toString().trim();

            if (!first_name.equals("") && !last_name.equals("") && !phone.equals("")) {

                Intent intent = new Intent(DetailsActivity.this, CodeActivity.class);
                intent.putExtra("name", first_name);
                intent.putExtra("surname", last_name);
                intent.putExtra("phone", phone);
                startActivity(intent);
//                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if (response.equals("success")) {
//                            progressDialog.dismiss();
//                            Toast.makeText(DetailsActivity.this, "Personal Information Successfully Added", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(DetailsActivity.this, CodeActivity.class));
//                            finish();
//
//                        } else if (response.equals("failure")) {
//                            progressDialog.dismiss();
//                            Snackbar.make(findViewById(android.R.id.content), "Something went wrong!!", Snackbar.LENGTH_INDEFINITE)
//                                    .setAction("OK", new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//
//                                        }
//                                    });
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        Snackbar.make(findViewById(android.R.id.content), error.toString().trim(), Snackbar.LENGTH_SHORT).show();
//
//                    }
//                }){
//                    @Nullable
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> data = new HashMap<>();
//                        data.put("agent_id", "");
//                        data.put("shop_name", "");
//                        data.put("address", "");
//                        data.put("first_name", first_name);
//                        data.put("last_name", last_name);
//                        data.put("city", "");
//                        data.put("phone", phone);
//                        data.put("email", "");
//                        data.put("password", "");
//                        data.put("created", "");
//                        data.put("updated", "");
//
//                        return data;
//
//                    }
//                };
//                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//                queue.add(request);

            } else if (mInputName.length() == 0) {
                Toast.makeText(this, "Please Enter First Name", Toast.LENGTH_SHORT).show();

            } else if (mInputSurname.length() == 0) {
                Toast.makeText(this, "Please Enter Last Name", Toast.LENGTH_SHORT).show();

            } else if (mPhoneNumber.length() == 0) {
                Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();

            }
        });

        mButtonBack.setOnClickListener(v -> onBackPressed());
    }



}