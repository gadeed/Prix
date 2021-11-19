package com.flexural.developers.prixapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.flexural.developers.prixapp.MainActivity;
import com.flexural.developers.prixapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static com.flexural.developers.prixapp.activity.LoginScreen.BASE_URL;

public class SettingsActivity extends AppCompatActivity {

    private ImageView mButtonBack;
    private RelativeLayout mButtonLogout;
    private LinearLayout mChangePin, mButtonTermsConditions;

    private String oldPassword, newPassword, confirmPassword;
    private ProgressDialog progressDialog;

    private String URL = BASE_URL + "update.php";

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mButtonBack = findViewById(R.id.button_back);
        mButtonLogout = findViewById(R.id.button_logout);
        mChangePin = findViewById(R.id.button_change_pin);
        mButtonTermsConditions = findViewById(R.id.button_terms_conditions);

        oldPassword = newPassword = "";

        progressDialog = new ProgressDialog(this);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        init();

    }

    private void init() {
        mButtonBack.setOnClickListener(v -> onBackPressed());

        mChangePin.setOnClickListener(v -> {
            showDialog();
        });

        mButtonLogout.setOnClickListener(v -> {
            sharedPreferences.edit().putBoolean("logged", false).apply();
            Intent intent = new Intent(this, LoginScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

            Toast.makeText(this, "Successfully Sign Out!", Toast.LENGTH_SHORT).show();
        });

        mButtonTermsConditions.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("menu", "privacy");
            startActivity(intent);
        });

    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_change_pin);

        EditText mOldPassword = dialog.findViewById(R.id.old_password);
        EditText mNewPassword = dialog.findViewById(R.id.input_password);
        EditText mConfirmPassword = dialog.findViewById(R.id.confirm_password);
        RelativeLayout mChangePassword = dialog.findViewById(R.id.change_password);

        mChangePassword.setOnClickListener(v -> {
            oldPassword = mOldPassword.getText().toString().trim();
            newPassword = mNewPassword.getText().toString().trim();
            confirmPassword = mConfirmPassword.getText().toString().trim();

            if (!oldPassword.equals("") && !newPassword.equals("")) {
                if (newPassword.equals(confirmPassword)) {
                    progressDialog.setMessage("Updating PIN...");
                    progressDialog.show();
                    StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                                Toast.makeText(SettingsActivity.this, "Password change successful", Toast.LENGTH_SHORT).show();

                            } else if (response.equals("failure")) {
                                progressDialog.dismiss();
                                Snackbar.make(findViewById(android.R.id.content), "Invalid PIN", Snackbar.LENGTH_SHORT).show();

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
                            data.put("oldPassword", oldPassword);
                            data.put("newPassword", newPassword);
                            data.put("confirmPassword", confirmPassword);
                            return data;

                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(request);
                } else {
                    Toast.makeText(this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Please Fill All the Fields", Toast.LENGTH_SHORT).show();
            }

        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

}