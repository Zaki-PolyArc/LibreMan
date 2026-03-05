package com.example.libreman.firebase;

import com.example.libreman.model.Book;
import com.example.libreman.model.BorrowRecord;
import com.example.libreman.model.Member;
import com.example.libreman.util.FineCalculator;

import java.util.List;

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
            @Override public void onSuccess(String message) { callback.onSuccess(message); }
            @Override public void onFailure(String error) { callback.onFailure(error); }
        });
    }

    public void updateBook(Book book, ResultCallback callback) {
        bookHelper.updateBook(book, new BookHelper.BookCallback() {
            @Override public void onSuccess(String message) { callback.onSuccess(message); }
            @Override public void onFailure(String error) { callback.onFailure(error); }
        });
    }

    public void deleteBook(String bookId, ResultCallback callback) {
        bookHelper.deleteBook(bookId, new BookHelper.BookCallback() {
            @Override public void onSuccess(String message) { callback.onSuccess(message); }
            @Override public void onFailure(String error) { callback.onFailure(error); }
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
            @Override public void onSuccess(String message) { callback.onSuccess(message); }
            @Override public void onFailure(String error) { callback.onFailure(error); }
        });
    }

    public void updateMember(Member member, ResultCallback callback) {
        memberHelper.updateMember(member, new MemberHelper.MemberCallback() {
            @Override public void onSuccess(String message) { callback.onSuccess(message); }
            @Override public void onFailure(String error) { callback.onFailure(error); }
        });
    }

    public void deleteMember(String memberId, ResultCallback callback) {
        memberHelper.deleteMember(memberId, new MemberHelper.MemberCallback() {
            @Override public void onSuccess(String message) { callback.onSuccess(message); }
            @Override public void onFailure(String error) { callback.onFailure(error); }
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

    // ======================== STUDENT SIGNUP & LOGIN ========================

    /**
     * Signup: Save student as a Member in Firebase using studentId as key
     */
    public void signupStudent(String studentId, String fullName, String email,
                              String password, ResultCallback callback) {
        Member member = new Member(fullName, email, "");
        member.setMemberId(studentId);

        // Store under members/{studentId}
        memberHelper.updateMember(member, new MemberHelper.MemberCallback() {
            @Override
            public void onSuccess(String message) {
                // Also store credentials under students_auth/{studentId}
                com.google.firebase.database.FirebaseDatabase
                        .getInstance("https://libreman-f9839-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("students_auth")
                        .child(studentId)
                        .child("password")
                        .setValue(password)
                        .addOnSuccessListener(unused -> callback.onSuccess("Account created!"))
                        .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
            }
            @Override
            public void onFailure(String error) { callback.onFailure(error); }
        });
    }

    /**
     * Login: Check studentId exists and password matches
     */
    public void loginStudent(String studentId, String email, String password,
                             ResultCallback callback) {
        // First check if member exists
        memberHelper.getMemberById(studentId, new MemberHelper.SingleMemberCallback() {
            @Override
            public void onMemberLoaded(Member member) {
                // Check email matches
                if (!member.getEmail().equalsIgnoreCase(email)) {
                    callback.onFailure("Email does not match this Student ID.");
                    return;
                }
                // Check password
                com.google.firebase.database.FirebaseDatabase
                        .getInstance("https://libreman-f9839-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("students_auth")
                        .child(studentId)
                        .child("password")
                        .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                            @Override
                            public void onDataChange(@androidx.annotation.NonNull com.google.firebase.database.DataSnapshot snapshot) {
                                String storedPassword = snapshot.getValue(String.class);
                                if (storedPassword != null && storedPassword.equals(password)) {
                                    callback.onSuccess(member.getFullName());
                                } else {
                                    callback.onFailure("Incorrect password.");
                                }
                            }
                            @Override
                            public void onCancelled(@androidx.annotation.NonNull com.google.firebase.database.DatabaseError error) {
                                callback.onFailure(error.getMessage());
                            }
                        });
            }
            @Override
            public void onError(String error) {
                callback.onFailure("Student ID not found. Please sign up first.");
            }
        });
    }

    // ======================== BORROW BOOK (UPDATED) ========================

    /**
     * Borrow a book:
     * 1. Check available copies > 0
     * 2. Check student hasn't hit max borrow limit (5)
     * 3. Create BorrowRecord with bookTitle + memberName
     * 4. Decrease availableCopies
     */
    public void borrowBook(String bookId, String memberId, String memberName,
                           ResultCallback callback) {

        // Step 1: Check borrow limit
        borrowRecordHelper.getActiveRecordsByMember(memberId,
                new BorrowRecordHelper.RecordListCallback() {
                    @Override
                    public void onRecordsLoaded(List<BorrowRecord> records) {
                        if (records.size() >= FineCalculator.MAX_BORROW_LIMIT) {
                            callback.onFailure("Borrow limit reached! Max " +
                                    FineCalculator.MAX_BORROW_LIMIT + " books.");
                            return;
                        }

                        // Check if student already borrowed this book
                        for (BorrowRecord r : records) {
                            if (r.getBookId().equals(bookId)) {
                                callback.onFailure("You already borrowed this book!");
                                return;
                            }
                        }

                        // Step 2: Check book availability
                        bookHelper.getBookById(bookId, new BookHelper.SingleBookCallback() {
                            @Override
                            public void onBookLoaded(Book book) {
                                if (book.getAvailableCopies() <= 0) {
                                    callback.onFailure("No copies available.");
                                    return;
                                }

                                // Step 3: Create borrow record
                                long now = System.currentTimeMillis();
                                long dueDate = FineCalculator.calculateDueDate(now);

                                BorrowRecord record = new BorrowRecord(
                                        bookId, book.getTitle(),
                                        memberId, memberName,
                                        now, dueDate
                                );

                                borrowRecordHelper.insertRecord(record,
                                        new BorrowRecordHelper.RecordCallback() {
                                            @Override
                                            public void onSuccess(String message) {
                                                // Step 4: Decrease copies
                                                book.setAvailableCopies(
                                                        book.getAvailableCopies() - 1);
                                                bookHelper.updateBook(book,
                                                        new BookHelper.BookCallback() {
                                                            @Override
                                                            public void onSuccess(String msg) {
                                                                callback.onSuccess(
                                                                        "Book borrowed! Due: " +
                                                                                FineCalculator.formatDate(dueDate));
                                                            }
                                                            @Override
                                                            public void onFailure(String error) {
                                                                callback.onFailure(
                                                                        "Borrowed but failed to update copies: "
                                                                                + error);
                                                            }
                                                        });
                                            }
                                            @Override
                                            public void onFailure(String error) {
                                                callback.onFailure(
                                                        "Failed to create record: " + error);
                                            }
                                        });
                            }

                            @Override
                            public void onError(String error) {
                                callback.onFailure("Book not found: " + error);
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        callback.onFailure("Failed to check borrow limit: " + error);
                    }
                });
    }

    // ======================== RETURN BOOK (UPDATED WITH FINE) ========================

    /**
     * Return a book:
     * 1. Calculate fine if overdue
     * 2. Mark record as returned + save fineAmount
     * 3. Increase availableCopies
     */
    public void returnBook(String recordId, ResultCallback callback) {
        borrowRecordHelper.getRecordById(recordId,
                new BorrowRecordHelper.SingleRecordCallback() {
                    @Override
                    public void onRecordLoaded(BorrowRecord record) {
                        if (record.isReturned()) {
                            callback.onFailure("Already returned.");
                            return;
                        }

                        // Calculate fine
                        double fine = FineCalculator.calculateFine(record.getDueDate());
                        record.setReturned(true);
                        record.setReturnDate(System.currentTimeMillis());
                        record.setFineAmount(fine);

                        borrowRecordHelper.updateRecord(record,
                                new BorrowRecordHelper.RecordCallback() {
                                    @Override
                                    public void onSuccess(String message) {
                                        // Increase copies back
                                        bookHelper.getBookById(record.getBookId(),
                                                new BookHelper.SingleBookCallback() {
                                                    @Override
                                                    public void onBookLoaded(Book book) {
                                                        book.setAvailableCopies(
                                                                book.getAvailableCopies() + 1);
                                                        bookHelper.updateBook(book,
                                                                new BookHelper.BookCallback() {
                                                                    @Override
                                                                    public void onSuccess(String msg) {
                                                                        String result = "Book returned!";
                                                                        if (fine > 0) {
                                                                            result += " Fine: Rs." + (int) fine;
                                                                        }
                                                                        callback.onSuccess(result);
                                                                    }
                                                                    @Override
                                                                    public void onFailure(String error) {
                                                                        callback.onFailure(
                                                                                "Returned but copies update failed: "
                                                                                        + error);
                                                                    }
                                                                });
                                                    }
                                                    @Override
                                                    public void onError(String error) {
                                                        callback.onFailure(
                                                                "Returned but book not found: " + error);
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

    public void getActiveRecordsByMember(String memberId,
                                         BorrowRecordHelper.RecordListCallback callback) {
        borrowRecordHelper.getActiveRecordsByMember(memberId, callback);
    }

    public void getOverdueRecords(BorrowRecordHelper.RecordListCallback callback) {
        borrowRecordHelper.getOverdueRecords(callback);
    }

    public void getAllRecords(BorrowRecordHelper.RecordListCallback callback) {
        borrowRecordHelper.getAllRecords(callback);
    }
}
//package com.example.libreman.firebase;
//
//import com.example.libreman.model.Book;
//import com.example.libreman.model.BorrowRecord;
//import com.example.libreman.model.Member;
//
//import java.util.List;
//
///**
// * LibraryRepository — single entry point for ALL Firebase database operations.
// * Wraps BookHelper, MemberHelper, and BorrowRecordHelper.
// * Handles borrow/return logic with automatic copy count management.
// */
//public class LibraryRepository {
//
//    private final BookHelper bookHelper;
//    private final MemberHelper memberHelper;
//    private final BorrowRecordHelper borrowRecordHelper;
//
//    public LibraryRepository() {
//        bookHelper = new BookHelper();
//        memberHelper = new MemberHelper();
//        borrowRecordHelper = new BorrowRecordHelper();
//    }
//
//    // ======================== GENERIC CALLBACK ========================
//
//    public interface ResultCallback {
//        void onSuccess(String message);
//        void onFailure(String error);
//    }
//
//    // ======================== BOOK OPERATIONS ========================
//
//    public void insertBook(Book book, ResultCallback callback) {
//        bookHelper.insertBook(book, new BookHelper.BookCallback() {
//            @Override
//            public void onSuccess(String message) { callback.onSuccess(message); }
//            @Override
//            public void onFailure(String error) { callback.onFailure(error); }
//        });
//    }
//
//    public void updateBook(Book book, ResultCallback callback) {
//        bookHelper.updateBook(book, new BookHelper.BookCallback() {
//            @Override
//            public void onSuccess(String message) { callback.onSuccess(message); }
//            @Override
//            public void onFailure(String error) { callback.onFailure(error); }
//        });
//    }
//
//    public void deleteBook(String bookId, ResultCallback callback) {
//        bookHelper.deleteBook(bookId, new BookHelper.BookCallback() {
//            @Override
//            public void onSuccess(String message) { callback.onSuccess(message); }
//            @Override
//            public void onFailure(String error) { callback.onFailure(error); }
//        });
//    }
//
//    public void getAllBooks(BookHelper.BookListCallback callback) {
//        bookHelper.getAllBooks(callback);
//    }
//
//    public void getBookById(String bookId, BookHelper.SingleBookCallback callback) {
//        bookHelper.getBookById(bookId, callback);
//    }
//
//    public void searchBooksByTitle(String query, BookHelper.BookListCallback callback) {
//        bookHelper.searchBooksByTitle(query, callback);
//    }
//
//    public void getBooksByGenre(String genre, BookHelper.BookListCallback callback) {
//        bookHelper.getBooksByGenre(genre, callback);
//    }
//
//    public void getAvailableBooks(BookHelper.BookListCallback callback) {
//        bookHelper.getAvailableBooks(callback);
//    }
//
//    // ======================== MEMBER OPERATIONS ========================
//
//    public void insertMember(Member member, ResultCallback callback) {
//        memberHelper.insertMember(member, new MemberHelper.MemberCallback() {
//            @Override
//            public void onSuccess(String message) { callback.onSuccess(message); }
//            @Override
//            public void onFailure(String error) { callback.onFailure(error); }
//        });
//    }
//
//    public void updateMember(Member member, ResultCallback callback) {
//        memberHelper.updateMember(member, new MemberHelper.MemberCallback() {
//            @Override
//            public void onSuccess(String message) { callback.onSuccess(message); }
//            @Override
//            public void onFailure(String error) { callback.onFailure(error); }
//        });
//    }
//
//    public void deleteMember(String memberId, ResultCallback callback) {
//        memberHelper.deleteMember(memberId, new MemberHelper.MemberCallback() {
//            @Override
//            public void onSuccess(String message) { callback.onSuccess(message); }
//            @Override
//            public void onFailure(String error) { callback.onFailure(error); }
//        });
//    }
//
//    public void getAllMembers(MemberHelper.MemberListCallback callback) {
//        memberHelper.getAllMembers(callback);
//    }
//
//    public void getMemberById(String memberId, MemberHelper.SingleMemberCallback callback) {
//        memberHelper.getMemberById(memberId, callback);
//    }
//
//    public void searchMembersByName(String query, MemberHelper.MemberListCallback callback) {
//        memberHelper.searchMembersByName(query, callback);
//    }
//
//    // ======================== BORROW & RETURN ========================
//
//    /**
//     * Borrow a book:
//     * 1. Checks if book exists and has available copies
//     * 2. Creates a BorrowRecord
//     * 3. Decrements availableCopies on the book
//     */
//    public void borrowBook(String bookId, String memberId, long dueDate, ResultCallback callback) {
//        bookHelper.getBookById(bookId, new BookHelper.SingleBookCallback() {
//            @Override
//            public void onBookLoaded(Book book) {
//                if (book.getAvailableCopies() <= 0) {
//                    callback.onFailure("No copies available for this book.");
//                    return;
//                }
//
//                // Create borrow record
//                BorrowRecord record = new BorrowRecord(bookId, memberId,
//                        System.currentTimeMillis(), dueDate);
//
//                borrowRecordHelper.insertRecord(record, new BorrowRecordHelper.RecordCallback() {
//                    @Override
//                    public void onSuccess(String message) {
//                        // Decrease available copies
//                        book.setAvailableCopies(book.getAvailableCopies() - 1);
//                        bookHelper.updateBook(book, new BookHelper.BookCallback() {
//                            @Override
//                            public void onSuccess(String msg) {
//                                callback.onSuccess("Book borrowed successfully.");
//                            }
//                            @Override
//                            public void onFailure(String error) {
//                                callback.onFailure("Record created but failed to update copies: " + error);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(String error) {
//                        callback.onFailure("Failed to create borrow record: " + error);
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String error) {
//                callback.onFailure("Book not found: " + error);
//            }
//        });
//    }
//
//    /**
//     * Return a book:
//     * 1. Marks the BorrowRecord as returned
//     * 2. Increments availableCopies on the book
//     */
//    public void returnBook(String recordId, ResultCallback callback) {
//        borrowRecordHelper.getRecordById(recordId, new BorrowRecordHelper.SingleRecordCallback() {
//            @Override
//            public void onRecordLoaded(BorrowRecord record) {
//                if (record.isReturned()) {
//                    callback.onFailure("This book has already been returned.");
//                    return;
//                }
//
//                // Mark as returned
//                record.setReturned(true);
//                record.setReturnDate(System.currentTimeMillis());
//
//                borrowRecordHelper.updateRecord(record, new BorrowRecordHelper.RecordCallback() {
//                    @Override
//                    public void onSuccess(String message) {
//                        // Increase available copies
//                        bookHelper.getBookById(record.getBookId(), new BookHelper.SingleBookCallback() {
//                            @Override
//                            public void onBookLoaded(Book book) {
//                                book.setAvailableCopies(book.getAvailableCopies() + 1);
//                                bookHelper.updateBook(book, new BookHelper.BookCallback() {
//                                    @Override
//                                    public void onSuccess(String msg) {
//                                        callback.onSuccess("Book returned successfully.");
//                                    }
//                                    @Override
//                                    public void onFailure(String error) {
//                                        callback.onFailure("Record updated but failed to update copies: " + error);
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onError(String error) {
//                                callback.onFailure("Record updated but book not found: " + error);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(String error) {
//                        callback.onFailure("Failed to update record: " + error);
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String error) {
//                callback.onFailure("Record not found: " + error);
//            }
//        });
//    }
//
//    // ======================== BORROW RECORD QUERIES ========================
//
//    public void getAllActiveRecords(BorrowRecordHelper.RecordListCallback callback) {
//        borrowRecordHelper.getAllActiveRecords(callback);
//    }
//
//    public void getActiveRecordsByMember(String memberId, BorrowRecordHelper.RecordListCallback callback) {
//        borrowRecordHelper.getActiveRecordsByMember(memberId, callback);
//    }
//
//    public void getOverdueRecords(BorrowRecordHelper.RecordListCallback callback) {
//        borrowRecordHelper.getOverdueRecords(callback);
//    }
//
//    public void getAllRecords(BorrowRecordHelper.RecordListCallback callback) {
//        borrowRecordHelper.getAllRecords(callback);
//    }
//}
