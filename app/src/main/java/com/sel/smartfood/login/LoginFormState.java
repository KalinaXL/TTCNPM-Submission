package com.sel.smartfood.login;

import androidx.annotation.Nullable;

public class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;
    public LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError){
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }
    public LoginFormState(boolean isDataValid){
        this.usernameError = this.passwordError = null;
        this.isDataValid = true;
    }

    @Nullable
    public Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
