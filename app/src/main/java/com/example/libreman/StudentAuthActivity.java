package com.example.libreman;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class StudentAuthActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tabLogin, tabSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_auth);

        btnBack = findViewById(R.id.btn_back);
        tabLogin = findViewById(R.id.tab_login);
        tabSignup = findViewById(R.id.tab_signup);

        btnBack.setOnClickListener(v -> finish());

        tabLogin.setOnClickListener(v -> {
            loadFragment(new StudentLoginFragment());
            selectLoginTab();
        });

        tabSignup.setOnClickListener(v -> {
            loadFragment(new StudentSignupFragment());
            selectSignupTab();
        });

        // Default screen
        loadFragment(new StudentLoginFragment());
        selectLoginTab();

        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        finish();
                    }
                });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void selectLoginTab() {
        tabLogin.setBackgroundResource(R.drawable.bg_tab_selected);
        tabSignup.setBackgroundResource(R.drawable.bg_tab_unselected);
    }

    private void selectSignupTab() {
        tabLogin.setBackgroundResource(R.drawable.bg_tab_unselected);
        tabSignup.setBackgroundResource(R.drawable.bg_tab_selected);
    }
}
