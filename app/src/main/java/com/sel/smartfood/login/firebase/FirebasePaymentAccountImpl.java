package com.sel.smartfood.login.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebasePaymentAccountImpl implements FirebasePaymentAccount {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private static FirebasePaymentAccountImpl instance;
    public FirebasePaymentAccountImpl(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference().child("PaymentAccounts");
    }
    @Nullable
    @Override
    public Float getBalance(String uuid) {
        final Float[] balance = {null};
        ref.child(uuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 balance[0] = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                balance[0] = null;
            }
        });
        return balance[0];
    }

}
