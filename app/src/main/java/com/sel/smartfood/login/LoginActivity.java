package com.sel.smartfood.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.sel.smartfood.MainActivity;
import com.sel.smartfood.R;
import com.sel.smartfood.utils.Result;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEt;
    private EditText passwordEt;
    private Button loginBtn;
    private ProgressBar loadingPb;
    private LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findWidgets();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getLoginFormState().observe(this, loginFormState->{
            if (loginFormState == null)
                return;
            loginBtn.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null){
                usernameEt.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null){
                passwordEt.setError(getString(loginFormState.getPasswordError()));
            }
        });
        loginViewModel.getLoginResult().observe(this, loginResult->{
            if (loginResult == null){
                loginBtn.setEnabled(true);
                loadingPb.setVisibility(View.GONE);
                Toast.makeText(this, LoginViewModel.LOGIN_ERROR_MESSAGE , Toast.LENGTH_SHORT).show();
                return;
            }
            if (loginResult instanceof Result.Success){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                loginBtn.setEnabled(true);
                loadingPb.setVisibility(View.GONE);
                Toast.makeText(this, ((Result.Error)loginResult).getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEt.getText().toString(), passwordEt.getText().toString());
            }
        };
        usernameEt.addTextChangedListener(afterTextChangedListener);
        passwordEt.addTextChangedListener(afterTextChangedListener);

        loginBtn.setOnClickListener(v->{
            loginViewModel.login(usernameEt.getText().toString(), passwordEt.getText().toString());
            loadingPb.setVisibility(View.VISIBLE);
            loginBtn.setEnabled(false);
        });

    }
    private void findWidgets(){
        usernameEt = findViewById(R.id.et_username);
        passwordEt = findViewById(R.id.et_password);
        loginBtn = findViewById(R.id.btn_login);
        loadingPb = findViewById(R.id.pb_loading);
    }
}
