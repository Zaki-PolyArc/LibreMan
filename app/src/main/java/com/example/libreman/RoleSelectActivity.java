package com.example.libreman;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RoleSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_select);

        CardView cardAdmin = findViewById(R.id.cardAdmin);
        CardView cardStudent = findViewById(R.id.cardStudent);

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
