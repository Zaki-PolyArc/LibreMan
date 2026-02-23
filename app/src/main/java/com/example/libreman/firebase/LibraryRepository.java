package com.example.libreman.firebase;

import com.example.libreman.model.Book;
import com.example.libreman.model.BorrowRecord;
import com.example.libreman.model.Member;

import java.util.List;

/**
 * LibraryRepository — single entry point for ALL Firebase database operations.
 * Wraps BookHelper, MemberHelper, and BorrowRecordHelper.
 * Handles borrow/return logic with automatic copy count management.
 */
public class LibraryRepository {

    private final BookHelper bookHelper;
    private final MemberHelper memberHelper;
    private final BorrowRecordHelper borrowRecordHelper;

    public LibraryRepository() {
        bookHelper = new BookHelper();
        memberHelper = new MemberHelper();
        borrowRecordHelper = new BorrowRecordHelper();
    }

    // ======================== GENERIC CALLBACK ========================

    public interface ResultCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    // ======================== BOOK OPERATIONS ========================

    public void insertBook(Book book, ResultCallback callback) {
        bookHelper.insertBook(book, new BookHelper.BookCallback() {
            @Override
            public void onSuccess(String message) { callback.onSuccess(message); }
            @Override
            public void onFailure(String error) { callback.onFailure(error); }
        });
    }

    public void updateBook(Book book, ResultCallback callback) {
        bookHelper.updateBook(book, new BookHelper.BookCallback() {
            @Override
            public void onSuccess(String message) { callback.onSuccess(message); }
            @Override
            public void onFailure(String error) { callback.onFailure(error); }
        });
    }

    public void deleteBook(String bookId, ResultCallback callback) {
        bookHelper.deleteBook(bookId, new BookHelper.BookCallback() {
            @Override
            public void onSuccess(String message) { callback.onSuccess(message); }
            @Override
            public void onFailure(String error) { callback.onFailure(error); }
        });
    }

    public void getAllBooks(BookHelper.BookListCallback callback) {
        bookHelper.getAllBooks(callback);
    }

    public void getBookById(String bookId, BookHelper.SingleBookCallback callback) {
        bookHelper.getBookById(bookId, callback);
    }

    public void searchBooksByTitle(String query, BookHelper.BookListCallback callback) {
        bookHelper.searchBooksByTitle(query, callback);
    }

    public void getBooksByGenre(String genre, BookHelper.BookListCallback callback) {
        bookHelper.getBooksByGenre(genre, callback);
    }

    public void getAvailableBooks(BookHelper.BookListCallback callback) {
        bookHelper.getAvailableBooks(callback);
    }

    // ======================== MEMBER OPERATIONS ========================

    public void insertMember(Member member, ResultCallback callback) {
        memberHelper.insertMember(member, new MemberHelper.MemberCallback() {
            @Override
            public void onSuccess(String message) { callback.onSuccess(message); }
            @Override
            public void onFailure(String error) { callback.onFailure(error); }
        });
    }

    public void updateMember(Member member, ResultCallback callback) {
        memberHelper.updateMember(member, new MemberHelper.MemberCallback() {
            @Override
            public void onSuccess(String message) { callback.onSuccess(message); }
            @Override
            public void onFailure(String error) { callback.onFailure(error); }
        });
    }

    public void deleteMember(String memberId, ResultCallback callback) {
        memberHelper.deleteMember(memberId, new MemberHelper.MemberCallback() {
            @Override
            public void onSuccess(String message) { callback.onSuccess(message); }
            @Override
            public void onFailure(String error) { callback.onFailure(error); }
        });
    }

    public void getAllMembers(MemberHelper.MemberListCallback callback) {
        memberHelper.getAllMembers(callback);
    }

    public void getMemberById(String memberId, MemberHelper.SingleMemberCallback callback) {
        memberHelper.getMemberById(memberId, callback);
    }

