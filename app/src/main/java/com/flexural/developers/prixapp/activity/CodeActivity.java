package com.flexural.developers.prixapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.flexural.developers.prixapp.MainActivity;
import com.flexural.developers.prixapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CodeActivity extends AppCompatActivity {

    private RelativeLayout mButtonNext;
    private PinEntryEditText mInputCode;
    private LinearLayout mMainLayout;

    private String mAuthVerificationId, email, phoneNumber;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private ProgressDialog progressDialog;

    private DatabaseReference mAccountRef, mProfileRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        mButtonNext = findViewById(R.id.button_next);
        mInputCode = findViewById(R.id.input_code);
        mMainLayout = findViewById(R.id.main_layout);

        progressDialog = new ProgressDialog(CodeActivity.this);
        progressDialog.setMessage("Please Wait...");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mAccountRef = FirebaseDatabase.getInstance().getReference().child("prix").child("accounts");
        mProfileRef = FirebaseDatabase.getInstance().getReference().child("prix").child("profile");


        init();
        receiveIntent();

    }

    private void receiveIntent() {
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");
        email = intent.getStringExtra("email");

        sendVerificationCode(phoneNumber);
        progressDialog.show();

    }

    private void sendVerificationCode(String number) {
        progressDialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                CodeActivity.this,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mAuthVerificationId = s;
            progressDialog.dismiss();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                mInputCode.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(CodeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthVerificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            account(mAuth.getCurrentUser());

                            mProfileRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.child(mAuth.getCurrentUser().getUid()).hasChild("account")) {
                                        Intent intent = new Intent(CodeActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    } else if (snapshot.child(mAuth.getCurrentUser().getUid()).hasChild("email")) {
                                        Intent intent = new Intent(CodeActivity.this, PasswordActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    } else {
                                        Intent intent = new Intent(CodeActivity.this, DetailsActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            Toast.makeText(CodeActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void init() {
        mButtonNext.setOnClickListener(v -> {
            String code = mInputCode.getText().toString().trim();

            if (code.isEmpty() || code.length() < 6) {

                mInputCode.setError("Enter code...");
                mInputCode.requestFocus();
                return;
            } else {
                progressDialog.show();
                verifyCode(code);

            }

        });
    }


    private void account(FirebaseUser user){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("phoneNumber", phoneNumber);
        hashMap.put("email", email);
        hashMap.put("userId", mAuth.getCurrentUser().getUid());

        mAccountRef.child(user.getUid()).updateChildren(hashMap);

    }

}