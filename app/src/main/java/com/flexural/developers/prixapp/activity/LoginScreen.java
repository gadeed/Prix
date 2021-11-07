package com.flexural.developers.prixapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity {

    private ImageView mButtonClose;
    private EditText mInputEmail, mInputPassword;
    private RelativeLayout mButtonSignIn, mButtonSignUp;

    private ProgressDialog progressDialog;
    private String email, password;

    private String URL = "http://192.168.137.1/prix/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mButtonClose = findViewById(R.id.button_close);
        mButtonSignIn = findViewById(R.id.button_sign_in);
        mButtonSignUp = findViewById(R.id.button_sign_up);
        mInputEmail = findViewById(R.id.input_email);
        mInputPassword = findViewById(R.id.input_password);

        email = password = "";

        progressDialog = new ProgressDialog(LoginScreen.this);
        progressDialog.setMessage("Login...");

        init();

    }

    private void init() {
        mButtonClose.setOnClickListener(v -> {
            onBackPressed();
        });

        mButtonSignIn.setOnClickListener(v -> {
            email = mInputEmail.getText().toString().trim();
            password = mInputPassword.getText().toString().trim();

            if (!email.equals("") && !password.equals("")) {
                progressDialog.show();

                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else if (response.equals("failure")) {
                            progressDialog.dismiss();
                            Snackbar.make(findViewById(android.R.id.content), "Invalid Login Id/Password", Snackbar.LENGTH_SHORT).show();

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
                        data.put("email", email);
                        data.put("password", password);
                        return data;

                    }
                };
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(request);

            } else if (mInputEmail.length() == 0){
                Snackbar.make(findViewById(android.R.id.content), "Invalid Phone Number", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
            } else if (mInputPassword.length() == 0) {
                Snackbar.make(findViewById(android.R.id.content), "Invalid Password", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
            }

        });

        mButtonSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this, RegistrationActivity.class));

        });
    }

}