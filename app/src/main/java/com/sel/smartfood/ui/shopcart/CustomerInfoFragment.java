package com.sel.smartfood.ui.shopcart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.sel.smartfood.R;

public class CustomerInfoFragment extends Fragment {

    EditText    ed_customer_name;
    EditText    ed_customer_phone;
    EditText    ed_customer_email;
    Spinner spinner_paymemt;

    public CustomerInfoFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_info, container, false);
        Maps(view);
        CatchEventSpinner();

        return view;
    }

    private void Maps(View view) {
        ed_customer_name = view.findViewById(R.id.ed_customer_name);
        ed_customer_phone = view.findViewById(R.id.ed_customer_phone);
        ed_customer_email = view.findViewById(R.id.ed_customer_email);
        spinner_paymemt = view.findViewById(R.id.spinner_payment);

    }

    private void CatchEventSpinner() {
        String[] payment_type = new String[]{"Momo", "OCB", "Payoo"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.requireActivity(), android.R.layout.simple_spinner_dropdown_item, payment_type );
        spinner_paymemt.setAdapter(arrayAdapter);
    }

}