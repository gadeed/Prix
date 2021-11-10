package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flexural.developers.prixapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.flexural.developers.prixapp.activity.LoginScreen.BASE_URL;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView mButtonBack;
    private TextView mUsername, mPhoneNumber, mMerchantNumber, mEmail, mAddress, mShopName, mID;

    private String URL = BASE_URL + "personalInfo.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mButtonBack = findViewById(R.id.button_back);
        mUsername = findViewById(R.id.user_name);
        mPhoneNumber = findViewById(R.id.phone_number);
        mMerchantNumber = findViewById(R.id.merchant_number);
        mEmail = findViewById(R.id.email_address);
        mAddress = findViewById(R.id.address);
        mShopName = findViewById(R.id.shop_name);

        mID = findViewById(R.id.id);

        init();
        getData();

    }

    private void init() {
        mButtonBack.setOnClickListener(v -> onBackPressed());
    }

    private void getData(){
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String id = object.getString("id");
                        String firstName = object.getString("first_name");
                        String lastName = object.getString("last_name");
                        String shopName = object.getString("shop_name");
                        String address = object.getString("address");
                        String city = object.getString("city");
                        String email = object.getString("email");
                        String phone = object.getString("phone");

                        mUsername.setText(firstName + " " + lastName);
                        mEmail.setText(email);
                        mPhoneNumber.setText(phone);
                        mAddress.setText(address + ", " + city);
                        mShopName.setText(shopName);
                        mID.setText(id);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProfileActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(EditProfileActivity.this).add(request);
    }
}