package com.prannjjol.limit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class TransactionsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        setupBottomNavigation();

        findViewById(R.id.btnAddExpense).setOnClickListener(v -> {
            showAddTransactionDialog();
        });

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
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        tvDate.setText(date);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // ⏰ Time Picker
        tvTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    TransactionsActivity.this,
                    (view, hourOfDay, minute) -> {
                        String time = String.format("%02d:%02d", hourOfDay, minute);
                        tvTime.setText(time);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true   // true = 24-hour format
            );
            timePickerDialog.show();
        });

        // Save button (dummy for now)
        btnSave.setOnClickListener(v -> {
            String amountStr = etAmount.getText().toString().trim();
            String dateStr = tvDate.getText().toString().trim();

            // ✅ Amount validation
            if (amountStr.isEmpty()) {
                etAmount.setError("Amount is required");
                etAmount.requestFocus();
                return;
            }

            // Optional: prevent zero / negative amount
            try {
                double amount = Double.parseDouble(amountStr);
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

            // ✅ Passed all validations
            Toast.makeText(this, "Transaction saved (dummy)", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        // Cancel button
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
