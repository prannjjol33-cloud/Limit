package com.prannjjol.limit;

import android.os.Bundle;
import android.widget.Toast;


public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupBottomNavigation();

        findViewById(R.id.btnSaveDefaultBudget).setOnClickListener(v -> {
            Toast.makeText(this, "Default budget saved (dummy)", Toast.LENGTH_SHORT).show();
        });

    }
}
