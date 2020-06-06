package com.sel.smartfood.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface FirebasePaymentAccount {
    @Nullable
    Float getBalance(String uuid);
}
