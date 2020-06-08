package com.sel.smartfood.login.data.remote.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.reactivex.rxjava3.core.Completable;

public class FirebaseRegistrationImpl implements FirebaseRegistration{
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    public FirebaseRegistrationImpl(){
        firebaseAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference().child("PaymentAccounts");
    }
    public Completable register(String email, String password){
        Exception arr[] = {null};
        return Completable.create(emitter -> {
           firebaseAuth.createUserWithEmailAndPassword(email, password)
                       .addOnCompleteListener(task -> {
                           if (!emitter.isDisposed() && task.isSuccessful()){
//                               uid[0] = Objects.requireNonNull(task.getResult().getUser()).getUid();
                           }
                           else{
                               arr[0] = task.getException();
                           }
                       });
           if (arr[0] != null){
               emitter.onError(arr[0]);
               return;
           }
           ref.child(email.split("@")[0]).setValue(0, (databaseError, databaseReference) -> {
                if (databaseError == null){
                    emitter.onComplete();
                }
                else{
                    emitter.onError(new Exception("Lỗi"));
                }
           });
        });
    }
}
