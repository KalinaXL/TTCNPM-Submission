package com.sel.smartfood.login;

import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;

public class FirebaseService {
   private FirebaseAuthentication firebaseAuth;
   private FirebasePaymentAccount firebasePayment;
   public FirebaseService(){
       this.firebaseAuth = new FirebaseAuthenticationImpl();
       this.firebasePayment = new FirebasePaymentAccountImpl();
   }
   public Completable login(String username, String password){
       return firebaseAuth.loginWithEmail(username, password);
   }
   public FirebaseUser getCurrentUser(){
       return firebaseAuth.getCurrentUser();
   }
   public Float getBalance(String uuid){
       return firebasePayment.getBalance(uuid);
   }
}
