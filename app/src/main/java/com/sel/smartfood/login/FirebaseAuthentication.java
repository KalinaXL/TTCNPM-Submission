package com.sel.smartfood.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;

public interface FirebaseAuthentication {
    @NonNull
    Completable loginWithEmail(String username, String password);
    @Nullable
    FirebaseUser getCurrentUser();
}
