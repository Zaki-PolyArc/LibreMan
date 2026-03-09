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

public class OverdueAdapter extends RecyclerView.Adapter<OverdueAdapter.ViewHolder> {

    private final List<BorrowRecord> records;

    public OverdueAdapter(List<BorrowRecord> records) {
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
                .inflate(R.layout.item_overdue, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BorrowRecord record = records.get(position);

        holder.tvStudentName.setText(record.getMemberName());
        holder.tvBookTitle.setText(record.getBookTitle());

        long overdueDays = FineCalculator.getOverdueDays(record.getDueDate());
        double fine = FineCalculator.calculateFine(record.getDueDate());

        holder.tvOverdueDays.setText(overdueDays + " days overdue");
        holder.tvOverdueDays.setTextColor(Color.parseColor("#EF4444"));

        holder.tvFine.setText("Fine: Rs." + (int) fine);
        holder.tvFine.setTextColor(Color.parseColor("#EF4444"));

        holder.tvDueDate.setText("Due: " + FineCalculator.formatDate(record.getDueDate()));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName, tvBookTitle, tvOverdueDays, tvFine, tvDueDate;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvOverdueStudent);
            tvBookTitle = itemView.findViewById(R.id.tvOverdueBook);
            tvOverdueDays = itemView.findViewById(R.id.tvOverdueDays);
            tvFine = itemView.findViewById(R.id.tvOverdueFine);
            tvDueDate = itemView.findViewById(R.id.tvOverdueDueDate);
        }
    }
}