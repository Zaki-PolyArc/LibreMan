package com.example.libreman;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class BookSearchActivity extends AppCompatActivity {

    private BookAdapter adapter;
    private TextView tvResultsFor, tvResultCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        // -------------------- Views --------------------
        RecyclerView rvBooks = findViewById(R.id.rvBooks);
        EditText etSearch = findViewById(R.id.etSearch);
        ImageView btnClear = findViewById(R.id.btnClear);
        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        tvResultsFor = findViewById(R.id.tvResultsFor);
        tvResultCount = findViewById(R.id.tvResultCount);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        // -------------------- Bottom Navigation --------------------
        bottomNav.setSelectedItemId(R.id.nav_search);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_catalog) {
                startActivity(new Intent(this, CatalogActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }

            if (id == R.id.nav_search) {
                return true; // already here
            }

            return false;
        });

        // -------------------- Initial UI State --------------------
        btnClear.setVisibility(View.GONE);

        // -------------------- Dummy Data --------------------
        List<Book> books = new ArrayList<>();
        books.add(new Book("Clean Code", "Robert C. Martin", "9780132350884", "AVAILABLE"));
        books.add(new Book("Effective Java", "Joshua Bloch", "9780134685991", "AVAILABLE"));
        books.add(new Book("Design Patterns", "Erich Gamma", "9780201633610", "ISSUED"));

        // -------------------- RecyclerView --------------------
        adapter = new BookAdapter(books);
        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        rvBooks.setAdapter(adapter);

        // Initial result text
        tvResultsFor.setText("All books");
        tvResultCount.setText(books.size() + " found");

        // Listen for result count changes from adapter
        adapter.setOnResultCountListener(count ->
                tvResultCount.setText(count + " found")
        );

        // -------------------- Chip Selection --------------------
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.chipAuthor) {
                adapter.searchType = BookAdapter.SearchType.AUTHOR;
            } else if (checkedId == R.id.chipISBN) {
                adapter.searchType = BookAdapter.SearchType.ISBN;
            } else {
                adapter.searchType = BookAdapter.SearchType.BOOK;
            }

            adapter.getFilter().filter(etSearch.getText().toString());
        });

        // -------------------- Search Input --------------------
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                updateResultsText(s.toString());
                adapter.getFilter().filter(s.toString());
            }

            @Override public void afterTextChanged(Editable s) {}
        });

        // -------------------- Clear Button --------------------
        btnClear.setOnClickListener(v -> {
            etSearch.setText("");
            updateResultsText("");
            adapter.getFilter().filter("");
        });
    }

    // -------------------- Helper --------------------
    private void updateResultsText(String query) {
        if (query.isEmpty()) {
            tvResultsFor.setText("All books");
        } else {
            tvResultsFor.setText("Results for \"" + query + "\"");
        }
    }
}
