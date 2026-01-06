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

    private AppDatabase db;
    private TransactionDao transactionDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        db = AppDatabase.getInstance(this);
        transactionDao = db.transactionDao();
        setupBottomNavigation();

        recyclerView = findViewById(R.id.recyclerTransactions);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter(transactionList, position -> {
            showDeleteDialog(position);
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);

        loadTransactionsFromDb();

        updateEmptyState();

        findViewById(R.id.btnAddExpense).setOnClickListener(v -> {
            showAddTransactionDialog();
        });
    }

    private void loadTransactionsFromDb() {
        new Thread(() -> {
            List<Transaction> list = transactionDao.getAllTransactions();

            runOnUiThread(() -> {
                transactionList.clear();
                transactionList.addAll(list);
                adapter.notifyDataSetChanged();
                updateEmptyState();
            });
        }).start();
    }

    private void showDeleteDialog(int position) {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delete_transaction);
        dialog.setCancelable(true);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        Button btnCancel = dialog.findViewById(R.id.cancelButton);
        Button btnDelete = dialog.findViewById(R.id.deleteButton);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnDelete.setOnClickListener(v -> {
            Transaction toDelete = transactionList.get(position);

            new Thread(() -> {
                transactionDao.delete(toDelete);

                runOnUiThread(() -> {
                    loadTransactionsFromDb();
                    dialog.dismiss();
                });
            }).start();
        });

        dialog.show();
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

            new Thread(() -> {
                transactionDao.insert(transaction);

                runOnUiThread(() -> {
                    loadTransactionsFromDb();
                    dialog.dismiss();
                });
            }).start();

            updateEmptyState();
            dialog.dismiss();
        });


        // Cancel button
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
