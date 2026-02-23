package com.example.libreman.model;

import com.google.firebase.database.Exclude;

public class Book {

    private String bookId;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private int totalCopies;
    private int availableCopies;
    private long addedDate;

    // Required empty constructor for Firebase
    public Book() {}

    public Book(String title, String author, String isbn, String genre, int totalCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.addedDate = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    public long getAddedDate() { return addedDate; }
    public void setAddedDate(long addedDate) { this.addedDate = addedDate; }

    /**
     * Returns the availability status of the book.
     * Excluded from Firebase so it is NOT stored in the database —
     * it is calculated on the fly from availableCopies.
     */
    @Exclude
    public String getStatus() {
        if (availableCopies <= 0) {
            return "Unavailable";
        } else if (availableCopies == totalCopies) {
            return "Available";
        } else {
            return "Available (" + availableCopies + " of " + totalCopies + " copies)";
        }
    }


}

//package com.example.libreman.model;
//
//import com.google.firebase.database.Exclude;
//
//public class Book {
//
//    private String bookId;
//    private String title;
//    private String author;
//    private String isbn;
//    private String genre;
//    private int totalCopies;
//    private int availableCopies;
//    private long addedDate;
//
//    // Required empty constructor for Firebase
//    public Book() {}
//
//    public Book(String title, String author, String isbn, String genre, int totalCopies) {
//        this.title = title;
//        this.author = author;
//        this.isbn = isbn;
//        this.genre = genre;
//        this.totalCopies = totalCopies;
//        this.availableCopies = totalCopies;
//        this.addedDate = System.currentTimeMillis();
//    }
//
//    // Getters and Setters
//    public String getBookId() { return bookId; }
//    public void setBookId(String bookId) { this.bookId = bookId; }
//
//    public String getTitle() { return title; }
//    public void setTitle(String title) { this.title = title; }
//
//    public String getAuthor() { return author; }
//    public void setAuthor(String author) { this.author = author; }
//
//    public String getIsbn() { return isbn; }
//    public void setIsbn(String isbn) { this.isbn = isbn; }
//
//    public String getGenre() { return genre; }
//    public void setGenre(String genre) { this.genre = genre; }
//
//    public int getTotalCopies() { return totalCopies; }
//    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }
//
//    public int getAvailableCopies() { return availableCopies; }
//    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }
//
//    public long getAddedDate() { return addedDate; }
//    public void setAddedDate(long addedDate) { this.addedDate = addedDate; }
//
//
//}