    public void searchMembersByName(String query, MemberHelper.MemberListCallback callback) {
        memberHelper.searchMembersByName(query, callback);
    }

    // ======================== BORROW & RETURN ========================

    /**
     * Borrow a book:
     * 1. Checks if book exists and has available copies
     * 2. Creates a BorrowRecord
     * 3. Decrements availableCopies on the book
     */
    public void borrowBook(String bookId, String memberId, long dueDate, ResultCallback callback) {
        bookHelper.getBookById(bookId, new BookHelper.SingleBookCallback() {
            @Override
            public void onBookLoaded(Book book) {
                if (book.getAvailableCopies() <= 0) {
                    callback.onFailure("No copies available for this book.");
                    return;
                }

                // Create borrow record
                BorrowRecord record = new BorrowRecord(bookId, memberId,
                        System.currentTimeMillis(), dueDate);

                borrowRecordHelper.insertRecord(record, new BorrowRecordHelper.RecordCallback() {
                    @Override
                    public void onSuccess(String message) {
                        // Decrease available copies
                        book.setAvailableCopies(book.getAvailableCopies() - 1);
                        bookHelper.updateBook(book, new BookHelper.BookCallback() {
                            @Override
                            public void onSuccess(String msg) {
                                callback.onSuccess("Book borrowed successfully.");
                            }
                            @Override
                            public void onFailure(String error) {
                                callback.onFailure("Record created but failed to update copies: " + error);
                            }
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        callback.onFailure("Failed to create borrow record: " + error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                callback.onFailure("Book not found: " + error);
            }
        });
    }

    /**
     * Return a book:
     * 1. Marks the BorrowRecord as returned
     * 2. Increments availableCopies on the book
     */
    public void returnBook(String recordId, ResultCallback callback) {
        borrowRecordHelper.getRecordById(recordId, new BorrowRecordHelper.SingleRecordCallback() {
            @Override
            public void onRecordLoaded(BorrowRecord record) {
                if (record.isReturned()) {
                    callback.onFailure("This book has already been returned.");
                    return;
                }

                // Mark as returned
                record.setReturned(true);
                record.setReturnDate(System.currentTimeMillis());

                borrowRecordHelper.updateRecord(record, new BorrowRecordHelper.RecordCallback() {
                    @Override
                    public void onSuccess(String message) {
                        // Increase available copies
                        bookHelper.getBookById(record.getBookId(), new BookHelper.SingleBookCallback() {
                            @Override
                            public void onBookLoaded(Book book) {
                                book.setAvailableCopies(book.getAvailableCopies() + 1);
                                bookHelper.updateBook(book, new BookHelper.BookCallback() {
                                    @Override
                                    public void onSuccess(String msg) {
                                        callback.onSuccess("Book returned successfully.");
                                    }
                                    @Override
                                    public void onFailure(String error) {
                                        callback.onFailure("Record updated but failed to update copies: " + error);
                                    }
                                });
                            }

                            @Override
                            public void onError(String error) {
                                callback.onFailure("Record updated but book not found: " + error);
                            }
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        callback.onFailure("Failed to update record: " + error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                callback.onFailure("Record not found: " + error);
            }
        });
    }

    // ======================== BORROW RECORD QUERIES ========================

    public void getAllActiveRecords(BorrowRecordHelper.RecordListCallback callback) {
        borrowRecordHelper.getAllActiveRecords(callback);
    }

    public void getActiveRecordsByMember(String memberId, BorrowRecordHelper.RecordListCallback callback) {
        borrowRecordHelper.getActiveRecordsByMember(memberId, callback);
    }

    public void getOverdueRecords(BorrowRecordHelper.RecordListCallback callback) {
        borrowRecordHelper.getOverdueRecords(callback);
    }

    public void getAllRecords(BorrowRecordHelper.RecordListCallback callback) {
        borrowRecordHelper.getAllRecords(callback);
    }
}
