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

public class AdminLoginFragment extends Fragment {

    private TextInputEditText etAdminId, etEmail, etPassword;
    private MaterialButton btnLoginSubmit;
    private TextView tvForgotPassword;

    public AdminLoginFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_login, container, false);

        etAdminId = view.findViewById(R.id.et_admin_id);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnLoginSubmit = view.findViewById(R.id.btn_login_submit);
        tvForgotPassword = view.findViewById(R.id.tv_forgot_password);

        btnLoginSubmit.setOnClickListener(v -> validateLogin());

        return view;
    }

    private void validateLogin() {

        String id = etAdminId.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

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

        Toast.makeText(getContext(), "Admin Login Successful", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(getActivity(), MainActivity.class));
        requireActivity().finish();
    }
}
