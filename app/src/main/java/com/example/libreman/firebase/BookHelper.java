package com.example.libreman.firebase;

import androidx.annotation.NonNull;
import com.example.libreman.model.Book;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookHelper {

    private final DatabaseReference booksRef;

    public BookHelper() {
        booksRef = FirebaseDatabase
                .getInstance("https://libreman-f9839-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("books");
    }
    // ======================== CALLBACK INTERFACES ========================

    public interface BookCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    public interface SingleBookCallback {
        void onBookLoaded(Book book);
        void onError(String error);
    }

    public interface BookListCallback {
        void onBooksLoaded(List<Book> books);
        void onError(String error);
    }

    // ======================== STORE ========================

    public void insertBook(Book book, BookCallback callback) {
        String key = booksRef.push().getKey();
        if (key == null) {
            callback.onFailure("Failed to generate book ID.");
            return;
        }
        book.setBookId(key);
        booksRef.child(key).setValue(book)
                .addOnSuccessListener(unused -> callback.onSuccess("Book added with ID: " + key))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void updateBook(Book book, BookCallback callback) {
        if (book.getBookId() == null) {
            callback.onFailure("Book ID is null.");
            return;
        }
        booksRef.child(book.getBookId()).setValue(book)
                .addOnSuccessListener(unused -> callback.onSuccess("Book updated."))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void deleteBook(String bookId, BookCallback callback) {
        booksRef.child(bookId).removeValue()
                .addOnSuccessListener(unused -> callback.onSuccess("Book deleted."))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // ======================== RETRIEVE ========================

    public void getBookById(String bookId, SingleBookCallback callback) {
        booksRef.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Book book = snapshot.getValue(Book.class);
                if (book != null) {
                    callback.onBookLoaded(book);
                } else {
                    callback.onError("Book not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void getAllBooks(BookListCallback callback) {
        booksRef.orderByChild("title").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Book> books = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Book book = child.getValue(Book.class);
                    if (book != null) books.add(book);
                }
                callback.onBooksLoaded(books);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void searchBooksByTitle(String query, BookListCallback callback) {
        booksRef.orderByChild("title")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Book> books = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Book book = child.getValue(Book.class);
                            if (book != null) books.add(book);
                        }
                        callback.onBooksLoaded(books);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    public void getBooksByGenre(String genre, BookListCallback callback) {
        booksRef.orderByChild("genre").equalTo(genre)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Book> books = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Book book = child.getValue(Book.class);
                            if (book != null) books.add(book);
                        }
                        callback.onBooksLoaded(books);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    public void getAvailableBooks(BookListCallback callback) {
        booksRef.orderByChild("availableCopies").startAt(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Book> books = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Book book = child.getValue(Book.class);
                            if (book != null) books.add(book);
                        }
                        callback.onBooksLoaded(books);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }
}
