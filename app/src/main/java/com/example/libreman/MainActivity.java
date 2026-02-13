package com.example.libreman;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new CatalogFragment());
            bottomNavigation.setSelectedItemId(R.id.nav_catalog);
        }

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            Fragment currentFragment =
                    getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

            // Prevent reloading same fragment
            if (id == R.id.nav_catalog && currentFragment instanceof CatalogFragment) {
                return true;
            }

            if (id == R.id.nav_search && currentFragment instanceof SearchFragment) {
                return true;
            }

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

    // Proper back behavior
    @Override
    public void onBackPressed() {

        Fragment currentFragment =
                getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        // If not on Catalog, go to Catalog
        if (!(currentFragment instanceof CatalogFragment)) {
            bottomNavigation.setSelectedItemId(R.id.nav_catalog);
            loadFragment(new CatalogFragment());
        } else {
            super.onBackPressed(); // exit app
        }
    }
}
