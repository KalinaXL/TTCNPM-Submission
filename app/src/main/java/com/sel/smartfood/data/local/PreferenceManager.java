package com.sel.smartfood.data.local;

import android.app.Application;

public class PreferenceManager {
    public final static String PREFERENCE_NAME = "signin";
    public final static String LOGGED_IN_STATE_KEY = "is_logged_in";
    public final static String SAVED_EMAIL_KEY = "email";

    private Preferences preferences;
    public PreferenceManager(Application application){
        preferences = new Preferences(application, PREFERENCE_NAME);
    }

    public void saveLogInState(){
        preferences.saveBooleanValue(LOGGED_IN_STATE_KEY, true);
    }
    public void deleteLogInState(){
        preferences.saveBooleanValue(LOGGED_IN_STATE_KEY, false);
    }

    public void setEmail(String email){
        preferences.saveStringValue(SAVED_EMAIL_KEY, email);
    }

    public void clearEmail(){
        preferences.saveStringValue(SAVED_EMAIL_KEY, "");
    }

    public boolean getLogInState(){
        return preferences.getBooleanValue(LOGGED_IN_STATE_KEY);
    }

    public String getEmail(){
        return preferences.getStringValue(SAVED_EMAIL_KEY);
    }
}
