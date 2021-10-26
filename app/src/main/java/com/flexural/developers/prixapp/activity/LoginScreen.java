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
import androidx.appcompat.app.AppCompatActivity;

import com.flexural.developers.prixapp.MainActivity;
import com.flexural.developers.prixapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginScreen extends AppCompatActivity {

    private ImageView mButtonClose;
    private EditText mInputNumber, mInputPassword;
    private RelativeLayout mButtonSignIn, mButtonSignUp;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private DatabaseReference mProfileRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mButtonClose = findViewById(R.id.button_close);
        mButtonSignIn = findViewById(R.id.button_sign_in);
        mButtonSignUp = findViewById(R.id.button_sign_up);
        mInputNumber = findViewById(R.id.input_number);
        mInputPassword = findViewById(R.id.input_password);

        progressDialog = new ProgressDialog(LoginScreen.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();

        mProfileRef = FirebaseDatabase.getInstance().getReference().child("prix").child("profile");

        init();
        getData();

    }

    private void init() {
        mButtonClose.setOnClickListener(v -> {
            onBackPressed();
        });

        mButtonSignIn.setOnClickListener(v -> {
            if (mInputNumber.length() == 10 && mInputPassword.length() == 6) {
                String phoneNumber = "+27" + mInputNumber.getText().toString().substring(1);

                Intent intent = new Intent(LoginScreen.this, CodeActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);

            } else if (mInputNumber.length() != 10){
                Snackbar.make(findViewById(android.R.id.content), "Invalid Phone Number", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
            } else if (mInputPassword.length() != 6) {
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

    private void getData() {
        if (mAuth.getCurrentUser() != null) {
            mProfileRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                        if (snapshot.child(mAuth.getCurrentUser().getUid()).hasChild("account")) {
                            progressDialog.dismiss();

                            Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } else if (snapshot.child(mAuth.getCurrentUser().getUid()).hasChild("email")){
                            progressDialog.dismiss();

                            Intent intent = new Intent(LoginScreen.this, ConfirmationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } else {
                            progressDialog.dismiss();

                            Intent intent = new Intent(LoginScreen.this, PasswordActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }

                    } else {
                        progressDialog.dismiss();

                        Intent intent = new Intent(LoginScreen.this, DetailsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            progressDialog.dismiss();

        }
    }
}