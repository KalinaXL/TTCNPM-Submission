package com.sel.smartfood.transaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sel.smartfood.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment {

    private CardView withdrawCv;
    private CardView depositCv;
    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_transaction, container, false);
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        withdrawCv = view.findViewById(R.id.include_withdraw);
        depositCv = view.findViewById(R.id.include_deposit);
    }
    public void displayBottomSheet(View view){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.Theme_Design_BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.item_payment_service, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void navigateToPaymentSrviceChoice(View v){
        Navigation.findNavController(v).navigate(R.id.action_nav_transaction_to_choosePaymentServiceFragment);
    }
}
