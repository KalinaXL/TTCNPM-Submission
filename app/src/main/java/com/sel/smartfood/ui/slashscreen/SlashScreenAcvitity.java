package com.sel.smartfood.ui.slashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.sel.smartfood.ui.login.LoginActivity;
import com.sel.smartfood.ui.main.MainActivity;
import com.sel.smartfood.viewmodel.SigninViewModel;

public class SlashScreenAcvitity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SigninViewModel signinViewModel = new ViewModelProvider(this).get(SigninViewModel.class);
        signinViewModel.checkLoggedInState();
        signinViewModel.IsLoggedIn().observe(this, isLoggedIn ->{
            Handler handler = new Handler();
            if (isLoggedIn == null || !isLoggedIn){
                Intent intent = new Intent(this, LoginActivity.class);
                handler.post(() -> startActivity(intent));
            }
            else{
                Intent intent = new Intent(this, MainActivity.class);
                handler.post(() -> startActivity(intent));
            }
        });
    }
}
