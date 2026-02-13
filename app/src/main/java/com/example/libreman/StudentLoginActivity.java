package com.example.libreman;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class StudentLoginActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView btnLogin, btnSignup;
    private TextInputEditText etStudentId, etEmail, etPassword;
    private MaterialButton btnLoginSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        btnBack = findViewById(R.id.btn_back);
        btnLogin = findViewById(R.id.btn_login);
        btnSignup = findViewById(R.id.btn_signup);
        etStudentId = findViewById(R.id.et_student_id);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLoginSubmit = findViewById(R.id.btn_login_submit);

        btnBack.setOnClickListener(v -> finish());

        btnSignup.setOnClickListener(v -> {
            startActivity(new Intent(this, StudentSignupActivity.class));
            finish();
        });

        btnLoginSubmit.setOnClickListener(v -> validateAndLogin());

        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        finish();
                    }
                });
    }

    private void validateAndLogin() {
        String studentId = etStudentId.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(studentId)) {
            etStudentId.setError("Student ID required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email required");
            return;
        }

        if (password.length() < 8) {
            etPassword.setError("Min 8 characters");
            return;
        }

        Toast.makeText(this, "Student Login Successful", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, CatalogActivity.class);
        startActivity(intent);
        finish();
    }
}
