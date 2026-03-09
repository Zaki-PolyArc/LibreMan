package com.example.libreman;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libreman.model.BorrowRecord;
import com.example.libreman.util.FineCalculator;

import java.util.List;

public class BorrowedBooksAdapter extends RecyclerView.Adapter<BorrowedBooksAdapter.ViewHolder> {

    private final List<BorrowRecord> records;

    public interface OnReturnClickListener {
        void onReturnClick(BorrowRecord record);
    }

    private OnReturnClickListener returnClickListener;

    public void setOnReturnClickListener(OnReturnClickListener listener) {
        this.returnClickListener = listener;
    }

    public BorrowedBooksAdapter(List<BorrowRecord> records) {
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
                .inflate(R.layout.item_borrowed_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BorrowRecord record = records.get(position);

        holder.tvTitle.setText(record.getBookTitle());
        holder.tvBorrowDate.setText("Borrowed: " + FineCalculator.formatDate(record.getBorrowDate()));
        holder.tvDueDate.setText("Due: " + FineCalculator.formatDate(record.getDueDate()));

        // Fine status
        String status = FineCalculator.getFineStatus(record);
        holder.tvFineStatus.setText(status);

        if (FineCalculator.isOverdue(record.getDueDate())) {
            holder.tvFineStatus.setTextColor(Color.parseColor("#EF4444")); // Red
            holder.tvDueDate.setTextColor(Color.parseColor("#EF4444"));
        } else if (FineCalculator.isDueSoon(record.getDueDate())) {
            holder.tvFineStatus.setTextColor(Color.parseColor("#F59E0B")); // Yellow/Orange
        } else {
            holder.tvFineStatus.setTextColor(Color.parseColor("#10B981")); // Green
        }

        // Return button
        holder.btnReturn.setOnClickListener(v -> {
            if (returnClickListener != null) {
                returnClickListener.onReturnClick(record);
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvBorrowDate, tvDueDate, tvFineStatus;
        Button btnReturn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBorrowedTitle);
            tvBorrowDate = itemView.findViewById(R.id.tvBorrowDate);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            tvFineStatus = itemView.findViewById(R.id.tvFineStatus);
            btnReturn = itemView.findViewById(R.id.btnReturn);
        }
    }
}