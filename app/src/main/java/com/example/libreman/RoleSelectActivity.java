package com.example.libreman;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.widget.TextView;

public class RoleSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_select);

        CardView cardStudent = findViewById(R.id.cardStudent);
        CardView cardAdmin = findViewById(R.id.cardAdmin);
        TextView tvGuest = findViewById(R.id.tvGuest);

        cardStudent.setOnClickListener(v -> openMain());
        cardAdmin.setOnClickListener(v -> openMain());
        tvGuest.setOnClickListener(v -> openMain());
    }

    private void openMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();   // important

    }
}
