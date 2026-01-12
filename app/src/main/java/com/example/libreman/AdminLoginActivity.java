package com.example.libreman;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AdminLoginActivity extends AppCompatActivity {

    // UI Components
    private ImageButton btnBack;
    private TextView btnLogin;
    private TextView btnSignup;
    private TextView tvForgotPassword;

    private TextInputEditText etAdminId;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;

    private MaterialButton btnLoginSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlog_in);

        // Initialize views
        initializeViews();

        // Set click listeners
        setClickListeners();

        // Handle back press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void initializeViews() {
        // Header
        btnBack = findViewById(R.id.btn_back);

        // Tabs
        btnLogin = findViewById(R.id.btn_login);
        btnSignup = findViewById(R.id.btn_signup);

        // Input fields
        etAdminId = findViewById(R.id.et_admin_id);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        // Buttons and links
        btnLoginSubmit = findViewById(R.id.btn_login_submit);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
    }

    private void setClickListeners() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Tab switching - Login (already on this screen)
        btnLogin.setOnClickListener(v -> {
            Toast.makeText(this, "Already on Login", Toast.LENGTH_SHORT).show();
        });

        // Tab switching - Sign Up
        btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(AdminLoginActivity.this, AdminSignupActivity.class);
            startActivity(intent);
            finish();
        });

        // Forgot Password
        tvForgotPassword.setOnClickListener(v -> {
            // Show toast message or dialog for password reset
            Toast.makeText(AdminLoginActivity.this,
                    "Please contact IT Support for password reset",
                    Toast.LENGTH_LONG).show();
        });

        // Login button
        btnLoginSubmit.setOnClickListener(v -> validateAndLogin());
    }

    private void validateAndLogin() {
        // Get input values
        String adminId = etAdminId.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Reset errors
        etAdminId.setError(null);
        etEmail.setError(null);
        etPassword.setError(null);

        // Validation
        boolean isValid = true;

        // Validate admin ID
        if (TextUtils.isEmpty(adminId)) {
            etAdminId.setError("Admin ID is required");
            etAdminId.requestFocus();
            isValid = false;
        } else if (adminId.length() != 6) {
            etAdminId.setError("Admin ID must be 6 digits");
            etAdminId.requestFocus();
            isValid = false;
        }

        // Validate email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            if (isValid) etEmail.requestFocus();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            if (isValid) etEmail.requestFocus();
            isValid = false;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            if (isValid) etPassword.requestFocus();
            isValid = false;
        } else if (password.length() < 8) {
            etPassword.setError("Password must be at least 8 characters");
            if (isValid) etPassword.requestFocus();
            isValid = false;
        }

        // If all validations pass, attempt login
        if (isValid) {
            performLogin(adminId, email, password);
        }
    }

    private void performLogin(String adminId, String email, String password) {
        // Show loading state
        btnLoginSubmit.setEnabled(false);
        btnLoginSubmit.setText("Logging In...");

        new android.os.Handler().postDelayed(() -> {
            // Validate credentials
            if (validateCredentials(adminId, email, password)) {
                // Login successful
                Toast.makeText(AdminLoginActivity.this,
                        "Login successful!",
                        Toast.LENGTH_SHORT).show();

                // Navigate to dashboard
                Intent intent = new Intent(AdminLoginActivity.this, CatalogActivity.class);
                intent.putExtra("admin_id", adminId);
                intent.putExtra("admin_email", email);

                // Clear the activity stack so user can't go back to login
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                // Login failed
                Toast.makeText(AdminLoginActivity.this,
                        "Invalid credentials. Please try again.",
                        Toast.LENGTH_LONG).show();

                btnLoginSubmit.setEnabled(true);
                btnLoginSubmit.setText("Log In");
            }

        }, 2000);
    }

    private boolean validateCredentials(String adminId, String email, String password) {
        // Accept any valid format
        return adminId.length() == 6 &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.length() >= 8;
    }
}