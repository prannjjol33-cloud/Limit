package com.prannjjol.limit;

import android.os.Bundle;
import android.widget.Toast;

public class SetBudgetActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);
        setupBottomNavigation();

        findViewById(R.id.saveBudgetButton).setOnClickListener(v -> {
            Toast.makeText(this, "Budget saved (dummy)", Toast.LENGTH_SHORT).show();
        });


    }
}
