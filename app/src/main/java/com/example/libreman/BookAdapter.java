package com.example.libreman;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder>
        implements Filterable {

    public enum SearchType { BOOK, AUTHOR, ISBN }

    private final List<Book> originalList;
    private List<Book> filteredList;
    public SearchType searchType = SearchType.BOOK;

    // 🔹 Callback for result count
    public interface OnResultCountListener {
        void onResultCount(int count);
    }

    private OnResultCountListener resultCountListener;

    public void setOnResultCountListener(OnResultCountListener listener) {
        this.resultCountListener = listener;
    }

    public BookAdapter(List<Book> books) {
        this.originalList = books;
        this.filteredList = new ArrayList<>(books);
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = filteredList.get(position);
        holder.title.setText(book.title);
        holder.author.setText(book.author);
        holder.isbn.setText("ISBN: " + book.isbn);
        holder.status.setText(book.status);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase().trim();
                List<Book> result = new ArrayList<>();

                if (query.isEmpty()) {
                    result.addAll(originalList);
                } else {
                    for (Book book : originalList) {
                        switch (searchType) {
                            case AUTHOR:
                                if (book.author.toLowerCase().contains(query))
                                    result.add(book);
                                break;

                            case ISBN:
                                if (book.isbn.contains(query))
                                    result.add(book);
                                break;

                            case BOOK:
                                if (book.title.toLowerCase().contains(query))
                                    result.add(book);
                                break;
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = result;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<Book>) results.values;
                notifyDataSetChanged();

                if (resultCountListener != null) {
                    resultCountListener.onResultCount(filteredList.size());
                }
            }
        };
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, isbn, status;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvBookTitle);
            author = itemView.findViewById(R.id.tvAuthor);
            isbn = itemView.findViewById(R.id.tvISBN);
            status = itemView.findViewById(R.id.tvStatus);
        }
    }
}
