package com.example.libreman;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.libreman.firebase.LibraryRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class StudentLoginFragment extends Fragment {

    private TextInputEditText etStudentId, etEmail, etPassword;
    private MaterialButton btnLoginSubmit;
    private TextView tvForgotPassword;
    private LibraryRepository repository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student_login, container, false);

        etStudentId = view.findViewById(R.id.et_student_id);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnLoginSubmit = view.findViewById(R.id.btn_login_submit);
        tvForgotPassword = view.findViewById(R.id.tv_forgot_password);

        repository = new LibraryRepository();

        btnLoginSubmit.setOnClickListener(v -> validateAndLogin());

        tvForgotPassword.setOnClickListener(v ->
                Toast.makeText(getContext(), "Reset feature coming soon", Toast.LENGTH_SHORT).show()
        );

        return view;
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
            etPassword.setError("Minimum 8 characters");
            return;
        }

        btnLoginSubmit.setEnabled(false);
        btnLoginSubmit.setText("Logging in...");

        repository.loginStudent(studentId, email, password,
                new LibraryRepository.ResultCallback() {
                    @Override
                    public void onSuccess(String memberName) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(),
                                    "Welcome back, " + memberName + "!",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("ROLE", "STUDENT");
                            intent.putExtra("MEMBER_ID", studentId);
                            intent.putExtra("MEMBER_NAME", memberName);
                            startActivity(intent);
                            requireActivity().finish();
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> {
                            btnLoginSubmit.setEnabled(true);
                            btnLoginSubmit.setText("Login");
                            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                        });
                    }
                });
    }
}
//package com.example.libreman;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Patterns;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.fragment.app.Fragment;
//
//import com.google.android.material.button.MaterialButton;
//import com.google.android.material.textfield.TextInputEditText;
//
//public class StudentLoginFragment extends Fragment {
//
//    private TextInputEditText etStudentId, etEmail, etPassword;
//    private MaterialButton btnLoginSubmit;
//    private TextView tvForgotPassword;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_student_login, container, false);
//
//        etStudentId = view.findViewById(R.id.et_student_id);
//        etEmail = view.findViewById(R.id.et_email);
//        etPassword = view.findViewById(R.id.et_password);
//        btnLoginSubmit = view.findViewById(R.id.btn_login_submit);
//        tvForgotPassword = view.findViewById(R.id.tv_forgot_password);
//
//        btnLoginSubmit.setOnClickListener(v -> validateAndLogin());
//
//        tvForgotPassword.setOnClickListener(v ->
//                Toast.makeText(getContext(), "Reset feature coming soon", Toast.LENGTH_SHORT).show()
//        );
//
//        return view;
//    }
//
//    private void validateAndLogin() {
//
//        String studentId = etStudentId.getText().toString().trim();
//        String email = etEmail.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
//
//        if (TextUtils.isEmpty(studentId)) {
//            etStudentId.setError("Student ID required");
//            return;
//        }
//
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            etEmail.setError("Valid email required");
//            return;
//        }
//
//        if (password.length() < 8) {
//            etPassword.setError("Minimum 8 characters");
//            return;
//        }
//
//        Toast.makeText(getContext(), "Student Login Successful", Toast.LENGTH_SHORT).show();
//
//        startActivity(new Intent(getActivity(), MainActivity.class));
//        requireActivity().finish();
//    }
//}
