package com.flexural.developers.prixapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.adapters.TransferAdapter;
import com.flexural.developers.prixapp.model.Transfer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.flexural.developers.prixapp.activity.LoginScreen.BASE_URL;

public class TransferActivity extends AppCompatActivity {
    private String URL = BASE_URL + "getTransferList.php";
    private String URL_WALLET = BASE_URL + "personalInfo.php";

    private CardView mButtonAddFriend, mShareCode;
    private RecyclerView mRecyclerTransfer;

    private List<Transfer> transferList;
    private TransferAdapter transferAdapter;
    private String shopName, currentAccNo, currentMid, shopAccNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        mButtonAddFriend = findViewById(R.id.button_add_friend);
        mShareCode = findViewById(R.id.button_qr_code);
        mRecyclerTransfer = findViewById(R.id.recycler_view);

        mRecyclerTransfer.setLayoutManager(new GridLayoutManager(this, 3));

        transferList = new ArrayList<>();

  // Comments. 1- shaqada file kan halkan ayay kabilaabanaysaa
  // method kan hoose ayaa lawacayaa, aan raacno.
        receiveIntent();

    }

    private void loadData(String currentMid, String currentAccNo) {
        // 4. marka methodkaan la imaado anoo wata currentMid oo ah merchantID ga waxaan waacayaa script-ga ladhoho 'getTransferList.php'
        // waxaana tuurayaa currentMid ama mid, kadib script-ga wuxuu soo celinayaa json oo ah list of transfers ama dadka asaga saaxiibadiis ah oo uu horay 
        // u diiwangashaday.
        // script-gaas xogta uu soo celinayo waa sidan: accNo ka iyo shopName ka 
        // tusaale haddi 5 qof kuugu jirto liiska transfers ka waxaad arkaysaa 5ta qof shopName kooda iyo  accNo kooda laakiin waxa soo muuqanayo waa shopName ka
        
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                // waa kuwaan accNo iyo shopnName ka aan kaga hadlay faallada kor ku xusan
                        shopAccNo = object.getString("accNo");
                        shopName = object.getString("shopName");

        // NB qaybtan hoose oo comment ga saaray looma baahna 
        
//                        if (mid.equals(currentMid)){
//                            String midTr = object.getString("mid_tr");
//
//                            Transfer transfer = new Transfer(currentMid, midTr);
//                            transferList.add(transfer);
//
//                            getShopName(midTr);
//                        }

                // kadib markaa soo celiyo liiska aan soo sheegay waxaan ku shubayaa ArrayListka hoose oo ladhoho transferList
                // iyo modelka ladhoho Transer 
                        Transfer transfer = new Transfer(currentAccNo, shopName, shopAccNo);
                        transferList.add(transfer);
                    }

                // kadib halkaan ayaa Adaptorka ladhoho TransferAdaper looga dhiibayaa transferList 
                // waxaynu aadaynaa adaptorka, ila soco
                
                    transferAdapter = new TransferAdapter(TransferActivity.this, transferList);
                    mRecyclerTransfer.setAdapter(transferAdapter);
                    transferAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransferActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("mid", currentMid);
                return data;

            }
        };
        Volley.newRequestQueue(TransferActivity.this).add(request);
    }

    private void receiveIntent() {
        
        // 2. marka methodkan la imaado, code ka hoose ee intent-ga ha wuxuu soo aqrinayaa labo qiime oo 
        // kala ah shopName iyo mid. shopName wa magaca dukaanka merchantiga, mid waa ID-ga merchant-ga tusaale '35' tiro lamabar ah 
        // NB. mesha aad ka isticmaasheen mid oo lambar ah isticmaala merchantAccNo oo ah 'GHZ35' hadda kadib waayo asaga ayaa fahan ahaan iyo caqli ahaan fudud.
        Intent intent = getIntent();
        String shopName = intent.getStringExtra("shopName");
        currentMid = intent.getStringExtra("mid");
        currentAccNo = intent.getStringExtra("acc_no");


// 3. kadib methodkaas hoose ee loadData ayaa lawacayaa ayadoo loo dhiibayo parameter ah currentMid oo lamabarka 35 camal ah.
        loadData(currentMid, currentAccNo);
//        Toast.makeText(this, currentMid, Toast.LENGTH_SHORT).show();

        mButtonAddFriend.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, AddFriendActivity.class);
            intent1.putExtra("mid", currentMid);
            startActivity(intent1);
        });

        mShareCode.setOnClickListener(v -> {
            Intent sendIntent = new Intent(this, ExtraActivity.class);
            sendIntent.putExtra("extra", "qr_code");
            sendIntent.putExtra("shopName", shopName);
            sendIntent.putExtra("mid", currentMid);
            startActivity(sendIntent);
        });
    }
    
    // 5. methodkaan looma baahna waayo shaqo dheeraad ah oo aan loobahnayo ayuu qabanayaa
    // waana laga maarmaa

//    private void getShopName(String midTr) {
//        StringRequest request = new StringRequest(Request.Method.GET, URL_WALLET, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONArray array = new JSONArray(response);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject object = array.getJSONObject(i);
//                        walletAccNo = object.getString("id");
//
//                        if (midTr.equals(walletAccNo)){
//                            shopName = object.getString("shop_name");
//
//                        }
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                transferAdapter = new TransferAdapter(TransferActivity.this, transferList, shopName, midTr, currentMid);
//                mRecyclerTransfer.setAdapter(transferAdapter);
//                transferAdapter.notifyDataSetChanged();
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(TransferActivity.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//        Volley.newRequestQueue(TransferActivity.this).add(request);
//    }

}