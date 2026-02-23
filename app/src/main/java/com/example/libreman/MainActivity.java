package com.example.libreman;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private String role = "STUDENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);

        // Initialize Firebase
        FirebaseDatabase.getInstance("https://libreman-f9839-default-rtdb.asia-southeast1.firebasedatabase.app/");

        bottomNavigation = findViewById(R.id.bottomNavigation);

        String receivedRole = getIntent().getStringExtra("ROLE");
        if (receivedRole != null) role = receivedRole;

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
//package com.example.libreman;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//
//import com.example.libreman.firebase.LibraryRepository;
//import com.example.libreman.model.Book;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class MainActivity extends AppCompatActivity {
//
//    private BottomNavigationView bottomNavigation;
//    private String role = "STUDENT";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_container);
//
//        // ============ FIREBASE CONNECTION ============
//        FirebaseDatabase database = FirebaseDatabase
//                .getInstance("https://libreman-f9839-default-rtdb.asia-southeast1.firebasedatabase.app/");
//
//        database.getReference("test")
//                .setValue("Firebase is connected!")
//                .addOnSuccessListener(unused -> {
//                    Log.d("Firebase", "✅ CONNECTED");
//                    Toast.makeText(this, "✅ Firebase Connected!", Toast.LENGTH_SHORT).show();
//                    storeInitialBooks();
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("Firebase", "❌ FAILED — " + e.getMessage());
//                    Toast.makeText(this, "❌ Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                });
//        // ============ END FIREBASE ============
//
//        bottomNavigation = findViewById(R.id.bottomNavigation);
//
//        String receivedRole = getIntent().getStringExtra("ROLE");
//        if (receivedRole != null) role = receivedRole;
//
//        if (savedInstanceState == null) {
//            if (role.equals("ADMIN")) {
//                loadFragment(new AdminDashboardFragment());
//            } else {
//                loadFragment(new StudentDashboardFragment());
//            }
//            bottomNavigation.setSelectedItemId(R.id.nav_home);
//        }
//
//        bottomNavigation.setOnItemSelectedListener(item -> {
//
//            int id = item.getItemId();
//            Fragment current =
//                    getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
//
//            if (id == R.id.nav_home) {
//                if (role.equals("ADMIN") && current instanceof AdminDashboardFragment) return true;
//                if (role.equals("STUDENT") && current instanceof StudentDashboardFragment) return true;
//
//                if (role.equals("ADMIN")) {
//                    loadFragment(new AdminDashboardFragment());
//                } else {
//                    loadFragment(new StudentDashboardFragment());
//                }
//                return true;
//            }
//
//            if (id == R.id.nav_catalog && current instanceof CatalogFragment) return true;
//            if (id == R.id.nav_search && current instanceof SearchFragment) return true;
//
//            if (id == R.id.nav_catalog) {
//                loadFragment(new CatalogFragment());
//                return true;
//            }
//
//            if (id == R.id.nav_search) {
//                loadFragment(new SearchFragment());
//                return true;
//            }
//
//            Toast.makeText(this, "New features incoming", Toast.LENGTH_SHORT).show();
//            return true;
//        });
//    }
//
//    // ============ REMOVE THIS METHOD AFTER FIRST SUCCESSFUL RUN ============
//    private void storeInitialBooks() {
//        LibraryRepository repository = new LibraryRepository();
//
//        Book book1 = new Book("Clean Code", "Robert C. Martin", "9780132350884", "Programming", 3);
//        repository.insertBook(book1, new LibraryRepository.ResultCallback() {
//            @Override
//            public void onSuccess(String message) {
//                Log.d("Firebase", "✅ Book 1: " + message);
//                Toast.makeText(MainActivity.this, "Book 1 added!", Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onFailure(String error) { Log.e("Firebase", "❌ Book 1: " + error); }
//        });
//
//        Book book2 = new Book("Effective Java", "Joshua Bloch", "9780134685991", "Programming", 2);
//        repository.insertBook(book2, new LibraryRepository.ResultCallback() {
//            @Override
//            public void onSuccess(String message) { Log.d("Firebase", "✅ Book 2: " + message); }
//            @Override
//            public void onFailure(String error) { Log.e("Firebase", "❌ Book 2: " + error); }
//        });
//
//        Book book3 = new Book("Design Patterns", "Erich Gamma", "9780201633610", "Programming", 1);
//        repository.insertBook(book3, new LibraryRepository.ResultCallback() {
//            @Override
//            public void onSuccess(String message) { Log.d("Firebase", "✅ Book 3: " + message); }
//            @Override
//            public void onFailure(String error) { Log.e("Firebase", "❌ Book 3: " + error); }
//        });
//    }
//    // ============ END REMOVE ============
//
//    private void loadFragment(@NonNull Fragment fragment) {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragmentContainer, fragment)
//                .commit();
//    }
//}
////package com.example.libreman;
////
////import android.os.Bundle;
////import android.util.Log;
////import android.widget.Toast;
////
////import androidx.annotation.NonNull;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.fragment.app.Fragment;
////
////import com.example.libreman.firebase.LibraryRepository;
////import com.example.libreman.model.Book;
////import com.google.android.material.bottomnavigation.BottomNavigationView;
////import com.google.firebase.database.FirebaseDatabase;
////
////public class MainActivity extends AppCompatActivity {
////
////    private BottomNavigationView bottomNavigation;
////    private String role = "STUDENT"; // default
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main_container);
////
////        // ============ FIREBASE TEST — Remove after confirming it works ============
////        FirebaseDatabase.getInstance().getReference("test")
////                .setValue("Firebase is connected!")
////                .addOnSuccessListener(unused -> {
////                    Log.d("Firebase", "✅ CONNECTED — write successful");
////                    Toast.makeText(this, "Firebase Connected!", Toast.LENGTH_SHORT).show();
////                })
////                .addOnFailureListener(e -> {
////                    Log.e("Firebase", "❌ FAILED — " + e.getMessage());
////                    Toast.makeText(this, "Firebase Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
////                });
////
////        // ============ STORE BOOKS — Remove after first run ============
////        LibraryRepository repository = new LibraryRepository();
////
////        Book book1 = new Book("Clean Code", "Robert C. Martin", "9780132350884", "Programming", 3);
////        repository.insertBook(book1, new LibraryRepository.ResultCallback() {
////            @Override
////            public void onSuccess(String message) { Log.d("Firebase", "✅ Book 1: " + message); }
////            @Override
////            public void onFailure(String error) { Log.e("Firebase", "❌ Book 1: " + error); }
////        });
////
////        Book book2 = new Book("Effective Java", "Joshua Bloch", "9780134685991", "Programming", 2);
////        repository.insertBook(book2, new LibraryRepository.ResultCallback() {
////            @Override
////            public void onSuccess(String message) { Log.d("Firebase", "✅ Book 2: " + message); }
////            @Override
////            public void onFailure(String error) { Log.e("Firebase", "❌ Book 2: " + error); }
////        });
////
////        Book book3 = new Book("Design Patterns", "Erich Gamma", "9780201633610", "Programming", 1);
////        repository.insertBook(book3, new LibraryRepository.ResultCallback() {
////            @Override
////            public void onSuccess(String message) { Log.d("Firebase", "✅ Book 3: " + message); }
////            @Override
////            public void onFailure(String error) { Log.e("Firebase", "❌ Book 3: " + error); }
////        });
////        // ============ END FIREBASE TEST ============
////
////        bottomNavigation = findViewById(R.id.bottomNavigation);
////
////        // Get role
////        String receivedRole = getIntent().getStringExtra("ROLE");
////        if (receivedRole != null) role = receivedRole;
////
////        // Load correct dashboard
////        if (savedInstanceState == null) {
////            if (role.equals("ADMIN")) {
////                loadFragment(new AdminDashboardFragment());
////            } else {
////                loadFragment(new StudentDashboardFragment());
////            }
////            bottomNavigation.setSelectedItemId(R.id.nav_home);
////        }
////
////        bottomNavigation.setOnItemSelectedListener(item -> {
////
////            int id = item.getItemId();
////            Fragment current =
////                    getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
////
////            // Prevent reload of same fragment
////            if (id == R.id.nav_home) {
////                if (role.equals("ADMIN") && current instanceof AdminDashboardFragment) return true;
////                if (role.equals("STUDENT") && current instanceof StudentDashboardFragment) return true;
////
////                if (role.equals("ADMIN")) {
////                    loadFragment(new AdminDashboardFragment());
////                } else {
////                    loadFragment(new StudentDashboardFragment());
////                }
////                return true;
////            }
////
////            if (id == R.id.nav_catalog && current instanceof CatalogFragment) return true;
////            if (id == R.id.nav_search && current instanceof SearchFragment) return true;
////
////            if (id == R.id.nav_catalog) {
////                loadFragment(new CatalogFragment());
////                return true;
////            }
////
////            if (id == R.id.nav_search) {
////                loadFragment(new SearchFragment());
////                return true;
////            }
////
////            Toast.makeText(this, "New features incoming", Toast.LENGTH_SHORT).show();
////            return true;
////        });
////    }
////
////    private void loadFragment(@NonNull Fragment fragment) {
////        getSupportFragmentManager()
////                .beginTransaction()
////                .replace(R.id.fragmentContainer, fragment)
////                .commit();
////    }
////}
////package com.example.libreman;
////
////import android.os.Bundle;
////import android.widget.Toast;
////
////import androidx.annotation.NonNull;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.fragment.app.Fragment;
////
////import com.google.android.material.bottomnavigation.BottomNavigationView;
////
////public class MainActivity extends AppCompatActivity {
////
////    private BottomNavigationView bottomNavigation;
////    private String role = "STUDENT"; // default
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main_container);
////
////        bottomNavigation = findViewById(R.id.bottomNavigation);
////
////        // Get role
////        String receivedRole = getIntent().getStringExtra("ROLE");
////        if (receivedRole != null) role = receivedRole;
////
////        // Load correct dashboard
////        if (savedInstanceState == null) {
////            if (role.equals("ADMIN")) {
////                loadFragment(new AdminDashboardFragment());
////            } else {
////                loadFragment(new StudentDashboardFragment());
////            }
////            bottomNavigation.setSelectedItemId(R.id.nav_home);
////        }
////
////        bottomNavigation.setOnItemSelectedListener(item -> {
////
////            int id = item.getItemId();
////            Fragment current =
////                    getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
////
////            // Prevent reload of same fragment
////            if (id == R.id.nav_home) {
////                if (role.equals("ADMIN") && current instanceof AdminDashboardFragment) return true;
////                if (role.equals("STUDENT") && current instanceof StudentDashboardFragment) return true;
////
////                if (role.equals("ADMIN")) {
////                    loadFragment(new AdminDashboardFragment());
////                } else {
////                    loadFragment(new StudentDashboardFragment());
////                }
////                return true;
////            }
////
////            if (id == R.id.nav_catalog && current instanceof CatalogFragment) return true;
////            if (id == R.id.nav_search && current instanceof SearchFragment) return true;
////
////            if (id == R.id.nav_catalog) {
////                loadFragment(new CatalogFragment());
////                return true;
////            }
////
////            if (id == R.id.nav_search) {
////                loadFragment(new SearchFragment());
////                return true;
////            }
////
////            Toast.makeText(this, "New features incoming", Toast.LENGTH_SHORT).show();
////            return true;
////        });
////    }
////
////    private void loadFragment(@NonNull Fragment fragment) {
////        getSupportFragmentManager()
////                .beginTransaction()
////                .replace(R.id.fragmentContainer, fragment)
////                .commit();
////    }
////}
