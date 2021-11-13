package com.flexural.developers.prixapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.flexural.developers.prixapp.activity.LoginScreen.BASE_URL;

public class SignupActivity extends AppCompatActivity {

    private final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private RelativeLayout mButtonNext;
    private CheckBox mTermsCondition;
    private EditText mInputPassword, mInputEmail, mReEnterPassword;
    private ImageView mButtonBack;

    private String email, password, reEnterPassword, currentDate, name, surname, shopName, address, city, phone;
    private ProgressDialog progressDialog;
    private String termsCondition = "false";

    private String URL = BASE_URL + "registerInfo.php";
    private String MERCHANT_URL = BASE_URL + "merchantUpload.php";
    private String INFO_URL = BASE_URL + "personalInfo.php";


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

        mTermsCondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    termsCondition = "true";
                } else {
                    termsCondition = "false";
                }
            }
        });


        init();

    }

    private void init() {
        mButtonNext.setOnClickListener(v -> {
            if (!termsCondition.equals("false")) {
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
                    name = getIntent().getStringExtra("name");
                    surname = getIntent().getStringExtra("surname");
                    phone = getIntent().getStringExtra("phone");
                    shopName = getIntent().getStringExtra("shopName");
                    address = getIntent().getStringExtra("address");
                    city = getIntent().getStringExtra("city");

                    if (!name.equals("") && !surname.equals("") && !phone.equals("") && !shopName.equals("")
                            && !address.equals("") && !city.equals("")) {

                        progressDialog.setMessage("Uploading information...");
                        progressDialog.show();

                       uploadPersonalInformation();

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

            } else {
                Toast.makeText(this, "Accept the Terms and Condition", Toast.LENGTH_SHORT).show();
            }

        });

        mButtonBack.setOnClickListener(v -> onBackPressed());

    }

    private void uploadPersonalInformation(){
        StringRequest request = new StringRequest(Request.Method.POST, URL, response -> {
            if (response.equals("success")) {
                progressDialog.dismiss();
                getData();

            } else if (response.equals("failure")) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Something went wrong!!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v1) {

                            }
                        });

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
    }

    private void getData(){
        StringRequest request = new StringRequest(Request.Method.GET, INFO_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String id = object.getString("id");

                        createMerchant(id);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(SignupActivity.this).add(request);
    }

    private void createMerchant(String id) {
        StringRequest request1 = new StringRequest(Request.Method.POST, MERCHANT_URL, response1 -> {
            if (response1.equals("success")) {
                startActivity(new Intent(SignupActivity.this, ConfirmationActivity.class));
                finish();

            } else if (response1.equals("failure")) {
                Snackbar.make(findViewById(android.R.id.content), "Something went wrong!!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v1) {

                            }
                        });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(findViewById(android.R.id.content), error.toString().trim(), Snackbar.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("mid", id);
                data.put("acc_no", getSaltString() + id);
                data.put("balance", "0.00");
                return data;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request1);
    }

    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 2) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    private boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}