package com.example.libreman;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AdminSignupFragment extends Fragment {

    private TextInputEditText etFullName, etAdminId, etEmail, etPassword;
    private MaterialButton btnCreateAccount;
    private TextView tvLoginLink;

    public AdminSignupFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_signup, container, false);

        // Match EXACT IDs from your XML
        etFullName = view.findViewById(R.id.et_full_name);
        etAdminId = view.findViewById(R.id.et_student_id); // Your XML uses et_student_id for Admin ID
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnCreateAccount = view.findViewById(R.id.btn_create_account);
        tvLoginLink = view.findViewById(R.id.tv_login_link);

        btnCreateAccount.setOnClickListener(v -> validateSignup());

        tvLoginLink.setOnClickListener(v -> {
            // Switch to Login Fragment
            if (getActivity() instanceof AdminAuthActivity) {
                ((AdminAuthActivity) getActivity()).showLogin();
            }
        });

        return view;
    }

    private void validateSignup() {

        String name = etFullName.getText().toString().trim();
        String id = etAdminId.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etFullName.setError("Full name required");
            return;
        }

        if (TextUtils.isEmpty(id)) {
            etAdminId.setError("Admin ID required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email required");
            return;
        }

        if (password.length() < 8) {
            etPassword.setError("Minimum 8 characters");
            return;
        }

        Toast.makeText(getContext(), "Admin Account Created", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(getActivity(), MainActivity.class));
        requireActivity().finish();
    }
}
