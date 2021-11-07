package com.flexural.developers.prixapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.model.Airtime;

import java.util.List;

public class AirtimeAdapter extends RecyclerView.Adapter<AirtimeAdapter.ViewHolder> {
    private Context context;
    private List<Airtime> airtimeList;

    public AirtimeAdapter(Context context, List<Airtime> airtimeList) {
        this.context = context;
        this.airtimeList = airtimeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Airtime airtime = airtimeList.get(position);
        holder.mPrice.setText(airtime.prod_id);
        holder.mPinNumber.setText(airtime.pin_no);
        holder.mSerialNumber.setText(airtime.serial_no);
        holder.mStatus.setText(airtime.status);
        holder.mExpiredDate.setText(airtime.expired_date);
    }

    @Override
    public int getItemCount() {
        return airtimeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mPrice, mPinNumber, mSerialNumber, mStatus, mExpiredDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPrice = itemView.findViewById(R.id.price);
            mPinNumber = itemView.findViewById(R.id.pin_number);
            mSerialNumber = itemView.findViewById(R.id.serial_number);
            mStatus = itemView.findViewById(R.id.status);
            mExpiredDate = itemView.findViewById(R.id.expired_date);
        }
    }
}
