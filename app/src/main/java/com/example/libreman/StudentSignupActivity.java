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

public class StudentSignupActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView btnLogin, btnSignup, tvLoginLink;
    private TextInputEditText etFullName, etStudentId, etEmail, etPassword;
    private MaterialButton btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);

        btnBack = findViewById(R.id.btn_back);
        btnLogin = findViewById(R.id.btn_login);
        btnSignup = findViewById(R.id.btn_signup);
        etFullName = findViewById(R.id.et_full_name);
        etStudentId = findViewById(R.id.et_student_id);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnCreateAccount = findViewById(R.id.btn_create_account);
        tvLoginLink = findViewById(R.id.tv_login_link);

        btnBack.setOnClickListener(v -> finish());

        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, StudentLoginActivity.class));
            finish();
        });

        tvLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(this, StudentLoginActivity.class));
            finish();
        });

        btnCreateAccount.setOnClickListener(v -> createAccount());

        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        finish();
                    }
                });
    }

    private void createAccount() {
        String name = etFullName.getText().toString().trim();
        String id = etStudentId.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etFullName.setError("Name required");
            return;
        }

        if (TextUtils.isEmpty(id)) {
            etStudentId.setError("Student ID required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email required");
            return;
        }

        if (pass.length() < 8) {
            etPassword.setError("Min 8 characters");
            return;
        }

        Toast.makeText(this, "Student Account Created", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, CatalogActivity.class));
        finish();
    }
}
