package com.example.libreman;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CatalogBookAdapter extends RecyclerView.Adapter<CatalogBookAdapter.BookViewHolder> {

    private final int ITEM_COUNT = 6; // test data

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_card, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        holder.title.setText("Clean Code");
        holder.author.setText("Robert C. Martin");
        holder.isbn.setText("ISBN: 978-0132350884");
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {

        TextView title, author, isbn;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvBookTitle);
            author = itemView.findViewById(R.id.tvAuthor);
            isbn = itemView.findViewById(R.id.tvISBN);
        }
    }
}
