package com.flexural.developers.prixapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flexural.developers.prixapp.MainActivity;
import com.flexural.developers.prixapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ConfirmationActivity extends AppCompatActivity {

    private RelativeLayout mButtonNext;

    private String userId;

    private DatabaseReference mProfileRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        mButtonNext = findViewById(R.id.button_next);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mProfileRef = FirebaseDatabase.getInstance().getReference().child("prix").child("profile");

        init();

    }

    private void init() {
        mButtonNext.setOnClickListener(v -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("account", "true");

            mProfileRef.child(userId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(ConfirmationActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            });

        });
    }
}