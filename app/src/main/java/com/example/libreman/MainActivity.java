package com.example.libreman;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private String role = "STUDENT"; // default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Get role
        String receivedRole = getIntent().getStringExtra("ROLE");
        if (receivedRole != null) role = receivedRole;

        // Load correct dashboard
        if (savedInstanceState == null) {
            if (role.equals("ADMIN")) {
                loadFragment(new AdminDashboardFragment());
            } else {
                loadFragment(new StudentDashboardFragment());
            }
            bottomNavigation.setSelectedItemId(R.id.nav_home);
        }

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            Fragment current =
                    getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

            // Prevent reload of same fragment
            if (id == R.id.nav_home) {
                if (role.equals("ADMIN") && current instanceof AdminDashboardFragment) return true;
                if (role.equals("STUDENT") && current instanceof StudentDashboardFragment) return true;

                if (role.equals("ADMIN")) {
                    loadFragment(new AdminDashboardFragment());
                } else {
                    loadFragment(new StudentDashboardFragment());
                }
                return true;
            }

            if (id == R.id.nav_catalog && current instanceof CatalogFragment) return true;
            if (id == R.id.nav_search && current instanceof SearchFragment) return true;

            if (id == R.id.nav_catalog) {
                loadFragment(new CatalogFragment());
                return true;
            }

            if (id == R.id.nav_search) {
                loadFragment(new SearchFragment());
                return true;
            }

            Toast.makeText(this, "New features incoming", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void loadFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
