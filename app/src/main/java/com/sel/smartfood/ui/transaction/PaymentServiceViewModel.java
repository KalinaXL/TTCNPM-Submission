package com.sel.smartfood.ui.transaction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sel.smartfood.R;

import java.util.ArrayList;
import java.util.List;

public class PaymentServiceViewModel extends ViewModel {
    private MutableLiveData<List<PaymentService>> paymentService = new MutableLiveData<>();
    private List<PaymentService> paymentServiceList;
    private MutableLiveData<Boolean> isNextButtonEnabled = new MutableLiveData<>();

    public PaymentServiceViewModel(){
        paymentServiceList = getPaymentServiceList();
        this.paymentService.setValue(paymentServiceList);
    }

    public LiveData<List<PaymentService>> getPaymentService() {
        return paymentService;
    }

    public LiveData<Boolean> getIsNextButtonEnabled() {
        return isNextButtonEnabled;
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
