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

public class AdminSignupActivity extends AppCompatActivity {

    // UI Components
    private ImageButton btnBack;
    private TextView btnLogin;
    private TextView btnSignup;
    private TextView tvLoginLink;

    private TextInputEditText etFullName;
    private TextInputEditText etStudentId;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;

    private MaterialButton btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminsignup_in);

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
        etFullName = findViewById(R.id.et_full_name);
        etStudentId = findViewById(R.id.et_student_id);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        // Buttons
        btnCreateAccount = findViewById(R.id.btn_create_account);
        tvLoginLink = findViewById(R.id.tv_login_link);
    }

    private void setClickListeners() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Tab switching - Login
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(AdminSignupActivity.this, AdminLoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Tab switching - Sign Up (already on this screen)
        btnSignup.setOnClickListener(v -> {
            Toast.makeText(this, "Already on Sign Up", Toast.LENGTH_SHORT).show();
        });

        // Create Account button
        btnCreateAccount.setOnClickListener(v -> validateAndCreateAccount());

        // Login link at bottom
        tvLoginLink.setOnClickListener(v -> {
            Intent intent = new Intent(AdminSignupActivity.this, AdminLoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void validateAndCreateAccount() {
        // Get input values
        String fullName = etFullName.getText().toString().trim();
        String adminId = etStudentId.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Reset errors
        etFullName.setError(null);
        etStudentId.setError(null);
        etEmail.setError(null);
        etPassword.setError(null);

        // Validation
        boolean isValid = true;

        // Validate full name
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            isValid = false;
        } else if (fullName.length() < 2) {
            etFullName.setError("Please enter a valid name");
            etFullName.requestFocus();
            isValid = false;
        }

        // Validate admin ID
        if (TextUtils.isEmpty(adminId)) {
            etStudentId.setError("Admin ID is required");
            if (isValid) etStudentId.requestFocus();
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

        // If all validations pass, create account
        if (isValid) {
            createAccount(fullName, adminId, email, password);
        }
    }

    private void createAccount(String fullName, String adminId, String email, String password) {
        // Show loading state
        btnCreateAccount.setEnabled(false);
        btnCreateAccount.setText("Creating Account...");

        new android.os.Handler().postDelayed(() -> {
            // Simulate successful account creation
            Toast.makeText(AdminSignupActivity.this,
                    "Account created successfully!",
                    Toast.LENGTH_LONG).show();

            // Navigate to dashboard
            Intent intent = new Intent(AdminSignupActivity.this, CatalogActivity.class);
            intent.putExtra("admin_name", fullName);
            intent.putExtra("admin_id", adminId);
            intent.putExtra("admin_email", email);
            startActivity(intent);
            finish();

        }, 2000);

        // Reset button after delay
        new android.os.Handler().postDelayed(() -> {
            btnCreateAccount.setEnabled(true);
            btnCreateAccount.setText("Create Account");
        }, 2000);
    }
}