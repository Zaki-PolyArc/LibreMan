package com.example.libreman;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CurrentReadsAdapter
        extends RecyclerView.Adapter<CurrentReadsAdapter.ViewHolder> {

    private final List<Book> books;

    public CurrentReadsAdapter(List<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_current_read, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);
        holder.title.setText(book.getTitle());
        holder.due.setText("Due soon");
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, due;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvBookTitle);
            due = itemView.findViewById(R.id.tvDue);
        }
    }
}
