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
import com.flexural.developers.prixapp.utils.DatabaseList;

import java.util.List;

public class AirtimeAdapter extends RecyclerView.Adapter<AirtimeAdapter.ViewHolder> {
    private Context context;
    private List<DatabaseList> databaseLists;

    public AirtimeAdapter(Context context, List<DatabaseList> databaseLists) {
        this.context = context;
        this.databaseLists = databaseLists;
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
        DatabaseList airtime = databaseLists.get(position);
        holder.mPrice.setText(airtime.getProd_id());
        holder.mPinNumber.setText(airtime.getPin_no());
        holder.mSerialNumber.setText(airtime.getSerial_no());

    }

    @Override
    public int getItemCount() {
        return databaseLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mPrice, mPinNumber, mSerialNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPrice = itemView.findViewById(R.id.price);
            mPinNumber = itemView.findViewById(R.id.pin_number);
            mSerialNumber = itemView.findViewById(R.id.serial_number);
        }
    }
}
