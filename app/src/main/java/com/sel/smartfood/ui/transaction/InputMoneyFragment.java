package com.sel.smartfood.ui.transaction;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sel.smartfood.R;
import com.sel.smartfood.viewmodel.TransactionViewModel;

public class InputMoneyFragment extends Fragment {
    private EditText inputMoneyEt;
    private Button transBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_input_money, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findWidgets(view);

        TransactionViewModel viewModel = new ViewModelProvider(getActivity()).get(TransactionViewModel.class);

        inputMoneyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.amountOfMoneyChange(inputMoneyEt.getText().toString());
            }
        });

        viewModel.getIsTransactionButtonEnabled().observe(getViewLifecycleOwner(), isEnabled -> {
            if (isEnabled == null || !isEnabled){
                inputMoneyEt.setError("Số tiền phải là số nguyên dương");
            }
            else{
                transBtn.setEnabled(true);
            }
        });

        transBtn.setOnClickListener(v -> {
            viewModel.updateBalance(Long.valueOf(inputMoneyEt.getText().toString()));
        });
        viewModel.getIsUpdatedSuccessful().observe(getViewLifecycleOwner(), isSuccessful -> {
            if (isSuccessful != null){
                if (isSuccessful){
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findWidgets(View view){
        inputMoneyEt = view.findViewById(R.id.et_input_money);
        transBtn = view.findViewById(R.id.btn_transaction);
    }
}
