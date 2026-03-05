package com.example.libreman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libreman.model.Book;

import java.util.ArrayList;
import java.util.List;

public class StudentDashboardFragment extends Fragment {

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

        RecyclerView rvReads = view.findViewById(R.id.rvCurrentReads);
        RecyclerView rvAlerts = view.findViewById(R.id.rvAlerts);

        rvReads.setLayoutManager(
                new LinearLayoutManager(getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false)
        );

        rvAlerts.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        // Dummy Books
//        List<Book> books = new ArrayList<>();
//        books.add(new Book("Clean Code", "Robert Martin", "123", "ISSUED"));
//        books.add(new Book("Atomic Habits", "James Clear", "456", "ISSUED"));
//
//        rvReads.setAdapter(new CurrentReadsAdapter(books));

        // Dummy Alerts
        List<String> alerts = new ArrayList<>();
        alerts.add("Clean Code is due tomorrow.");
        alerts.add("New book available in your department.");

        rvAlerts.setAdapter(new AlertsAdapter(alerts));

        return view;
    }
}
