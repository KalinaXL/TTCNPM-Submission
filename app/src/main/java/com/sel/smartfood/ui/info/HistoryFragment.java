package com.sel.smartfood.ui.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sel.smartfood.R;

public class HistoryFragment extends Fragment{
    private RelativeLayout orderHistoryRL;
    private RelativeLayout transactionHistoryRL;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.layout_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderHistoryRL = view.findViewById(R.id.rl_order_history);
        transactionHistoryRL = view.findViewById(R.id.rl_transaction_history);
    }
}
