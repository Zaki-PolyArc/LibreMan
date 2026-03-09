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

import androidx.fragment.app.Fragment;

import com.example.libreman.firebase.LibraryRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class StudentSignupFragment extends Fragment {

    private TextInputEditText etFullName, etStudentId, etEmail, etPassword;
    private MaterialButton btnCreateAccount, btnGuest;
    private TextView tvLoginLink;
    private LibraryRepository repository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student_signup, container, false);

        etFullName = view.findViewById(R.id.et_full_name);
        etStudentId = view.findViewById(R.id.et_signup_student_id);
        etEmail = view.findViewById(R.id.et_signup_student_email);
        etPassword = view.findViewById(R.id.et_signup_password);
        btnCreateAccount = view.findViewById(R.id.btn_create_account);
        btnGuest = view.findViewById(R.id.btn_guest);
        tvLoginLink = view.findViewById(R.id.tv_login_link);

        repository = new LibraryRepository();

        btnCreateAccount.setOnClickListener(v -> createAccount());

        btnGuest.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("ROLE", "GUEST");
            startActivity(intent);
            requireActivity().finish();
        });

        tvLoginLink.setOnClickListener(v ->
                Toast.makeText(getContext(), "Switch to Login tab", Toast.LENGTH_SHORT).show()
        );

        return view;
    }

    private void createAccount() {

        String name = etFullName.getText().toString().trim();
        String studentId = etStudentId.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etFullName.setError("Name required");
            return;
        }

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

        btnCreateAccount.setEnabled(false);
        btnCreateAccount.setText("Creating...");

        repository.signupStudent(studentId, name, email, password,
                new LibraryRepository.ResultCallback() {
                    @Override
                    public void onSuccess(String message) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(),
                                    "Account created! Welcome, " + name,
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("ROLE", "STUDENT");
                            intent.putExtra("MEMBER_ID", studentId);
                            intent.putExtra("MEMBER_NAME", name);
                            startActivity(intent);
                            requireActivity().finish();
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> {
                            btnCreateAccount.setEnabled(true);
                            btnCreateAccount.setText("Create Account");
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
//public class StudentSignupFragment extends Fragment {
//
//    private TextInputEditText etFullName, etStudentId, etEmail, etPassword;
//    private MaterialButton btnCreateAccount, btnGuest;
//    private TextView tvLoginLink;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_student_signup, container, false);
//
//        etFullName = view.findViewById(R.id.et_full_name);
//        etStudentId = view.findViewById(R.id.et_signup_student_id);
//        etEmail = view.findViewById(R.id.et_signup_student_email);
//        etPassword = view.findViewById(R.id.et_signup_password);
//        btnCreateAccount = view.findViewById(R.id.btn_create_account);
//        btnGuest = view.findViewById(R.id.btn_guest);
//        tvLoginLink = view.findViewById(R.id.tv_login_link);
//
//        btnCreateAccount.setOnClickListener(v -> createAccount());
//
//        btnGuest.setOnClickListener(v -> {
//            startActivity(new Intent(getActivity(), MainActivity.class));
//            requireActivity().finish();
//        });
//
//        tvLoginLink.setOnClickListener(v ->
//                Toast.makeText(getContext(), "Switch to Login tab", Toast.LENGTH_SHORT).show()
//        );
//
//        return view;
//    }
//
//    private void createAccount() {
//
//        String name = etFullName.getText().toString().trim();
//        String studentId = etStudentId.getText().toString().trim();
//        String email = etEmail.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
//
//        if (TextUtils.isEmpty(name)) {
//            etFullName.setError("Name required");
//            return;
//        }
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
//        Toast.makeText(getContext(), "Student Account Created", Toast.LENGTH_SHORT).show();
//
//        startActivity(new Intent(getActivity(), MainActivity.class));
//        requireActivity().finish();
//    }
//}
