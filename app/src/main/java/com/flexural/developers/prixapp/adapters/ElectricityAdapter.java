package com.flexural.developers.prixapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.model.Electricity;

import java.util.List;

public class ElectricityAdapter extends RecyclerView.Adapter<ElectricityAdapter.ViewHolder> {
    private Context context;
    private List<Electricity> electricityList;

    public ElectricityAdapter(Context context, List<Electricity> electricityList) {
        this.context = context;
        this.electricityList = electricityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_electricity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Electricity electricity = electricityList.get(position);
        holder.mElectricityPrice.setText(electricity.price);
        holder.mDescription.setText(electricity.price);

    }

    @Override
    public int getItemCount() {
        return electricityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mElectricityPrice, mDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mElectricityPrice = itemView.findViewById(R.id.electricity_price);
            mDescription = itemView.findViewById(R.id.description);

        }
    }
}
