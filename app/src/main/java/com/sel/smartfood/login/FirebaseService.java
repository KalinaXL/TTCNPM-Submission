package com.sel.smartfood.login;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;

public class FirebaseService {
    private static FirebaseService instance;
    private FirebaseAuth firebaseAuth;
    private FirebaseService(){
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public static FirebaseService getInstance(){
        if (instance == null){
            synchronized (FirebaseService.class){
                if (instance == null){
                    instance = new FirebaseService();
                }
            }
        }
        return instance;
    }
    public Completable loginWithEmail(String email, String password){
        return Completable.create(emitter->{
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (!emitter.isDisposed() && task.isSuccessful()){
                            emitter.onComplete();
                        }
                        else{
                            emitter.onError(task.getException());
                        }
                    });
        });
    }
    public final FirebaseUser getCurrentUser(){
        return firebaseAuth.getCurrentUser();
    }
}
