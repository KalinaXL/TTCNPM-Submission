package com.sel.smartfood.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sel.smartfood.ui.main.MainActivity;
import com.sel.smartfood.R;
import com.sel.smartfood.data.model.SigninFormState;
import com.sel.smartfood.data.model.Result;

public class SigninFragment extends Fragment {
    private EditText usernameEt;
    private EditText passwordEt;
    private Button loginBtn;
    private ProgressBar loadingPb;
    private TextView registerTv;
    private RegisterFragment.SigninViewModel signinViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.layout_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findWidgets(view);
        signinViewModel = new ViewModelProvider(this).get(RegisterFragment.SigninViewModel.class);
        signinViewModel.getSigninFormState().observe(getViewLifecycleOwner(), this::handleSigninFormState);
        signinViewModel.getSigninResult().observe(getViewLifecycleOwner(), this::handleSigninResult);

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                signinViewModel.loginDataChanged(usernameEt.getText().toString(), passwordEt.getText().toString());
            }
        };
        usernameEt.addTextChangedListener(afterTextChangedListener);
        passwordEt.addTextChangedListener(afterTextChangedListener);

        loginBtn.setOnClickListener(v -> {
            signinViewModel.login(usernameEt.getText().toString(), passwordEt.getText().toString());
            loadingPb.setVisibility(View.VISIBLE);
            loginBtn.setEnabled(false);
        });

        registerTv.setOnClickListener(v -> {
            getParentFragmentManager()
            .beginTransaction()
            .replace(R.id.fcv_login_activity, new RegisterFragment())
            .addToBackStack(null)
            .commit();
        });

    }

    private void handleSigninFormState(SigninFormState signinFormState){
        if (signinFormState == null)
            return;
        loginBtn.setEnabled(signinFormState.isDataValid());
        if (signinFormState.getUsernameError() != null){
            usernameEt.setError(getString(signinFormState.getUsernameError()));
        }
        if (signinFormState.getPasswordError() != null){
            passwordEt.setError(getString(signinFormState.getPasswordError()));
        }
    }

    private void handleSigninResult(Result<Boolean> loginResult){
            if (loginResult == null){
                loginBtn.setEnabled(true);
                loadingPb.setVisibility(View.GONE);
                Toast.makeText(requireActivity(), RegisterFragment.SigninViewModel.LOGIN_ERROR_MESSAGE , Toast.LENGTH_SHORT).show();
                return;
            }
            if (loginResult instanceof Result.Success){
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
            else{
                loginBtn.setEnabled(true);
                loadingPb.setVisibility(View.GONE);
                Toast.makeText(requireActivity(), ((Result.Error)loginResult).getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
    }
    private void findWidgets(View view){
        usernameEt = view.findViewById(R.id.et_username);
        passwordEt = view.findViewById(R.id.et_password);
        loginBtn = view.findViewById(R.id.btn_sign_in);
        loadingPb = view.findViewById(R.id.pb_loading);
        registerTv = view.findViewById(R.id.tv_register_hint);
    }
}
