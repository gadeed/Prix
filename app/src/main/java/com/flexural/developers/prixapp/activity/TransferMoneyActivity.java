package com.flexural.developers.prixapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class TransferMoneyActivity extends AppCompatActivity {
    private String URL_WALLET = BASE_URL + "merchantWallet.php";

    private EditText mInputAmount, mInputReference;
    private TextView mMerchantID, mCurrentBalance;
    private RelativeLayout mButtonPay;

    private String balance, inputAmount, inputReference;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money);

        mInputAmount = findViewById(R.id.input_amount);
        mInputReference = findViewById(R.id.input_reference);
        mMerchantID = findViewById(R.id.account_number);
        mCurrentBalance = findViewById(R.id.current_balance);
        mButtonPay = findViewById(R.id.button_pay);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Transferring money...");

        receiveIntent();

    }

    private void receiveIntent() {
//        String midTr = getIntent().getStringExtra("mid_tr");
        String shopAccNo = getIntent().getStringExtra("shop_acc_no");
        String currentAccNo = getIntent().getStringExtra("current_acc_no");
        mMerchantID.setText(shopAccNo);

        getWalletBalance(currentAccNo);

    }

    private void getWalletBalance(String currentAccNo){
        StringRequest request = new StringRequest(Request.Method.GET, URL_WALLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String accNo = object.getString("acc_no");

                        if (accNo.equals(currentAccNo)) {
                            String accNoTr = object.getString("acc_no");

                            mButtonPay.setOnClickListener(v -> {
                                inputAmount = mInputAmount.getText().toString();
                                inputReference = mInputReference.getText().toString();

                                if (!inputAmount.equals("") && !inputReference.equals("")) {
                                    if (Float.valueOf(balance) > Float.valueOf(inputAmount)){
                                        progressDialog.show();

                                    } else {
                                        progressDialog.dismiss();

                                        Toast.makeText(TransferMoneyActivity.this, "Insufficient Funds! Please Top-Up your Account", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    progressDialog.dismiss();

                                    Toast.makeText(TransferMoneyActivity.this, "Please Fill All the Fields", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        if (accNo.equals(currentAccNo)) {
                            balance = object.getString("balance");

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mCurrentBalance.setText("R" + balance);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransferMoneyActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(TransferMoneyActivity.this).add(request);

    }

}