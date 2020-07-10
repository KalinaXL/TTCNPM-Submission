package com.sel.smartfood.data.remote.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sel.smartfood.data.model.PaymentAccount;

import io.reactivex.rxjava3.core.Single;

public interface FirebasePaymentAccount {
    @Nullable
    Single<PaymentAccount> getBalance(@NonNull String key);

    void updateBalance(@NonNull String key, @NonNull Long balance);
    void saveTransHistory(String email, Long amountOfMoney, String service, String date, boolean isWithdraw);
}
