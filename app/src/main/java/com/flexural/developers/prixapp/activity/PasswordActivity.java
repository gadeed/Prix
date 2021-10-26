package com.flexural.developers.prixapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

public class PasswordActivity extends AppCompatActivity {

    private final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private PinEntryEditText mInputPassword, mConfirmPassword;
    private RelativeLayout mButtonNext;
    private LinearLayout mMainLayout;
    private ImageView mButtonBack;

    private String userId;
    private AuthUtils authUtils;

    private FirebaseAuth mAuth;

    private DatabaseReference mAccountRef, mProfileRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        mInputPassword = findViewById(R.id.input_password);
        mConfirmPassword = findViewById(R.id.confirm_password);
        mButtonNext = findViewById(R.id.button_next);
        mMainLayout = findViewById(R.id.main_layout);
        mButtonBack = findViewById(R.id.button_back);

        mAuth = FirebaseAuth.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mProfileRef = FirebaseDatabase.getInstance().getReference().child("prix").child("profile");
        mAccountRef = FirebaseDatabase.getInstance().getReference().child("prix").child("accounts");

        init();


    }

    private void init() {
        mAccountRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                mButtonNext.setOnClickListener(v -> {
                    if (mInputPassword.length() > 0 && mConfirmPassword.length() > 0) {
                        String password = mInputPassword.getText().toString();
                        String confirmPassword = mConfirmPassword.getText().toString();

                        if (validate(password, confirmPassword)) {
                           HashMap<String, Object> hashMap = new HashMap<>();
                           hashMap.put("password", password);

                           mAccountRef.child(userId).updateChildren(hashMap)
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()) {
                                               HashMap<String, Object> hashMap1 = new HashMap<>();
                                               hashMap1.put("email", "true");

                                               mProfileRef.child(userId).updateChildren(hashMap1)
                                                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<Void> task) {
                                                               if (task.isSuccessful()) {
                                                                   startActivity(new Intent(PasswordActivity.this, ConfirmationActivity.class));

                                                               }
                                                           }
                                                       });
                                           }
                                       }
                                   });

                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Passwords do not match", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                        }

                    } else if (mInputPassword.length() == 0) {
                        Toast.makeText(PasswordActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();

                    } else if (mConfirmPassword.length() == 0) {
                        Toast.makeText(PasswordActivity.this, "Please Confirm Password", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mButtonBack.setOnClickListener(v -> onBackPressed());

    }

    private boolean validate(String password, String repeatPassword) {
        return password.length() > 0 && repeatPassword.equals(password);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    class AuthUtils {
        /**
         * Action register
         *
         * @param email
         * @param password
         */
        void createUser(String email, String password) {


        }

        /**
         * Action reset password
         *
         * @param email
         */
//        void resetPassword(final String email) {
//            mAuth.sendPasswordResetEmail(email)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            new LovelyInfoDialog(LoginActivity.this) {
//                                @Override
//                                public LovelyInfoDialog setConfirmButtonText(String text) {
//                                    findView(com.yarolegovich.lovelydialog.R.id.ld_btn_confirm).setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            dismiss();
//                                        }
//                                    });
//                                    return super.setConfirmButtonText(text);
//                                }
//                            }
//                                    .setTopColorRes(R.color.colorAccent)
//                                    .setIcon(R.drawable.ic_pass_reset)
//                                    .setTitle("Password Recovery")
//                                    .setMessage("Sent email to " + email)
//                                    .setConfirmButtonText("Ok")
//                                    .show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            new LovelyInfoDialog(LoginActivity.this) {
//                                @Override
//                                public LovelyInfoDialog setConfirmButtonText(String text) {
//                                    findView(com.yarolegovich.lovelydialog.R.id.ld_btn_confirm).setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            dismiss();
//                                        }
//                                    });
//                                    return super.setConfirmButtonText(text);
//                                }
//                            }
//                                    .setTopColorRes(R.color.colorAccent)
//                                    .setIcon(R.drawable.ic_pass_reset)
//                                    .setTitle("False")
//                                    .setMessage("False to sent email to " + email)
//                                    .setConfirmButtonText("Ok")
//                                    .show();
//                        }
//                    });
//        }

    }

    public void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Snackbar.make(mMainLayout, "Email has been sent for verification", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();

                        }
                        else{
                            Toast.makeText(PasswordActivity.this, "couldn't send email", Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }

}