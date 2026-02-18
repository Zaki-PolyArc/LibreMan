package com.example.libreman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CatalogBookAdapter extends RecyclerView.Adapter<CatalogBookAdapter.BookViewHolder> {

    private final Context context;

    private final List<Book> fullList = new ArrayList<>();
    private final List<Book> displayList = new ArrayList<>();

    public CatalogBookAdapter(Context context) {
        this.context = context;

        // Dummy Data
        fullList.add(new Book("Clean Code", "Robert C. Martin", "9780132350884", "AVAILABLE"));
        fullList.add(new Book("Effective Java", "Joshua Bloch", "9780134685991", "AVAILABLE"));
        fullList.add(new Book("Design Patterns", "Erich Gamma", "9780201633610", "CHECKED_OUT"));
        fullList.add(new Book("Refactoring", "Martin Fowler", "9780201485677", "CHECKED_OUT"));
        fullList.add(new Book("Android Dev", "Google", "9781111111111", "AVAILABLE"));
        fullList.add(new Book("Java Basics", "James Gosling", "9782222222222", "AVAILABLE"));

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
