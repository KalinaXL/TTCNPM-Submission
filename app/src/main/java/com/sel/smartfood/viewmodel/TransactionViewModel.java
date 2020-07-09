package com.sel.smartfood.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sel.smartfood.R;
import com.sel.smartfood.data.model.PaymentAccount;
import com.sel.smartfood.data.remote.firebase.FirebasePaymentAccountImpl;
import com.sel.smartfood.data.remote.firebase.FirebaseService;
import com.sel.smartfood.data.remote.firebase.FirebaseServiceBuilder;
import com.sel.smartfood.ui.transaction.PaymentService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TransactionViewModel extends ViewModel {
    private MutableLiveData<List<PaymentService>> paymentService = new MutableLiveData<>();
    private List<PaymentService> paymentServiceList;
    private MutableLiveData<Boolean> isNextButtonEnabled = new MutableLiveData<>();
    private MutableLiveData<Boolean> isTransactionButtonEnabled = new MutableLiveData<>();
    private FirebaseService firebaseService;
    private MutableLiveData<PaymentAccount> paymentAccount = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdatedSuccessful = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable;

    public TransactionViewModel(){
        paymentServiceList = getPaymentServiceList();
        this.paymentService.setValue(paymentServiceList);
        firebaseService = new FirebaseServiceBuilder().addPaymentAccount(new FirebasePaymentAccountImpl()).build();
        compositeDisposable = new CompositeDisposable();
    }

    public void getBalance(){
        Disposable d = firebaseService.getBalance("admin")
                        .subscribeOn(Schedulers.io())
                        .subscribe(account -> paymentAccount.postValue(account), e -> paymentAccount.postValue(null));
        compositeDisposable.add(d);
    }
    public void updateBalance(long balance){
        balance = paymentAccount.getValue().getBalance() - balance;
        Disposable d = firebaseService.updateBalance("admin", balance)
                        .subscribeOn(Schedulers.io())
                        .subscribe(isUpdated -> isUpdatedSuccessful.postValue(isUpdated), e -> isUpdatedSuccessful.postValue(null));
        compositeDisposable.add(d);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
        if (!compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }

    public LiveData<List<PaymentService>> getPaymentService() {
        return paymentService;
    }

    public LiveData<Boolean> getIsNextButtonEnabled() {
        return isNextButtonEnabled;
    }

    public LiveData<Boolean> getIsTransactionButtonEnabled() {
        return isTransactionButtonEnabled;
    }

    public LiveData<PaymentAccount> getPaymentAccount() {
        return paymentAccount;
    }

    public LiveData<Boolean> getIsUpdatedSuccessful() {
        return isUpdatedSuccessful;
    }

    public void amountOfMoneyChange(String number){
        try{
            int amount = Integer.parseInt(number);
            if (amount > 0 && amount % 1000 == 0){
                isTransactionButtonEnabled.setValue(true);
            }
            else{
                isTransactionButtonEnabled.setValue(false);
            }
        }
        catch (NumberFormatException e){
            isTransactionButtonEnabled.setValue(false);
        }

    }

    public void chooseOnePaymentService(int position){
        boolean flag = false;
        for (int i = 0; i < paymentServiceList.size(); i++){
            if (i == position){
                PaymentService paymentChoosed = paymentServiceList.get(i);
                paymentChoosed.setChoosed(!paymentChoosed.isChoosed());
                flag = paymentChoosed.isChoosed();
            }
            else{
                paymentServiceList.get(i).setChoosed(false);
            }
        }
        this.paymentService.setValue(paymentServiceList);
        this.isNextButtonEnabled.setValue(flag);
    }
    private List<PaymentService> getPaymentServiceList(){
        int[] paymentImageSources = getPaymentImageSources();
        String[] paymentServiceNames = getPaymentServiceNames();
        List<PaymentService> list = new ArrayList<>();
        int size = paymentImageSources.length;
        for (int i = 0; i < size; i++){
            list.add(new PaymentService(paymentImageSources[i], paymentServiceNames[i]));
        }
        return list;
    }
    private int[] getPaymentImageSources(){
        return new int[]{
                R.drawable.momo_pay,
                R.drawable.viettel_pay,
                R.drawable.samsung_pay,
                R.drawable.apple_pay
        };
    }
    private String[] getPaymentServiceNames(){
        return new String[]{
          "Momo",
          "ViettelPay",
          "SamsungPay",
          "ApplePay"
        };
    }
}
