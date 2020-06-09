package com.sel.smartfood.login.data.remote.firebase;

public class FirebaseServiceBuilder implements FirebaseService.Builder{
    private FirebaseAuthentication firebaseAuth;
    private FirebasePaymentAccount firebasePaymentAccount;
    private FirebaseRegistration firebaseRegistration;

    @Override
    public FirebaseService.Builder addAuth(FirebaseAuthentication auth) {
        this.firebaseAuth = auth;
        return this;
    }

    @Override
    public FirebaseService.Builder addPaymentAccount(FirebasePaymentAccount paymentAccount) {
        this.firebasePaymentAccount = paymentAccount;
        return this;
    }

    @Override
    public FirebaseService.Builder addRegistration(FirebaseRegistration firebaseRegistration) {
        this.firebaseRegistration = firebaseRegistration;
        return this;
    }

    @Override
    public FirebaseService build() {
        return new FirebaseService(firebaseAuth,
                                   firebasePaymentAccount,
                                   firebaseRegistration);
    }
}
