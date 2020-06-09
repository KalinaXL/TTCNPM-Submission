package com.sel.smartfood.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sel.smartfood.R;

public class ChoosePaymentServiceFragment extends Fragment {
    private ListView paymentServiceLv;
    private PaymentServiceViewModel paymentServiceViewModel;
    private PaymentServiceAdapter paymentServiceAdapter;
    private Button nextPaymentBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_payment_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        paymentServiceLv = view.findViewById(R.id.lv_payment_service);
        nextPaymentBtn = view.findViewById(R.id.btn_next_payment);
        paymentServiceViewModel = new ViewModelProvider(this).get(PaymentServiceViewModel.class);

        paymentServiceAdapter = new PaymentServiceAdapter(getContext());
        paymentServiceLv.setAdapter(paymentServiceAdapter);


        paymentServiceLv.setOnItemClickListener((adapter, v, position, arg)->{
            paymentServiceViewModel.chooseOnePaymentService(position);
        });
        paymentServiceViewModel.getPaymentService().observe(getViewLifecycleOwner(), paymentServiceList->{
            if (paymentServiceList == null){
                Toast.makeText(getContext(), R.string.payment_service_choice_error, Toast.LENGTH_SHORT).show();
                return;
            }
            paymentServiceAdapter.changeDataset(paymentServiceList);
        });
        paymentServiceViewModel.getIsNextButtonEnabled().observe(getViewLifecycleOwner(), isButtonEnabled->{
            if (isButtonEnabled == null)
                return;
            nextPaymentBtn.setEnabled(isButtonEnabled);
        });
    }
}
