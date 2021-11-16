package com.flexural.developers.prixapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flexural.developers.prixapp.R;
import com.flexural.developers.prixapp.model.Statements;

import java.util.List;

public class StatementAdapter extends RecyclerView.Adapter<StatementAdapter.ViewHolder> {
    private Context context;
    private List<Statements> statementsList;

    public StatementAdapter(Context context, List<Statements> statementsList) {
        this.context = context;
        this.statementsList = statementsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_statement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Statements statements = statementsList.get(position);
        holder.mTransactionDate.setText(statements.transactionDate);
        holder.mSender.setText(statements.sender);
        holder.mAmount.setText(statements.amount);

    }

    @Override
    public int getItemCount() {
        return statementsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTransactionDate, mSender, mAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTransactionDate = itemView.findViewById(R.id.transaction_date);
            mAmount = itemView.findViewById(R.id.amount);
            mSender = itemView.findViewById(R.id.sender);

        }
    }
}
