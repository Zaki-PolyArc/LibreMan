package com.example.libreman.firebase;

import androidx.annotation.NonNull;

import com.example.libreman.model.Member;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MemberHelper {

    private final DatabaseReference membersRef;

    public MemberHelper() {
        membersRef = FirebaseDatabase
                .getInstance("https://libreman-f9839-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("members");
    }

    // ======================== CALLBACK INTERFACES ========================

    public interface MemberCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    public interface SingleMemberCallback {
        void onMemberLoaded(Member member);
        void onError(String error);
    }

    public interface MemberListCallback {
        void onMembersLoaded(List<Member> members);
        void onError(String error);
    }

    // ======================== STORE ========================

    public void insertMember(Member member, MemberCallback callback) {
        String key = membersRef.push().getKey();
        if (key == null) {
            callback.onFailure("Failed to generate member ID.");
            return;
        }
        member.setMemberId(key);
        membersRef.child(key).setValue(member)
                .addOnSuccessListener(unused -> callback.onSuccess("Member added with ID: " + key))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void updateMember(Member member, MemberCallback callback) {
        if (member.getMemberId() == null) {
            callback.onFailure("Member ID is null.");
            return;
        }
        membersRef.child(member.getMemberId()).setValue(member)
                .addOnSuccessListener(unused -> callback.onSuccess("Member updated."))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void deleteMember(String memberId, MemberCallback callback) {
        membersRef.child(memberId).removeValue()
                .addOnSuccessListener(unused -> callback.onSuccess("Member deleted."))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // ======================== RETRIEVE ========================

    public void getMemberById(String memberId, SingleMemberCallback callback) {
        membersRef.child(memberId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Member member = snapshot.getValue(Member.class);
                if (member != null) {
                    callback.onMemberLoaded(member);
                } else {
                    callback.onError("Member not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void getAllMembers(MemberListCallback callback) {
        membersRef.orderByChild("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Member> members = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Member member = child.getValue(Member.class);
                    if (member != null) members.add(member);
                }
                callback.onMembersLoaded(members);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void searchMembersByName(String query, MemberListCallback callback) {
        membersRef.orderByChild("fullName")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Member> members = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Member member = child.getValue(Member.class);
                            if (member != null) members.add(member);
                        }
                        callback.onMembersLoaded(members);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }
}
