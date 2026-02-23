package com.example.libreman;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.FirebaseDatabase;

public class RoleSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_select);

        CardView cardAdmin = findViewById(R.id.cardAdmin);
        CardView cardStudent = findViewById(R.id.cardStudent);

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
            Intent intent = new Intent(RoleSelectActivity.this, MainActivity.class);
            intent.putExtra("ROLE", "ADMIN");
            startActivity(intent);
            finish();
        });

        // Student click (temporary same dashboard)
        cardStudent.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectActivity.this, MainActivity.class);
            intent.putExtra("ROLE", "STUDENT");
            startActivity(intent);
            finish();
        });
    }
}
