package com.sel.smartfood.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sel.smartfood.R;
import com.sel.smartfood.viewmodel.ShopCartModel;
import com.sel.smartfood.viewmodel.ShopViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShopViewModel shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel.getCategories();
        shopViewModel.getProducts();

        BottomNavigationView bnvTab = findViewById(R.id.bnv_tab);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bnvTab, navController);


    }

}
