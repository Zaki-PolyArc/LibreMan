package com.example.libreman;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RoleSelectActivity extends AppCompatActivity {

    private CardView cardStudent;
    private CardView cardAdmin;
    private TextView tvGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_select);

        initializeViews();
        setClickListeners();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
            }
        });
    }

    private void initializeViews() {
        cardStudent = findViewById(R.id.cardStudent);
        cardAdmin = findViewById(R.id.cardAdmin);
        tvGuest = findViewById(R.id.tvGuest);
    }

    private void setClickListeners() {

        // ✅ Student Card - Navigate to Student Login
        cardStudent.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectActivity.this, StudentLoginActivity.class);
            startActivity(intent);
        });

        // ✅ Admin Card - Navigate to Admin Login
        cardAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectActivity.this, AdminLoginActivity.class);
            startActivity(intent);
        });

        // ✅ Guest Link - Navigate to Catalog
        tvGuest.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectActivity.this, CatalogActivity.class);
            intent.putExtra("guest_mode", true);
            startActivity(intent);
        });
    }
}
