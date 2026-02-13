package com.example.libreman;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationHelper{

    public static void setupBottomNav(MainActivity activity, BottomNavigationView bottomNav) {

        bottomNav.setOnItemSelectedListener(item -> {

            Fragment fragment = null;

            int id = item.getItemId();

            if (id == R.id.nav_catalog) {
                fragment = new CatalogFragment();
            }
            else if (id == R.id.nav_search) {
                fragment = new SearchFragment();
            }
            else {
                Toast.makeText(activity, "New features incoming", Toast.LENGTH_SHORT).show();
                return false;
            }

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();

            return true;
        });
    }
}
