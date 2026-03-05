package com.example.libreman.util;

import com.example.libreman.model.BorrowRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FineCalculator {

    public static final double FINE_PER_DAY = 100.0; // Rs.100 per day
    public static final int BORROW_DAYS = 14;
    public static final int MAX_BORROW_LIMIT = 5;

    public static long calculateDueDate(long borrowDate) {
        return borrowDate + TimeUnit.DAYS.toMillis(BORROW_DAYS);
    }

    public static long getOverdueDays(long dueDate) {
        long now = System.currentTimeMillis();
        if (now <= dueDate) return 0;
        return TimeUnit.MILLISECONDS.toDays(now - dueDate);
    }

    public static double calculateFine(long dueDate) {
        return getOverdueDays(dueDate) * FINE_PER_DAY;
    }

    public static double calculateFineForRecord(BorrowRecord record) {
        if (record.isReturned()) return record.getFineAmount();
        return calculateFine(record.getDueDate());
    }

    public static boolean isOverdue(long dueDate) {
        return System.currentTimeMillis() > dueDate;
    }

    public static boolean isDueSoon(long dueDate) {
        long diff = dueDate - System.currentTimeMillis();
        return diff > 0 && diff <= TimeUnit.DAYS.toMillis(2); // due within 2 days
    }

    public static String formatDate(long timestamp) {
        return new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date(timestamp));
    }

    public static String getFineStatus(BorrowRecord record) {
        if (record.isReturned()) {
            if (record.getFineAmount() > 0) {
                return "Returned — Fine: Rs." + (int) record.getFineAmount();
            }
            return "Returned";
        }

        long overdue = getOverdueDays(record.getDueDate());
        if (overdue > 0) {
            double fine = overdue * FINE_PER_DAY;
            return "⚠️ Overdue " + overdue + " days — Fine: Rs." + (int) fine;
        }

        if (isDueSoon(record.getDueDate())) {
            return "⏰ Due soon!";
        }

        return "Due: " + formatDate(record.getDueDate());
    }
}