package com.flexural.developers.prixapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.model.Sales;

import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder> {
    private Context context;
    private List<Sales> salesList;

    public SalesAdapter(Context context, List<Sales> salesList) {
        this.context = context;
        this.salesList = salesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_sales, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sales sales = salesList.get(position);

        holder.mPrice.setText(sales.price);
        holder.mProductType.setText(sales.productType);
        holder.mProductName.setText(sales.productName);
        holder.mQuantity.setText(sales.qty);
        holder.mTotalPrice.setText(sales.total);
        holder.mPercent.setText(sales.percent);
        holder.mMerchant.setText(sales.merchant);

    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mProductType, mProductName, mQuantity, mPrice, mTotalPrice, mPercent, mMerchant;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPrice = itemView.findViewById(R.id.price);
            mProductType = itemView.findViewById(R.id.product_type);
            mProductName = itemView.findViewById(R.id.product_name);
            mQuantity = itemView.findViewById(R.id.quantity);
            mTotalPrice = itemView.findViewById(R.id.total_price);
            mPercent = itemView.findViewById(R.id.percentage);
            mMerchant = itemView.findViewById(R.id.merchant);

        }
    }
}
