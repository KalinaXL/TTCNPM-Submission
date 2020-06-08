package com.sel.smartfood.login.data.remote.firebase;

import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;

public class FirebaseService {
   private final FirebaseAuthentication firebaseAuth;
   private final FirebasePaymentAccount firebasePayment;
   private final FirebaseRegistration firebaseRegistration;

   public FirebaseService(FirebaseAuthentication firebaseAuthentication,
                           FirebasePaymentAccount firebasePaymentAccount,
                           FirebaseRegistration firebaseRegistration){
       this.firebaseAuth = firebaseAuthentication;
       this.firebasePayment = firebasePaymentAccount;
       this.firebaseRegistration = firebaseRegistration;
   }

   public Completable login(String username, String password){
       return firebaseAuth.loginWithEmail(username, password);
   }

   public Completable register(String email, String password){
       return firebaseRegistration.register(email, password);
   }

   public FirebaseUser getCurrentUser(){
       return firebaseAuth.getCurrentUser();
   }
   public Float getBalance(String uuid){
       return firebasePayment.getBalance(uuid);
   }

   public interface Builder{
       Builder addAuth(FirebaseAuthentication auth);
       Builder addPaymentAccount(FirebasePaymentAccount paymentAccount);
       Builder addRegistration(FirebaseRegistration firebaseRegistration);
       FirebaseService build();
   }
}
