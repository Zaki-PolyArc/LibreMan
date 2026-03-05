package com.example.libreman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libreman.model.Book;

import java.util.ArrayList;
import java.util.List;

public class CatalogBookAdapter extends RecyclerView.Adapter<CatalogBookAdapter.BookViewHolder> {

    private final Context context;

    private final List<Book> fullList = new ArrayList<>();
    private final List<Book> displayList = new ArrayList<>();

    public CatalogBookAdapter(Context context) {
        this.context = context;


        displayList.addAll(fullList);
    }

    public void filter(String type) {

        displayList.clear();

        if (type.equals("ALL")) {
            displayList.addAll(fullList);
        } else {
            for (Book book : fullList) {
                if (book.getStatus().equalsIgnoreCase(type)) {
                    displayList.add(book);
                }
            }
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_card, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        Book book = displayList.get(position);

        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.isbn.setText("ISBN: " + book.getIsbn());

        holder.itemView.setOnClickListener(v ->
                Toast.makeText(context,
                        "Status: " + book.getStatus(),
                        Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return displayList.size();
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
