package com.example.libreman;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.libreman.firebase.BookHelper;
import com.example.libreman.firebase.BorrowRecordHelper;
import com.example.libreman.firebase.LibraryRepository;
import com.example.libreman.firebase.MemberHelper;
import com.example.libreman.model.Book;
import com.example.libreman.model.BorrowRecord;
import com.example.libreman.model.Member;
import com.example.libreman.util.FineCalculator;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardFragment extends Fragment {

    private LibraryRepository repository;
    private RecyclerView rvStats, rvOverdue, rvMembers, rvActiveBorrows;
    private OverdueAdapter overdueAdapter;
    private ActiveBorrowsAdapter activeBorrowsAdapter;
    private List<BorrowRecord> overdueList = new ArrayList<>();
    private List<BorrowRecord> activeBorrowList = new ArrayList<>();

    public AdminDashboardFragment() {
        super(R.layout.fragment_admin_dashboard);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        repository = new LibraryRepository();

        rvStats = view.findViewById(R.id.rvStats);
        rvOverdue = view.findViewById(R.id.rvOverdue);
        rvMembers = view.findViewById(R.id.rvMembers);
        rvActiveBorrows = view.findViewById(R.id.rvActiveBorrows);

        rvStats.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvOverdue.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMembers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvActiveBorrows.setLayoutManager(new LinearLayoutManager(getContext()));

        // Setup adapters
        overdueAdapter = new OverdueAdapter(overdueList);
        rvOverdue.setAdapter(overdueAdapter);

        activeBorrowsAdapter = new ActiveBorrowsAdapter(activeBorrowList);
        rvActiveBorrows.setAdapter(activeBorrowsAdapter);

        // Load everything
        loadStats();
        loadOverdueRecords();
        loadActiveBorrows();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStats();
        loadOverdueRecords();
        loadActiveBorrows();
    }

    private void loadStats() {
        repository.getAllBooks(new BookHelper.BookListCallback() {
            @Override
            public void onBooksLoaded(List<Book> books) {
                int totalBooks = books.size();
                int totalCopies = 0;
                int availableCopies = 0;
                for (Book b : books) {
                    totalCopies += b.getTotalCopies();
                    availableCopies += b.getAvailableCopies();
                }
                int borrowed = totalCopies - availableCopies;

                int finalAvailableCopies = availableCopies;
                int finalBorrowed = borrowed;

                repository.getOverdueRecords(new BorrowRecordHelper.RecordListCallback() {
                    @Override
                    public void onRecordsLoaded(List<BorrowRecord> records) {
                        double totalFines = 0;
                        for (BorrowRecord r : records) {
                            totalFines += FineCalculator.calculateFine(r.getDueDate());
                        }

                        if (getActivity() == null) return;
                        double finalTotalFines = totalFines;
                        int overdueCount = records.size();

                        getActivity().runOnUiThread(() -> {
                            List<StatItem> stats = new ArrayList<>();
                            stats.add(new StatItem("📚 Total Books", String.valueOf(totalBooks)));
                            stats.add(new StatItem("✅ Available", String.valueOf(finalAvailableCopies)));
                            stats.add(new StatItem("📖 Borrowed", String.valueOf(finalBorrowed)));
                            stats.add(new StatItem("⚠️ Overdue", String.valueOf(overdueCount)));
                            stats.add(new StatItem("💰 Pending Fines", "Rs." + (int) finalTotalFines));

                            rvStats.setAdapter(new StatsAdapter(stats));
                        });
                    }

                    @Override
                    public void onError(String error) {}
                });
            }

            @Override
            public void onError(String error) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void loadOverdueRecords() {
        repository.getOverdueRecords(new BorrowRecordHelper.RecordListCallback() {
            @Override
            public void onRecordsLoaded(List<BorrowRecord> records) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> overdueAdapter.updateList(records));
            }

            @Override
            public void onError(String error) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void loadActiveBorrows() {
        repository.getAllActiveRecords(new BorrowRecordHelper.RecordListCallback() {
            @Override
            public void onRecordsLoaded(List<BorrowRecord> records) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> activeBorrowsAdapter.updateList(records));
            }

            @Override
            public void onError(String error) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show());
            }
        });
    }

    // ======================== INNER CLASSES ========================

    public static class StatItem {
        public String label;
        public String value;

        public StatItem(String label, String value) {
            this.label = label;
            this.value = value;
        }
    }

    public static class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder> {

        private final List<StatItem> stats;

        public StatsAdapter(List<StatItem> stats) {
            this.stats = stats;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_stat_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            StatItem item = stats.get(position);
            holder.tvLabel.setText(item.label);
            holder.tvValue.setText(item.value);
        }

        @Override
        public int getItemCount() { return stats.size(); }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvLabel, tvValue;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvLabel = itemView.findViewById(R.id.tvStatLabel);
                tvValue = itemView.findViewById(R.id.tvStatValue);
            }
        }
    }
}
//package com.example.libreman;
//
//import android.os.Bundle;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.libreman.firebase.BookHelper;
//import com.example.libreman.firebase.BorrowRecordHelper;
//import com.example.libreman.firebase.LibraryRepository;
//import com.example.libreman.firebase.MemberHelper;
//import com.example.libreman.model.Book;
//import com.example.libreman.model.BorrowRecord;
//import com.example.libreman.model.Member;
//import com.example.libreman.util.FineCalculator;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AdminDashboardFragment extends Fragment {
//
//    private LibraryRepository repository;
//    private RecyclerView rvStats, rvOverdue, rvMembers;
//    private OverdueAdapter overdueAdapter;
//    private List<BorrowRecord> overdueList = new ArrayList<>();
//
//    public AdminDashboardFragment() {
//        super(R.layout.fragment_admin_dashboard);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//
//        repository = new LibraryRepository();
//
//        rvStats = view.findViewById(R.id.rvStats);
//        rvOverdue = view.findViewById(R.id.rvOverdue);
//        rvMembers = view.findViewById(R.id.rvMembers);
//
//        rvStats.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        rvOverdue.setLayoutManager(new LinearLayoutManager(getContext()));
//        rvMembers.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        // Setup overdue adapter
//        overdueAdapter = new OverdueAdapter(overdueList);
//        rvOverdue.setAdapter(overdueAdapter);
//
//        // Load everything
//        loadStats();
//        loadOverdueRecords();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        loadStats();
//        loadOverdueRecords();
//    }
//
//    private void loadStats() {
//        // Get total books + available copies
//        repository.getAllBooks(new BookHelper.BookListCallback() {
//            @Override
//            public void onBooksLoaded(List<Book> books) {
//                int totalBooks = books.size();
//                int totalCopies = 0;
//                int availableCopies = 0;
//                for (Book b : books) {
//                    totalCopies += b.getTotalCopies();
//                    availableCopies += b.getAvailableCopies();
//                }
//                int borrowed = totalCopies - availableCopies;
//
//                // Get overdue count
//                int finalAvailableCopies = availableCopies;
//                int finalBorrowed = borrowed;
//
//                repository.getOverdueRecords(new BorrowRecordHelper.RecordListCallback() {
//                    @Override
//                    public void onRecordsLoaded(List<BorrowRecord> records) {
//                        // Calculate total fines
//                        double totalFines = 0;
//                        for (BorrowRecord r : records) {
//                            totalFines += FineCalculator.calculateFine(r.getDueDate());
//                        }
//
//                        if (getActivity() == null) return;
//                        double finalTotalFines = totalFines;
//                        int overdueCount = records.size();
//
//                        getActivity().runOnUiThread(() -> {
//                            List<StatItem> stats = new ArrayList<>();
//                            stats.add(new StatItem("📚 Total Books", String.valueOf(totalBooks)));
//                            stats.add(new StatItem("✅ Available", String.valueOf(finalAvailableCopies)));
//                            stats.add(new StatItem("📖 Borrowed", String.valueOf(finalBorrowed)));
//                            stats.add(new StatItem("⚠️ Overdue", String.valueOf(overdueCount)));
//                            stats.add(new StatItem("💰 Pending Fines", "Rs." + (int) finalTotalFines));
//
//                            rvStats.setAdapter(new StatsAdapter(stats));
//                        });
//                    }
//
//                    @Override
//                    public void onError(String error) {}
//                });
//            }
//
//            @Override
//            public void onError(String error) {
//                if (getActivity() == null) return;
//                getActivity().runOnUiThread(() ->
//                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show());
//            }
//        });
//    }
//
//    private void loadOverdueRecords() {
//        repository.getOverdueRecords(new BorrowRecordHelper.RecordListCallback() {
//            @Override
//            public void onRecordsLoaded(List<BorrowRecord> records) {
//                if (getActivity() == null) return;
//                getActivity().runOnUiThread(() -> overdueAdapter.updateList(records));
//            }
//
//            @Override
//            public void onError(String error) {
//                if (getActivity() == null) return;
//                getActivity().runOnUiThread(() ->
//                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show());
//            }
//        });
//    }
//
//    // ======================== INNER CLASSES ========================
//
//    public static class StatItem {
//        public String label;
//        public String value;
//
//        public StatItem(String label, String value) {
//            this.label = label;
//            this.value = value;
//        }
//    }
//
//    public static class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder> {
//
//        private final List<StatItem> stats;
//
//        public StatsAdapter(List<StatItem> stats) {
//            this.stats = stats;
//        }
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.item_stat_card, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            StatItem item = stats.get(position);
//            holder.tvLabel.setText(item.label);
//            holder.tvValue.setText(item.value);
//        }
//
//        @Override
//        public int getItemCount() { return stats.size(); }
//
//        static class ViewHolder extends RecyclerView.ViewHolder {
//            TextView tvLabel, tvValue;
//
//            ViewHolder(@NonNull View itemView) {
//                super(itemView);
//                tvLabel = itemView.findViewById(R.id.tvStatLabel);
//                tvValue = itemView.findViewById(R.id.tvStatValue);
//            }
//        }
//    }
//}
////package com.example.libreman;
////
////import android.os.Bundle;
////import androidx.annotation.NonNull;
////import androidx.annotation.Nullable;
////import androidx.fragment.app.Fragment;
////import androidx.recyclerview.widget.GridLayoutManager;
////import androidx.recyclerview.widget.RecyclerView;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////
////public class AdminDashboardFragment extends Fragment {
////
////    public AdminDashboardFragment() {
////        super(R.layout.fragment_admin_dashboard);
////    }
////
////    @Override
////    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
////
////        RecyclerView stats = view.findViewById(R.id.rvStats);
////        stats.setLayoutManager(new GridLayoutManager(getContext(), 2));
//////        stats.setAdapter(new StatsAdapter());
////    }
////}
