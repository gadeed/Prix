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
    private String shopName, walletAcc, currentMid;

    public TransferAdapter(Context context, List<Transfer> transferList, String shopName, String walletAcc, String currentMid) {
        this.context = context;
        this.transferList = transferList;
        this.shopName = shopName;
        this.walletAcc = walletAcc;
        this.currentMid = currentMid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_transfer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mShopName.setText(shopName);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TransferMoneyActivity.class);
            intent.putExtra("mid_tr", walletAcc);
            intent.putExtra("mid", currentMid);
            context.startActivity(intent);
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
        private TextView mShopName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mShopName = itemView.findViewById(R.id.shop_name);

        }

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
