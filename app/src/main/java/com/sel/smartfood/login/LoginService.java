package com.sel.smartfood.login;

import io.reactivex.rxjava3.core.Single;

public class LoginService {
    Single<UserInfo> findUserInfo(String username){
        return Single.fromCallable(()->{
            if (username.equals("username"))
                return new UserInfo();
            return null;
        });
    }
}
