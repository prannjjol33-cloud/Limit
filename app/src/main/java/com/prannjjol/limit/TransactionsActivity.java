package com.prannjjol.limit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

public class TransactionsActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private TextView tvEmptyState;
    private TransactionAdapter adapter;
    private List<Transaction> transactionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        setupBottomNavigation();

        recyclerView = findViewById(R.id.recyclerTransactions);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter(transactionList);
        recyclerView.setAdapter(adapter);

        // dummy data (temporary)
        transactionList.add(new Transaction(250, "Amazon", System.currentTimeMillis()));
        transactionList.add(new Transaction(120, "Swiggy", System.currentTimeMillis()));

        updateEmptyState();

        findViewById(R.id.btnAddExpense).setOnClickListener(v -> {
            showAddTransactionDialog();
        });
    }

    private void updateEmptyState() {
        if (transactionList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showAddTransactionDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_transaction);
        dialog.setCancelable(true);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        Calendar selectedDateTime = Calendar.getInstance();

        // Find dialog views
        EditText etAmount = dialog.findViewById(R.id.etAmount);
        EditText etMerchant = dialog.findViewById(R.id.etMerchant);
        TextView tvDate = dialog.findViewById(R.id.tvDate);
        TextView tvTime = dialog.findViewById(R.id.tvTime);

        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        Calendar calendar = Calendar.getInstance();
        // 📅 Date Picker
        tvDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    TransactionsActivity.this,
                    (view, year, month, dayOfMonth) -> {

                        selectedDateTime.set(Calendar.YEAR, year);
                        selectedDateTime.set(Calendar.MONTH, month);
                        selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String dateText = dayOfMonth + "/" + (month + 1) + "/" + year;
                        tvDate.setText(dateText);
                    },
                    selectedDateTime.get(Calendar.YEAR),
                    selectedDateTime.get(Calendar.MONTH),
                    selectedDateTime.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // ⏰ Time Picker
        tvTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    TransactionsActivity.this,
                    (view, hourOfDay, minute) -> {

                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDateTime.set(Calendar.MINUTE, minute);
                        selectedDateTime.set(Calendar.SECOND, 0);

                        String timeText = String.format("%02d:%02d", hourOfDay, minute);
                        tvTime.setText(timeText);
                    },
                    selectedDateTime.get(Calendar.HOUR_OF_DAY),
                    selectedDateTime.get(Calendar.MINUTE),
                    true
            );
            timePickerDialog.show();
        });


        // Save button (dummy for now)
        btnSave.setOnClickListener(v -> {

            String amountStr = etAmount.getText().toString().trim();
            String dateStr = tvDate.getText().toString().trim();
            String merchant = etMerchant.getText().toString().trim();

            // ✅ Amount validation
            if (amountStr.isEmpty()) {
                etAmount.setError("Amount is required");
                etAmount.requestFocus();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    etAmount.setError("Amount must be greater than 0");
                    etAmount.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                etAmount.setError("Invalid amount");
                etAmount.requestFocus();
                return;
            }

            // ✅ Date validation
            if (dateStr.isEmpty() || dateStr.equalsIgnoreCase("Select Date")) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔹 Create Transaction object (in-memory)
            long timestamp = selectedDateTime.getTimeInMillis();

            Transaction transaction = new Transaction(
                    amount,
                    merchant.isEmpty() ? "Unknown" : merchant,
                    timestamp
            );

            // 🔹 Add to list & update UI
            transactionList.add(0, transaction);
            adapter.notifyItemInserted(0);
            recyclerView.scrollToPosition(0);

            updateEmptyState();
            dialog.dismiss();
        });


        // Cancel button
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
