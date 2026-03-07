package com.example.libreman;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libreman.model.BorrowRecord;
import com.example.libreman.util.FineCalculator;

import java.util.List;

public class ActiveBorrowsAdapter extends RecyclerView.Adapter<ActiveBorrowsAdapter.ViewHolder> {

    private final List<BorrowRecord> records;

    public ActiveBorrowsAdapter(List<BorrowRecord> records) {
        this.records = records;
    }

    public void updateList(List<BorrowRecord> newRecords) {
        this.records.clear();
        this.records.addAll(newRecords);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_active_borrow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BorrowRecord record = records.get(position);

        holder.tvStudent.setText(record.getMemberName());
        holder.tvBook.setText(record.getBookTitle());
        holder.tvBorrowDate.setText("Borrowed: " + FineCalculator.formatDate(record.getBorrowDate()));
        holder.tvDue.setText("Due: " + FineCalculator.formatDate(record.getDueDate()));

        if (FineCalculator.isOverdue(record.getDueDate())) {
            holder.tvStatus.setText("Overdue");
            holder.tvStatus.setTextColor(Color.parseColor("#EF4444"));
            holder.tvDue.setTextColor(Color.parseColor("#EF4444"));
        } else if (FineCalculator.isDueSoon(record.getDueDate())) {
            holder.tvStatus.setText("Due Soon");
            holder.tvStatus.setTextColor(Color.parseColor("#F59E0B"));
        } else {
            holder.tvStatus.setText("Active");
            holder.tvStatus.setTextColor(Color.parseColor("#10B981"));
        }
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudent, tvBook, tvBorrowDate, tvDue, tvStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudent = itemView.findViewById(R.id.tvBorrowStudent);
            tvBook = itemView.findViewById(R.id.tvBorrowBook);
            tvBorrowDate = itemView.findViewById(R.id.tvBorrowDate);
            tvDue = itemView.findViewById(R.id.tvBorrowDue);
            tvStatus = itemView.findViewById(R.id.tvBorrowStatus);
        }
    }
}