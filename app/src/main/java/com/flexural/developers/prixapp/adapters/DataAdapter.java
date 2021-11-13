package com.flexural.developers.prixapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.activity.DataDetailsActivity;
import com.flexural.developers.prixapp.model.Data;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private Context context;
    private List<Data> dataList;
    private String network;

    public DataAdapter(Context context, List<Data> dataList, String network) {
        this.context = context;
        this.dataList = dataList;
        this.network = network;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_data_bundles, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data data = dataList.get(position);
        holder.mDataPrice.setText(data.dataPrice);
        holder.mDataAmount.setText("(" + data.dataAmount + ")");
        holder.mDescription.setText(data.description);

        if (network.equals("vodacom")) {
            holder.mBackground.setBackgroundColor(Color.RED);

        } else if (network.equals("telkom")) {
            holder.mBackground.setBackgroundColor(Color.BLUE);
        } else if (network.equals("cellc")) {
            holder.mBackground.setBackgroundColor(Color.BLACK);

        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DataDetailsActivity.class);
            intent.putExtra("dataPrice", data.dataPrice);
            intent.putExtra("dataAmount", data.dataAmount);
            intent.putExtra("description", data.description);
            intent.putExtra("network", network);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mDataAmount, mDataPrice, mDescription;
        private LinearLayout mBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDataAmount = itemView.findViewById(R.id.data_amount);
            mDataPrice = itemView.findViewById(R.id.data_price);
            mDescription = itemView.findViewById(R.id.description);
            mBackground = itemView.findViewById(R.id.background);

        }
    }
}
