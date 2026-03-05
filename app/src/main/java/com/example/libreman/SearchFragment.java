package com.example.libreman;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libreman.firebase.BookHelper;
import com.example.libreman.firebase.LibraryRepository;
import com.example.libreman.model.Book;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private BookAdapter adapter;
    private TextView tvResultsFor, tvResultCount;
    private EditText etSearch;
    private LibraryRepository repository;

    public SearchFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize repository
        repository = new LibraryRepository();

        // ====== GET ROLE + MEMBER INFO FROM INTENT ======
        String role = "GUEST";
        String memberId = null;
        String memberName = null;

        if (getActivity() != null && getActivity().getIntent() != null) {
            role = getActivity().getIntent().getStringExtra("ROLE");
            memberId = getActivity().getIntent().getStringExtra("MEMBER_ID");
            memberName = getActivity().getIntent().getStringExtra("MEMBER_NAME");
            if (role == null) role = "GUEST";
        }

        final String finalRole = role;
        final String finalMemberId = memberId;
        final String finalMemberName = memberName;

        // Views
        RecyclerView rvBooks = view.findViewById(R.id.rvBooks);
        etSearch = view.findViewById(R.id.etSearch);
        ImageView btnClear = view.findViewById(R.id.btnClear);
        ChipGroup chipGroup = view.findViewById(R.id.chipGroup);
        tvResultsFor = view.findViewById(R.id.tvResultsFor);
        tvResultCount = view.findViewById(R.id.tvResultCount);

        // Setup adapter with empty list first
        adapter = new BookAdapter(new ArrayList<>());
        rvBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBooks.setAdapter(adapter);

        // ====== SET ROLE SO BORROW BUTTON SHOWS FOR STUDENTS ======
        adapter.setUserRole(finalRole);

        // ====== BORROW BUTTON CLICK HANDLER ======
        adapter.setOnBorrowClickListener(book -> {
            if (finalMemberId == null) {
                Toast.makeText(getContext(), "Please login first!", Toast.LENGTH_SHORT).show();
                return;
            }

            repository.borrowBook(book.getBookId(), finalMemberId, finalMemberName,
                    new LibraryRepository.ResultCallback() {
                        @Override
                        public void onSuccess(String message) {
                            if (getActivity() == null) return;
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                // Refresh book list to update available copies
                                loadBooks();
                            });
                        }

                        @Override
                        public void onFailure(String error) {
                            if (getActivity() == null) return;
                            getActivity().runOnUiThread(() ->
                                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show());
                        }
                    });
        });

        // Initial UI
        tvResultsFor.setText("All books");
        tvResultCount.setText("Loading...");

        adapter.setOnResultCountListener(count ->
                tvResultCount.setText(count + " found")
        );

        // Load books from Firebase
        loadBooks();

        // FORCE default chip selection
        chipGroup.post(() -> {
            chipGroup.check(R.id.chipBookName);
            adapter.searchType = BookAdapter.SearchType.BOOK;
        });

        // Chip selection listener
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {

            if (checkedIds.isEmpty()) return;

            int checkedId = checkedIds.get(0);

            if (checkedId == R.id.chipAuthor) {
                adapter.searchType = BookAdapter.SearchType.AUTHOR;
            } else if (checkedId == R.id.chipISBN) {
                adapter.searchType = BookAdapter.SearchType.ISBN;
            } else {
                adapter.searchType = BookAdapter.SearchType.BOOK;
            }

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

    // ====== EXTRACTED TO METHOD SO WE CAN REFRESH AFTER BORROW ======
    private void loadBooks() {
        repository.getAllBooks(new BookHelper.BookListCallback() {
            @Override
            public void onBooksLoaded(List<Book> books) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        adapter.updateList(books);
                        tvResultCount.setText(books.size() + " found");

                        for (Book book : books) {
                            Log.d("Firebase", "📖 " + book.getTitle()
                                    + " by " + book.getAuthor()
                                    + " | Status: " + book.getStatus());
                        }
                    });
                }
            }

            @Override
            public void onError(String error) {
                Log.e("Firebase", "Failed to load books: " + error);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            tvResultCount.setText("Error loading books")
                    );
                }
            }
        });
    }

    private void updateResultsText(String query) {
        if (query == null || query.isEmpty()) {
            tvResultsFor.setText("All books");
        } else {
            tvResultsFor.setText("Results for \"" + query + "\"");
        }
    }
}
//package com.example.libreman;
//
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.libreman.firebase.BookHelper;
//import com.example.libreman.firebase.LibraryRepository;
//import com.example.libreman.model.Book;
//import com.google.android.material.chip.ChipGroup;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SearchFragment extends Fragment {
//
//    private BookAdapter adapter;
//    private TextView tvResultsFor, tvResultCount;
//    private EditText etSearch;
//    private LibraryRepository repository;
//
//    public SearchFragment() { }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_search, container, false);
//
//        // Initialize repository
//        repository = new LibraryRepository();
//
//        // Views
//        RecyclerView rvBooks = view.findViewById(R.id.rvBooks);
//        etSearch = view.findViewById(R.id.etSearch);
//        ImageView btnClear = view.findViewById(R.id.btnClear);
//        ChipGroup chipGroup = view.findViewById(R.id.chipGroup);
//        tvResultsFor = view.findViewById(R.id.tvResultsFor);
//        tvResultCount = view.findViewById(R.id.tvResultCount);
//
//        // Setup adapter with empty list first
//        adapter = new BookAdapter(new ArrayList<>());
//        rvBooks.setLayoutManager(new LinearLayoutManager(getContext()));
//        rvBooks.setAdapter(adapter);
//
//        // Initial UI
//        tvResultsFor.setText("All books");
//        tvResultCount.setText("Loading...");
//
//        adapter.setOnResultCountListener(count ->
//                tvResultCount.setText(count + " found")
//        );
//
//        // Retrieve all books from Firebase
//        repository.getAllBooks(new BookHelper.BookListCallback() {
//            @Override
//            public void onBooksLoaded(List<Book> books) {
//                // Run on UI thread since Firebase callback may be on background thread
//                if (getActivity() != null) {
//                    getActivity().runOnUiThread(() -> {
//                        adapter.updateList(books);
//                        tvResultCount.setText(books.size() + " found");
//
//                        for (Book book : books) {
//                            Log.d("Firebase", "📖 " + book.getTitle()
//                                    + " by " + book.getAuthor()
//                                    + " | Status: " + book.getStatus());
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onError(String error) {
//                Log.e("Firebase", "Failed to load books: " + error);
//                if (getActivity() != null) {
//                    getActivity().runOnUiThread(() ->
//                            tvResultCount.setText("Error loading books")
//                    );
//                }
//            }
//        });
//
//        // FORCE default chip selection
//        chipGroup.post(() -> {
//            chipGroup.check(R.id.chipBookName);
//            adapter.searchType = BookAdapter.SearchType.BOOK;
//        });
//
//        // Chip selection listener
//        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
//
//            if (checkedIds.isEmpty()) return;
//
//            int checkedId = checkedIds.get(0);
//
//            if (checkedId == R.id.chipAuthor) {
//                adapter.searchType = BookAdapter.SearchType.AUTHOR;
//            } else if (checkedId == R.id.chipISBN) {
//                adapter.searchType = BookAdapter.SearchType.ISBN;
//            } else {
//                adapter.searchType = BookAdapter.SearchType.BOOK;
//            }
//
//            adapter.getFilter().filter(etSearch.getText().toString());
//        });
//
//        // Search input listener
//        etSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                btnClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
//                updateResultsText(s.toString());
//                adapter.getFilter().filter(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) { }
//        });
//
//        // Clear button
//        btnClear.setOnClickListener(v -> {
//            etSearch.setText("");
//            adapter.getFilter().filter("");
//        });
//
//        return view;
//    }
//
//    private void updateResultsText(String query) {
//        if (query == null || query.isEmpty()) {
//            tvResultsFor.setText("All books");
//        } else {
//            tvResultsFor.setText("Results for \"" + query + "\"");
//        }
//    }
//}
////package com.example.libreman;
////
////import android.os.Bundle;
////import android.text.Editable;
////import android.text.TextWatcher;
////import android.util.Log;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.EditText;
////import android.widget.ImageView;
////import android.widget.TextView;
////
////import androidx.annotation.NonNull;
////import androidx.annotation.Nullable;
////import androidx.fragment.app.Fragment;
////import androidx.recyclerview.widget.LinearLayoutManager;
////import androidx.recyclerview.widget.RecyclerView;
////
////import com.example.libreman.firebase.BookHelper;
////import com.example.libreman.model.Book;
////import com.google.android.material.chip.ChipGroup;
////
////import java.util.ArrayList;
////import java.util.List;
////
////public class SearchFragment extends Fragment {
////
////    private BookAdapter adapter;
////    private TextView tvResultsFor, tvResultCount;
////    private EditText etSearch;
////
////    public SearchFragment() { }
////
////    @Nullable
////    @Override
////    public View onCreateView(@NonNull LayoutInflater inflater,
////                             ViewGroup container,
////                             Bundle savedInstanceState) {
////
////        View view = inflater.inflate(R.layout.fragment_search, container, false);
////
////        // Views
////        RecyclerView rvBooks = view.findViewById(R.id.rvBooks);
////        etSearch = view.findViewById(R.id.etSearch);
////        ImageView btnClear = view.findViewById(R.id.btnClear);
////        ChipGroup chipGroup = view.findViewById(R.id.chipGroup);
////        tvResultsFor = view.findViewById(R.id.tvResultsFor);
////        tvResultCount = view.findViewById(R.id.tvResultCount);
////
////        // Dummy Data
////        // Retrieve all books from Firebase
////        repository.getAllBooks(new BookHelper.BookListCallback() {
////            @Override
////            public void onBooksLoaded(List<Book> books) {
////                for (Book book : books) {
////                    Log.d("Firebase", "📖 " + book.getTitle()
////                            + " by " + book.getAuthor()
////                            + " | Status: " + book.getStatus());
////                }
////
////                // Use 'books' list here — pass to adapter, update UI, etc.
////            }
////
////            @Override
////            public void onError(String error) {
////                Log.e("Firebase", "Failed to load books: " + error);
////            }
////        });
////
////        // Adapter
////        adapter = new BookAdapter(books);
////        rvBooks.setLayoutManager(new LinearLayoutManager(getContext()));
////        rvBooks.setAdapter(adapter);
////
////        // Initial UI
////        tvResultsFor.setText("All books");
////        tvResultCount.setText(books.size() + " found");
////
////        adapter.setOnResultCountListener(count ->
////                tvResultCount.setText(count + " found")
////        );
////
////        // FORCE default chip selection
////        chipGroup.post(() -> {
////            chipGroup.check(R.id.chipBookName);
////            adapter.searchType = BookAdapter.SearchType.BOOK;
////        });
////
////        // Chip selection listener
////        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
////
////            if (checkedIds.isEmpty()) return; // nothing selected
////
////            int checkedId = checkedIds.get(0);
////
////            if (checkedId == R.id.chipAuthor) {
////                adapter.searchType = BookAdapter.SearchType.AUTHOR;
////
////            } else if (checkedId == R.id.chipISBN) {
////                adapter.searchType = BookAdapter.SearchType.ISBN;
////
////            } else {
////                adapter.searchType = BookAdapter.SearchType.BOOK;
////            }
////
////            // Re-filter using current search text
////            adapter.getFilter().filter(etSearch.getText().toString());
////        });
////
////        // Search input listener
////        etSearch.addTextChangedListener(new TextWatcher() {
////            @Override
////            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
////
////            @Override
////            public void onTextChanged(CharSequence s, int start, int before, int count) {
////                btnClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
////                updateResultsText(s.toString());
////                adapter.getFilter().filter(s.toString());
////            }
////
////            @Override
////            public void afterTextChanged(Editable s) { }
////        });
////
////        // Clear button
////        btnClear.setOnClickListener(v -> {
////            etSearch.setText("");
////            adapter.getFilter().filter("");
////        });
////
////        return view;
////    }
////
////    private void updateResultsText(String query) {
////        if (query == null || query.isEmpty()) {
////            tvResultsFor.setText("All books");
////        } else {
////            tvResultsFor.setText("Results for \"" + query + "\"");
////        }
////    }
////}
