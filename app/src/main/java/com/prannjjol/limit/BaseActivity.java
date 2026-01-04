package com.prannjjol.limit;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected void setupBottomNavigation() {

        View home = findViewById(R.id.btnHome);
        View transactions = findViewById(R.id.btnTransactions);
        View budget = findViewById(R.id.btnSetLimit);
        View settings = findViewById(R.id.btnSettings);

        if (home != null) {
            home.setOnClickListener(v -> {
                if (!(this instanceof MainActivity)) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            });
        }

        if (transactions != null) {
            transactions.setOnClickListener(v -> {
                if (!(this instanceof TransactionsActivity)) {
                    startActivity(new Intent(this, TransactionsActivity.class));
                    finish();
                }
            });
        }

        if (budget != null) {
            budget.setOnClickListener(v -> {
                if (!(this instanceof SetBudgetActivity)) {
                    startActivity(new Intent(this, SetBudgetActivity.class));
                    finish();
                }
            });
        }

        if (settings != null) {
            settings.setOnClickListener(v -> {
                if (!(this instanceof SettingsActivity)) {
                    startActivity(new Intent(this, SettingsActivity.class));
                    finish();
                }
            });
        }
    }


}
