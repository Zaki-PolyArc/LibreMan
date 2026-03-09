package com.example.libreman.model;

public class BorrowRecord {

    private String recordId;
    private String bookId;
    private String bookTitle;       // NEW — for easy display
    private String memberId;
    private String memberName;      // NEW — for admin dashboard
    private long borrowDate;
    private long dueDate;
    private long returnDate;
    private boolean returned;
    private double finePerDay;      // NEW — Rs.100
    private double fineAmount;      // NEW — calculated on return

    public BorrowRecord() {}

    public BorrowRecord(String bookId, String bookTitle, String memberId, String memberName,
                        long borrowDate, long dueDate) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.memberId = memberId;
        this.memberName = memberName;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = 0;
        this.returned = false;
        this.finePerDay = 100;
        this.fineAmount = 0;
    }

    // Getters and Setters
    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public long getBorrowDate() { return borrowDate; }
    public void setBorrowDate(long borrowDate) { this.borrowDate = borrowDate; }

    public long getDueDate() { return dueDate; }
    public void setDueDate(long dueDate) { this.dueDate = dueDate; }

    public long getReturnDate() { return returnDate; }
    public void setReturnDate(long returnDate) { this.returnDate = returnDate; }

    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }

    public double getFinePerDay() { return finePerDay; }
    public void setFinePerDay(double finePerDay) { this.finePerDay = finePerDay; }

    public double getFineAmount() { return fineAmount; }
    public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }

    /**
     * Calculate current fine (live — not stored until return)
     */
    public double calculateCurrentFine() {
        if (returned) return fineAmount;
        long now = System.currentTimeMillis();
        if (now <= dueDate) return 0;
        long overdueDays = (now - dueDate) / (1000 * 60 * 60 * 24);
        return overdueDays * finePerDay;
    }
}
//package com.example.libreman.model;
//
//public class BorrowRecord {
//
//    private String recordId;
//    private String bookId;
//    private String memberId;
//    private long borrowDate;
//    private long dueDate;
//    private long returnDate;   // 0 means not yet returned
//    private boolean returned;
//
//    // Required empty constructor for Firebase
//    public BorrowRecord() {}
//
//    public BorrowRecord(String bookId, String memberId, long borrowDate, long dueDate) {
//        this.bookId = bookId;
//        this.memberId = memberId;
//        this.borrowDate = borrowDate;
//        this.dueDate = dueDate;
//        this.returnDate = 0;
//        this.returned = false;
//    }
//
//    // Getters and Setters
//    public String getRecordId() { return recordId; }
//    public void setRecordId(String recordId) { this.recordId = recordId; }
//
//    public String getBookId() { return bookId; }
//    public void setBookId(String bookId) { this.bookId = bookId; }
//
//    public String getMemberId() { return memberId; }
//    public void setMemberId(String memberId) { this.memberId = memberId; }
//
//    public long getBorrowDate() { return borrowDate; }
//    public void setBorrowDate(long borrowDate) { this.borrowDate = borrowDate; }
//
//    public long getDueDate() { return dueDate; }
//    public void setDueDate(long dueDate) { this.dueDate = dueDate; }
//
//    public long getReturnDate() { return returnDate; }
//    public void setReturnDate(long returnDate) { this.returnDate = returnDate; }
//
//    public boolean isReturned() { return returned; }
//    public void setReturned(boolean returned) { this.returned = returned; }
//}