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

        cardStudent = findViewById(R.id.cardStudent);
        cardAdmin = findViewById(R.id.cardAdmin);
        tvGuest = findViewById(R.id.tvGuest);

        // ✅ Student → StudentAuthActivity
        cardStudent.setOnClickListener(v -> {
            startActivity(new Intent(this, StudentAuthActivity.class));
        });

        // ✅ Admin → AdminAuthActivity (UPDATED)
        cardAdmin.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminAuthActivity.class));
        });

        // ✅ Guest → Catalog
        tvGuest.setOnClickListener(v -> {
            Intent intent = new Intent(this, CatalogActivity.class);
            intent.putExtra("guest_mode", true);
            startActivity(intent);
        });

        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        finishAffinity();
                    }
                });
    }
}
