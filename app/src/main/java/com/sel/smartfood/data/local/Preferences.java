package com.sel.smartfood.data.local;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private SharedPreferences sharedPreferences;
    public Preferences(Application application, String name){
        sharedPreferences = application.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
    public void saveBooleanValue(String key, boolean value){
        sharedPreferences.edit().putBoolean(key, value).apply();
    }
    public boolean getBooleanValue(String key){
        return sharedPreferences.getBoolean(key, false);
    }
}
