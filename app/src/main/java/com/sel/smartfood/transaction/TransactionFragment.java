package com.sel.smartfood.transaction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sel.smartfood.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment {

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
}
