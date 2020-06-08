package com.sel.smartfood.ui.transaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sel.smartfood.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment {

    private CardView withdrawCv;
    private CardView depositCv;
    private BottomSheetDialog bottomSheetDialog;

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
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        withdrawCv = view.findViewById(R.id.include_withdraw);
        depositCv = view.findViewById(R.id.include_deposit);
        withdrawCv.setOnClickListener(this::displayBottomSheet);
        depositCv.setOnClickListener(this::displayBottomSheet);
    }
    public void displayBottomSheet(View view){
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.layout_choose_payment_type, null, false);
        bottomSheetView.findViewById(R.id.ll_online_payment_type).setOnClickListener(this::navigateToPaymentServiceChoice);
        bottomSheetView.findViewById(R.id.ll_payment_account_type).setOnClickListener(v->{
            Toast.makeText(requireContext(), "Payment account", Toast.LENGTH_SHORT).show();
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
    private void navigateToPaymentServiceChoice(View v){
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_transaction_to_choosePaymentServiceFragment);
        bottomSheetDialog.dismiss();
    }
}
