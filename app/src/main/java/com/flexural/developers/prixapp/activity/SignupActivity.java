package com.flexural.developers.prixapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flexural.developers.prixapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private RelativeLayout mButtonNext;
    private CheckBox mTermsCondition;
    private EditText mInputPassword, mInputEmail, mReEnterPassword;
    private ImageView mButtonBack;

    private String email, password, reEnterPassword, currentDate;
    private ProgressDialog progressDialog;

    private String URL = "http://10.198.75.11/prix/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mButtonNext = findViewById(R.id.button_next);
        mTermsCondition = findViewById(R.id.checkbox);
        mInputPassword = findViewById(R.id.input_password);
        mInputEmail = findViewById(R.id.input_email);
        mReEnterPassword = findViewById(R.id.re_enter_password);
        mButtonBack = findViewById(R.id.button_back);

        email = password = reEnterPassword = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDate = df.format(Calendar.getInstance().getTime());
        progressDialog = new ProgressDialog(this);


        init();

    }

    private void init() {
        mButtonNext.setOnClickListener(v -> {
            email = mInputEmail.getText().toString().trim();
            password = mInputPassword.getText().toString().trim();
            reEnterPassword = mReEnterPassword.getText().toString().trim();

            if (!password.equals(reEnterPassword)) {
                Snackbar.make(findViewById(android.R.id.content), "Password Mismatch", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

            } else if (!email.equals("") && !password.equals("") && !reEnterPassword.equals("")) {
                String name = getIntent().getStringExtra("name");
                String surname = getIntent().getStringExtra("surname");
                String phone = getIntent().getStringExtra("phone");
                String shopName = getIntent().getStringExtra("shopName");
                String address = getIntent().getStringExtra("address");
                String city = getIntent().getStringExtra("city");

                if (!name.equals("") && !surname.equals("") && !phone.equals("") && !shopName.equals("")
                        && !address.equals("") && !city.equals("")) {

                    progressDialog.setMessage("Uploading information...");
                    progressDialog.show();

                    StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                progressDialog.dismiss();
                                startActivity(new Intent(SignupActivity.this, ConfirmationActivity.class));
                                finish();

                            } else if (response.equals("failure")) {
                                progressDialog.dismiss();
                                Snackbar.make(findViewById(android.R.id.content), "Something went wrong!!", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("OK", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        });

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
                            data.put("agent_id", "1");
                            data.put("shop_name", shopName);
                            data.put("address", address);
                            data.put("first_name", name);
                            data.put("last_name", surname);
                            data.put("city", city);
                            data.put("phone", phone);
                            data.put("email", email);
                            data.put("password", password);
                            data.put("created", currentDate);
                            data.put("updated", currentDate);
                            return data;

                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(request);

                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Something went wrong. Start Again!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(SignupActivity.this, DetailsActivity.class));
                                    finish();
                                }
                            });
                }

            }

        });

        mButtonBack.setOnClickListener(v -> onBackPressed());

    }

    private boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    @Override
    protected void onStart() {
        super.onStart();

//

    }
}