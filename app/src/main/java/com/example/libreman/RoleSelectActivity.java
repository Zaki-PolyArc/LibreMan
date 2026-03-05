package com.example.libreman;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.FirebaseDatabase;

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
        // Add this at the top of onCreate() in YOUR LAUNCHER ACTIVITY
        FirebaseDatabase database = FirebaseDatabase
                .getInstance("https://libreman-f9839-default-rtdb.asia-southeast1.firebasedatabase.app/");

        database.getReference("test")
                .setValue("hello from launcher!")
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, "✅ IT WORKS!", Toast.LENGTH_LONG).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "❌ " + e.getMessage(), Toast.LENGTH_LONG).show());

        // Admin click
        cardAdmin.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminAuthActivity.class));
        });

        // ✅ Guest → Catalog
        tvGuest.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
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
