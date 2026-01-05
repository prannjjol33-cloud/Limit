package com.prannjjol.limit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter
        extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final List<Transaction> transactions;

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction t = transactions.get(position);

        holder.tvMerchant.setText(t.merchant);
        holder.tvAmount.setText("₹ " + t.amount);

        SimpleDateFormat sdf =
                new SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault());
        holder.tvTime.setText(sdf.format(new Date(t.timestamp)));

        holder.btnDelete.setOnClickListener(v ->
                Toast.makeText(v.getContext(),
                        "Delete clicked (dummy)",
                        Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMerchant, tvAmount, tvTime;
        ImageButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMerchant = itemView.findViewById(R.id.textMerchant);
            tvAmount = itemView.findViewById(R.id.textAmount);
            tvTime = itemView.findViewById(R.id.textTime);
            btnDelete = itemView.findViewById(R.id.deleteButton);
        }
    }
}
