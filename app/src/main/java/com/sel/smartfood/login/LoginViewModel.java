package com.sel.smartfood.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sel.smartfood.R;
import com.sel.smartfood.utils.Result;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {
    public final static int TIME_OUT_SEC = 10;
    public final static String LOGIN_ERROR_MESSAGE = "Đã có lỗi xảy ra! Vui lòng thử lại";
    public final static String WRONG_USER = "Tài khoản không tồn tại";
    public final static String WRONG_PASSWORD = "Mật khẩu không đúng";

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<Result<UserInfo>> loginResult = new MutableLiveData<>();
    private LoginService loginService = new LoginService();
    private CompositeDisposable compositeDisposable;
    public void login(String username, String password){
         Disposable d = loginService.findUserInfo(username).timeout(TIME_OUT_SEC, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).subscribe(userInfo->{
             if (userInfo == null){
                 loginResult.postValue(new Result.Error(new Exception(WRONG_USER)));
                 return;
             }
             if (password.equals("password")){
                 loginResult.postValue(new Result.Success<>(userInfo));
             }
             else{
                 loginResult.postValue(new Result.Error(new Exception(WRONG_PASSWORD)));
             }
         }, e->{
             loginResult.postValue(new Result.Error(new Exception(LOGIN_ERROR_MESSAGE)));
         });
         if (compositeDisposable == null)
             compositeDisposable = new CompositeDisposable();
         compositeDisposable.add(d);
    }

    public LiveData<Result<UserInfo>> getLoginResult() {
        return loginResult;
    }

    public void loginDataChanged(String username, String password){
        if (!isUsernameValid(username)){
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        }
        else if (!isPasswordValid(password)){
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        }
        else{
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()){
            compositeDisposable.clear();
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }

    private boolean isPasswordValid(String password){
        if (password == null)
            return false;
        password = password.trim();
        return password.length() >=6 && password.length() <= 30;
    }
    private boolean isUsernameValid(String username){
        if (username == null)
            return false;
        username = username.trim();
        return username.length() >=6 && username.length() <= 30;
    }
    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }
}
