package com.example.libreman;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final int ITEM_COUNT = 5; // static test count

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        holder.title.setText("Clean Code");
        holder.author.setText("Robert C. Martin");
        holder.isbn.setText("ISBN: 978-0132350884");
        holder.status.setText("AVAILABLE");
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {

        TextView title, author, isbn, status;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            author = itemView.findViewById(R.id.txtAuthor);
            isbn = itemView.findViewById(R.id.txtIsbn);
            status = itemView.findViewById(R.id.txtStatus);
        }
    }
}
