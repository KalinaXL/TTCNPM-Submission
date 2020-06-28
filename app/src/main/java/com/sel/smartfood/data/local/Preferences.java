package com.sel.smartfood.data.local;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private SharedPreferences sharedPreferences;
    public Preferences(Activity activity){
        sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    }
    public void saveBooleanValue(String key, boolean value){
        sharedPreferences.edit().putBoolean(key, value).apply();
    }
}
