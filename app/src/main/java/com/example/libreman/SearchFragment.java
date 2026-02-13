package com.example.libreman;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private BookAdapter adapter;
    private TextView tvResultsFor, tvResultCount;
    private EditText etSearch;

    public SearchFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Views
        RecyclerView rvBooks = view.findViewById(R.id.rvBooks);
        etSearch = view.findViewById(R.id.etSearch);
        ImageView btnClear = view.findViewById(R.id.btnClear);
        ChipGroup chipGroup = view.findViewById(R.id.chipGroup);
        tvResultsFor = view.findViewById(R.id.tvResultsFor);
        tvResultCount = view.findViewById(R.id.tvResultCount);

        // Dummy Data
        List<Book> books = new ArrayList<>();
        books.add(new Book("Clean Code", "Robert C. Martin", "9780132350884", "AVAILABLE"));
        books.add(new Book("Effective Java", "Joshua Bloch", "9780134685991", "AVAILABLE"));
        books.add(new Book("Design Patterns", "Erich Gamma", "9780201633610", "ISSUED"));

        // Adapter
        adapter = new BookAdapter(books);
        rvBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBooks.setAdapter(adapter);

        // Initial UI
        tvResultsFor.setText("All books");
        tvResultCount.setText(books.size() + " found");

        adapter.setOnResultCountListener(count ->
                tvResultCount.setText(count + " found")
        );

        // FORCE default chip selection
        chipGroup.post(() -> {
            chipGroup.check(R.id.chipBookName);
            adapter.searchType = BookAdapter.SearchType.BOOK;
        });

        // Chip selection listener
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {

            if (checkedIds.isEmpty()) return; // nothing selected

            int checkedId = checkedIds.get(0);

            if (checkedId == R.id.chipAuthor) {
                adapter.searchType = BookAdapter.SearchType.AUTHOR;

            } else if (checkedId == R.id.chipISBN) {
                adapter.searchType = BookAdapter.SearchType.ISBN;

            } else {
                adapter.searchType = BookAdapter.SearchType.BOOK;
            }

            // Re-filter using current search text
            adapter.getFilter().filter(etSearch.getText().toString());
        });

        // Search input listener
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                updateResultsText(s.toString());
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Clear button
        btnClear.setOnClickListener(v -> {
            etSearch.setText("");
            adapter.getFilter().filter("");
        });

        return view;
    }

    private void updateResultsText(String query) {
        if (query == null || query.isEmpty()) {
            tvResultsFor.setText("All books");
        } else {
            tvResultsFor.setText("Results for \"" + query + "\"");
        }
    }
}
