package com.example.libreman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libreman.firebase.LibraryRepository;
import com.example.libreman.model.BorrowRecord;
import com.example.libreman.util.FineCalculator;

import java.util.ArrayList;
import java.util.List;

public class StudentDashboardFragment extends Fragment {

    private String memberId;
    private String memberName;
    private LibraryRepository repository;
    private BorrowedBooksAdapter borrowedAdapter;
    private List<BorrowRecord> borrowedList = new ArrayList<>();
    private TextView tvGreeting;
    private RecyclerView rvCurrentReads, rvAlerts;

    public StudentDashboardFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_student_dashboard,
                container,
                false
        );

        // Get memberId + memberName from Activity intent
        if (getActivity() != null && getActivity().getIntent() != null) {
            memberId = getActivity().getIntent().getStringExtra("MEMBER_ID");
            memberName = getActivity().getIntent().getStringExtra("MEMBER_NAME");
        }

        repository = new LibraryRepository();

        tvGreeting = view.findViewById(R.id.tvGreeting);
        rvCurrentReads = view.findViewById(R.id.rvCurrentReads);
        rvAlerts = view.findViewById(R.id.rvAlerts);

        // Set greeting
        if (memberName != null) {
            tvGreeting.setText("Hello, " + memberName + " 👋");
        }

        // Setup borrowed books RecyclerView
        rvCurrentReads.setLayoutManager(
                new LinearLayoutManager(getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false)
        );

        borrowedAdapter = new BorrowedBooksAdapter(borrowedList);
        borrowedAdapter.setOnReturnClickListener(record -> returnBook(record));
        rvCurrentReads.setAdapter(borrowedAdapter);

        // Setup alerts RecyclerView
        rvAlerts.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load data
        loadBorrowedBooks();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBorrowedBooks();
    }

    private void loadBorrowedBooks() {
        if (memberId == null) return;

        repository.getActiveRecordsByMember(memberId,
                new com.example.libreman.firebase.BorrowRecordHelper.RecordListCallback() {
                    @Override
                    public void onRecordsLoaded(List<BorrowRecord> records) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> {
                            borrowedAdapter.updateList(records);

                            // Build alerts from borrowed books
                            List<String> alerts = new ArrayList<>();
                            for (BorrowRecord r : records) {
                                if (FineCalculator.isOverdue(r.getDueDate())) {
                                    long days = FineCalculator.getOverdueDays(r.getDueDate());
                                    double fine = FineCalculator.calculateFine(r.getDueDate());
                                    alerts.add("🔴 " + r.getBookTitle() + " is overdue by " +
                                            days + " days! Fine: Rs." + (int) fine);
                                } else if (FineCalculator.isDueSoon(r.getDueDate())) {
                                    alerts.add("⚠️ " + r.getBookTitle() + " is due soon!");
                                }
                            }

                            if (alerts.isEmpty()) {
                                alerts.add("✅ No alerts — all books on time!");
                            }

                            rvAlerts.setAdapter(new AlertsAdapter(alerts));
                        });
                    }

                    @Override
                    public void onError(String error) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(),
                                        "Error: " + error, Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private void returnBook(BorrowRecord record) {
        repository.returnBook(record.getRecordId(),
                new LibraryRepository.ResultCallback() {
                    @Override
                    public void onSuccess(String message) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            loadBorrowedBooks(); // Refresh
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show());
                    }
                });
    }
}
//package com.example.libreman;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.libreman.model.Book;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class StudentDashboardFragment extends Fragment {
//
//    public StudentDashboardFragment() {}
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//
//        View view = inflater.inflate(
//                R.layout.fragment_student_dashboard,
//                container,
//                false
//        );
//
//        RecyclerView rvReads = view.findViewById(R.id.rvCurrentReads);
//        RecyclerView rvAlerts = view.findViewById(R.id.rvAlerts);
//
//        rvReads.setLayoutManager(
//                new LinearLayoutManager(getContext(),
//                        LinearLayoutManager.HORIZONTAL,
//                        false)
//        );
//
//        rvAlerts.setLayoutManager(
//                new LinearLayoutManager(getContext())
//        );
//
//        // Dummy Books
////        List<Book> books = new ArrayList<>();
////        books.add(new Book("Clean Code", "Robert Martin", "123", "ISSUED"));
////        books.add(new Book("Atomic Habits", "James Clear", "456", "ISSUED"));
////
////        rvReads.setAdapter(new CurrentReadsAdapter(books));
//
//        // Dummy Alerts
//        List<String> alerts = new ArrayList<>();
//        alerts.add("Clean Code is due tomorrow.");
//        alerts.add("New book available in your department.");
//
//        rvAlerts.setAdapter(new AlertsAdapter(alerts));
//
//        return view;
//    }
//}
