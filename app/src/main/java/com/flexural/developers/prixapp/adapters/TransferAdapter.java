package com.flexural.developers.prixapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.activity.PrinterActivity;
import com.flexural.developers.prixapp.activity.TransferMoneyActivity;
import com.flexural.developers.prixapp.model.Airtime;
import com.flexural.developers.prixapp.model.AirtimeId;
import com.flexural.developers.prixapp.model.Transfer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

import static com.flexural.developers.prixapp.activity.LoginScreen.BASE_URL;

public class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.ViewHolder> {
    private Context context;
    private List<Transfer> transferList;
    private String URL = BASE_URL + "personalInfo.php";

    public TransferAdapter(Context context, List<Transfer> transferList) {
        this.context = context;
        this.transferList = transferList;

// marka adaporka imaado constructorka adaptorka oo kudhex jiro waxaa loosoo baasay dhowr parameter 
// transfersList kaliya ayaa muhiima. 
// shopname,walletAcc iyo currentMid looba baahna marka waad tiri kartaan 
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_transfer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Transfer transfer = transferList.get(position);
        holder.mShopName.setText(transfer.shopName);
        holder.mAccNo.setText(transfer.shopAccNo);
        // waxaan kusoo daray 2 daan line ee  hoose oo ah inaan soo qaado shopName iyo accNo 
        // waana sababta aan uuga maarmay qaybaha aan kor kusheegay file kaan dhexdiisa 
        
        // REF1 Transfer model ayuu wacayaa


   // halkan ayaan ku muujiyey shopName
   // NB shopName ka kaliya ayaa muuqanaya liiska saaxiibada ama transferlist 
   // waan isticmaali karaa accNo markaa ubaahado ileen variablka accNo ayaa ihaya fiiri comentiga korkiisa
   


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TransferMoneyActivity.class);
            intent.putExtra("current_acc_no", transfer.accNo);
            intent.putExtra("shop_acc_no", transfer.shopAccNo);
            context.startActivity(intent);

//            Toast.makeText(context, transfer.accNo, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        if (transferList.size() == 0){
            return 0;
        } else {
            return transferList.size();

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mShopName, mAccNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mShopName = itemView.findViewById(R.id.shop_name);
            mAccNo = itemView.findViewById(R.id.acc_no);

        }
        
        
        
        //// methodkaan hoose asagana looma baahna waayo shaqadeena waan qabsanay
        //kaliya waxaad karabteen inaa 'mid' ga kusoo heshaan shopName ka, taasna waan helnay 
        // marka uuma baahnin

        public void getShopName(String mid){
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            String id = object.getString("id");


                            if (mid.equals(id)) {
                                String shopName = object.getString("shop_name");

                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            Volley.newRequestQueue(context).add(stringRequest);
        }
    }
}
