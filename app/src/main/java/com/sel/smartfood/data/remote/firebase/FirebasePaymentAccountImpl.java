package com.sel.smartfood.data.remote.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sel.smartfood.data.model.PaymentAccount;

import java.util.Map;

import io.reactivex.rxjava3.core.Single;

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
    public Single<PaymentAccount> getBalance(@NonNull String key) {
        return Single.create(emitter -> {
            ref.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount() != 1){
                        emitter.onError(new Exception("Error"));
                        return;
                    }
                    for (DataSnapshot node : snapshot.getChildren()){
                        emitter.onSuccess(new PaymentAccount(node.getValue(Long.class)));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
        });
    }

    @Nullable
    @Override
    public Single<Boolean> updateBalance(@NonNull String key, @NonNull Long balance) {
        return Single.create(emitter -> {
            ref.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount() != 1){
                        emitter.onError(new Exception("Error"));
                        return;
                    }
                    ref.child(key).child("balance").setValue(balance);
                    emitter.onSuccess(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
        });
    }
}
