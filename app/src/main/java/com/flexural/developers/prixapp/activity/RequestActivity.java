package com.flexural.developers.prixapp.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

import static com.flexural.developers.prixapp.activity.LoginScreen.BASE_URL;

public class RequestActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    private TextView mButtonAdd;
    private ImageView mImageSelected;
    private EditText mTopUpAmount;
    private Button mButtonRequest;

    private String URL = BASE_URL + "topUp.php";
    private String topUpAmount, currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        mButtonAdd = findViewById(R.id.button_add);
        mImageSelected = findViewById(R.id.image_selected);
        mTopUpAmount = findViewById(R.id.top_up_amount);
        mButtonRequest = findViewById(R.id.button_request);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDate = df.format(Calendar.getInstance().getTime());

        topUpAmount = "";

        init();

    }

    private void init() {
        mButtonAdd.setOnClickListener(v -> {
            ImagePicker.with(this)
//                    .crop()	    			//Crop image(Optional), Check Customization for more option
//                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent((Function1) (new Function1() {
                        public Object invoke(Object var1) {
                            this.invoke((Intent) var1);
                            return Unit.INSTANCE;
                        }

                        public final void invoke(@NotNull Intent it) {
                            Intrinsics.checkNotNullParameter(it, "it");
                            launcher.launch(it);
                        }
                    }));
        });


    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            (ActivityResult result) -> {
        if (result.getResultCode() == RESULT_OK) {
            Uri uri = result.getData().getData();
            Glide.with(RequestActivity.this).load(uri).into(mImageSelected);

            mButtonRequest.setOnClickListener(v -> {
                topUpAmount = mTopUpAmount.getText().toString().trim();

                if (!topUpAmount.equals("") && uri != null) {
                    StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                Intent intent = new Intent(RequestActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                finish();

                            } else if (response.equals("failure")) {
                                Snackbar.make(findViewById(android.R.id.content), "Invalid Login Id/Password", Snackbar.LENGTH_SHORT).show();

                            }
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
                            data.put("mid", "");
                            data.put("amount", topUpAmount);
                            data.put("payment_document", String.valueOf(uri));
                            data.put("status", "Received");
                            data.put("created", currentDate);
                            data.put("updated", currentDate);
                            return data;

                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(request);
                } else {
                    Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {

        }
            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri selectedImage = data.getData();

                mImageSelected.setImageURI(selectedImage);
//                Glide.with(RequestActivity.this).load(selectedImage).into(mImageSelected);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onStart() {
        super.onStart();
        /* ... */
        boolean requestPermissions = false;
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        for(String s : permissions)
            if(ContextCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED)
                requestPermissions = true;
        if(requestPermissions)
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);

    }
}