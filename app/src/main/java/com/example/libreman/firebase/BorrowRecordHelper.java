package com.example.libreman.firebase;

import androidx.annotation.NonNull;

import com.example.libreman.model.BorrowRecord;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BorrowRecordHelper {

    private final DatabaseReference recordsRef;

    public BorrowRecordHelper() {
        recordsRef = FirebaseDatabase
                .getInstance("https://libreman-f9839-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("borrow_records");
    }

    // ======================== CALLBACK INTERFACES ========================

    public interface RecordCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    public interface SingleRecordCallback {
        void onRecordLoaded(BorrowRecord record);
        void onError(String error);
    }

    public interface RecordListCallback {
        void onRecordsLoaded(List<BorrowRecord> records);
        void onError(String error);
    }

    // ======================== STORE ========================

    public void insertRecord(BorrowRecord record, RecordCallback callback) {
        String key = recordsRef.push().getKey();
        if (key == null) {
            callback.onFailure("Failed to generate record ID.");
            return;
        }
        record.setRecordId(key);
        recordsRef.child(key).setValue(record)
                .addOnSuccessListener(unused -> callback.onSuccess("Record added with ID: " + key))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void updateRecord(BorrowRecord record, RecordCallback callback) {
        if (record.getRecordId() == null) {
            callback.onFailure("Record ID is null.");
            return;
        }
        recordsRef.child(record.getRecordId()).setValue(record)
                .addOnSuccessListener(unused -> callback.onSuccess("Record updated."))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // ======================== RETRIEVE ========================

    public void getRecordById(String recordId, SingleRecordCallback callback) {
        recordsRef.child(recordId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BorrowRecord record = snapshot.getValue(BorrowRecord.class);
                if (record != null) {
                    callback.onRecordLoaded(record);
                } else {
                    callback.onError("Record not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void getAllRecords(RecordListCallback callback) {
        recordsRef.orderByChild("borrowDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<BorrowRecord> records = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    BorrowRecord record = child.getValue(BorrowRecord.class);
                    if (record != null) records.add(record);
                }
                callback.onRecordsLoaded(records);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void getActiveRecordsByMember(String memberId, RecordListCallback callback) {
        recordsRef.orderByChild("memberId").equalTo(memberId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<BorrowRecord> records = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            BorrowRecord record = child.getValue(BorrowRecord.class);
                            if (record != null && !record.isReturned()) {
                                records.add(record);
                            }
                        }
                        callback.onRecordsLoaded(records);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    public void getActiveRecordsByBook(String bookId, RecordListCallback callback) {
        recordsRef.orderByChild("bookId").equalTo(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<BorrowRecord> records = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            BorrowRecord record = child.getValue(BorrowRecord.class);
                            if (record != null && !record.isReturned()) {
                                records.add(record);
                            }
                        }
                        callback.onRecordsLoaded(records);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    public void getAllActiveRecords(RecordListCallback callback) {
        recordsRef.orderByChild("returned").equalTo(false)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<BorrowRecord> records = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            BorrowRecord record = child.getValue(BorrowRecord.class);
                            if (record != null) records.add(record);
                        }
                        callback.onRecordsLoaded(records);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    public void getOverdueRecords(RecordListCallback callback) {
        long now = System.currentTimeMillis();
        recordsRef.orderByChild("returned").equalTo(false)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<BorrowRecord> records = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            BorrowRecord record = child.getValue(BorrowRecord.class);
                            if (record != null && record.getDueDate() < now) {
                                records.add(record);
                            }
                        }
                        callback.onRecordsLoaded(records);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }
}
//package com.example.libreman.firebase;
//
//import androidx.annotation.NonNull;
//
//import com.example.libreman.model.BorrowRecord;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BorrowRecordHelper {
//    public BorrowRecordHelper() {
//        recordsRef = FirebaseDatabase
//                .getInstance("https://libreman-f9839-default-rtdb.asia-southeast1.firebasedatabase.app/")
//                .getReference("borrow_records");
//    }
//
//    private final DatabaseReference recordsRef;
//
//    // ======================== CALLBACK INTERFACES ========================
//
//    public interface RecordCallback {
//        void onSuccess(String message);
//        void onFailure(String error);
//    }
//
//    public interface SingleRecordCallback {
//        void onRecordLoaded(BorrowRecord record);
//        void onError(String error);
//    }
//
//    public interface RecordListCallback {
//        void onRecordsLoaded(List<BorrowRecord> records);
//        void onError(String error);
//    }
//
//    // ======================== STORE ========================
//
//    public void insertRecord(BorrowRecord record, RecordCallback callback) {
//        String key = recordsRef.push().getKey();
//        if (key == null) {
//            callback.onFailure("Failed to generate record ID.");
//            return;
//        }
//        record.setRecordId(key);
//        recordsRef.child(key).setValue(record)
//                .addOnSuccessListener(unused -> callback.onSuccess("Record added with ID: " + key))
//                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
//    }
//
//    public void updateRecord(BorrowRecord record, RecordCallback callback) {
//        if (record.getRecordId() == null) {
//            callback.onFailure("Record ID is null.");
//            return;
//        }
//        recordsRef.child(record.getRecordId()).setValue(record)
//                .addOnSuccessListener(unused -> callback.onSuccess("Record updated."))
//                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
//    }
//
//    // ======================== RETRIEVE ========================
//
//    public void getRecordById(String recordId, SingleRecordCallback callback) {
//        recordsRef.child(recordId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                BorrowRecord record = snapshot.getValue(BorrowRecord.class);
//                if (record != null) {
//                    callback.onRecordLoaded(record);
//                } else {
//                    callback.onError("Record not found.");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                callback.onError(error.getMessage());
//            }
//        });
//    }
//
//    public void getAllRecords(RecordListCallback callback) {
//        recordsRef.orderByChild("borrowDate").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<BorrowRecord> records = new ArrayList<>();
//                for (DataSnapshot child : snapshot.getChildren()) {
//                    BorrowRecord record = child.getValue(BorrowRecord.class);
//                    if (record != null) records.add(record);
//                }
//                callback.onRecordsLoaded(records);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                callback.onError(error.getMessage());
//            }
//        });
//    }
//
//    public void getActiveRecordsByMember(String memberId, RecordListCallback callback) {
//        recordsRef.orderByChild("memberId").equalTo(memberId)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        List<BorrowRecord> records = new ArrayList<>();
//                        for (DataSnapshot child : snapshot.getChildren()) {
//                            BorrowRecord record = child.getValue(BorrowRecord.class);
//                            if (record != null && !record.isReturned()) {
//                                records.add(record);
//                            }
//                        }
//                        callback.onRecordsLoaded(records);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        callback.onError(error.getMessage());
//                    }
//                });
//    }
//
//    public void getActiveRecordsByBook(String bookId, RecordListCallback callback) {
//        recordsRef.orderByChild("bookId").equalTo(bookId)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        List<BorrowRecord> records = new ArrayList<>();
//                        for (DataSnapshot child : snapshot.getChildren()) {
//                            BorrowRecord record = child.getValue(BorrowRecord.class);
//                            if (record != null && !record.isReturned()) {
//                                records.add(record);
//                            }
//                        }
//                        callback.onRecordsLoaded(records);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        callback.onError(error.getMessage());
//                    }
//                });
//    }
//
//    public void getAllActiveRecords(RecordListCallback callback) {
//        recordsRef.orderByChild("returned").equalTo(false)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        List<BorrowRecord> records = new ArrayList<>();
//                        for (DataSnapshot child : snapshot.getChildren()) {
//                            BorrowRecord record = child.getValue(BorrowRecord.class);
//                            if (record != null) records.add(record);
//                        }
//                        callback.onRecordsLoaded(records);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        callback.onError(error.getMessage());
//                    }
//                });
//    }
//
//    public void getOverdueRecords(RecordListCallback callback) {
//        long now = System.currentTimeMillis();
//        recordsRef.orderByChild("returned").equalTo(false)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        List<BorrowRecord> records = new ArrayList<>();
//                        for (DataSnapshot child : snapshot.getChildren()) {
//                            BorrowRecord record = child.getValue(BorrowRecord.class);
//                            if (record != null && record.getDueDate() < now) {
//                                records.add(record);
//                            }
//                        }
//                        callback.onRecordsLoaded(records);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        callback.onError(error.getMessage());
//                    }
//                });
//    }
//}
