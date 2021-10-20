package com.flexural.developers.prixapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flexural.developers.prixapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private RelativeLayout mButtonNext;
    private CheckBox mTermsCondition;
    private EditText mInputPhone, mInputEmail;
    private LinearLayout mMainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mButtonNext = findViewById(R.id.button_next);
        mTermsCondition = findViewById(R.id.checkbox);
        mInputPhone = findViewById(R.id.input_number);
        mInputEmail = findViewById(R.id.input_email);
        mMainLayout = findViewById(R.id.main_layout);

        init();

    }

    private void init() {
        mButtonNext.setOnClickListener(v -> {
            if (mInputPhone.length() == 10 && mInputEmail.length() > 0 && mTermsCondition.isChecked()){

                if (validate(mInputEmail.getText().toString())){
                    String phoneNumber = "+27" + mInputPhone.getText().toString().substring(1);
                    String email = mInputEmail.getText().toString();

                    Intent intent = new Intent(SignupActivity.this, CodeActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("email", email);
                    intent.putExtra("terms", "true");
                    startActivity(intent);

                } else {
                    Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                }

            } else if (mInputPhone.length() != 10) {
                Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_LONG).show();
            } else if (mInputEmail.length() == 0) {
                Toast.makeText(this, "Please Enter Email Address", Toast.LENGTH_LONG).show();
            } else if (!mTermsCondition.isChecked()) {
                Toast.makeText(this, "Please Accept Terms & Conditions", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(this, DetailsActivity.class));
        }
    }
}