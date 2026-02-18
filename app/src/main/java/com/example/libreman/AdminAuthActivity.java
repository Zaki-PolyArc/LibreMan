package com.example.libreman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class AdminAuthActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView btnLoginTab, btnSignupTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_auth);

        btnBack = findViewById(R.id.btn_back);
        btnLoginTab = findViewById(R.id.btn_login);
        btnSignupTab = findViewById(R.id.btn_signup);

        btnBack.setOnClickListener(v -> finish());

        btnLoginTab.setOnClickListener(v -> {
            showLogin();
        });

        btnSignupTab.setOnClickListener(v -> {
            showSignup();
        });

        // Default screen
        showLogin();

        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        finish();
                    }
                });
    }

    public void showLogin() {
        replaceFragment(new AdminLoginFragment());

        btnLoginTab.setBackgroundResource(R.drawable.bg_tab_selected);
        btnSignupTab.setBackgroundResource(R.drawable.bg_tab_unselected);
    }

    private void showSignup() {
        replaceFragment(new AdminSignupFragment());

        btnLoginTab.setBackgroundResource(R.drawable.bg_tab_unselected);
        btnSignupTab.setBackgroundResource(R.drawable.bg_tab_selected);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
