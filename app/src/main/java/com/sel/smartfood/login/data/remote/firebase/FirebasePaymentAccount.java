package com.sel.smartfood.login.data.remote.firebase;

import androidx.annotation.Nullable;

public interface FirebasePaymentAccount {
    @Nullable
    Float getBalance(String uuid);
}
