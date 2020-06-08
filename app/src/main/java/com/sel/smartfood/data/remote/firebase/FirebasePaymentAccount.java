package com.sel.smartfood.data.remote.firebase;

import androidx.annotation.Nullable;

public interface FirebasePaymentAccount {
    @Nullable
    Float getBalance(String uuid);
}
