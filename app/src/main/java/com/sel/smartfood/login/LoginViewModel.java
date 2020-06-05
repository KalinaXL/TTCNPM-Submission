package com.sel.smartfood.login;

import android.provider.ContactsContract;
import android.util.Patterns;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.sel.smartfood.R;
import com.sel.smartfood.utils.Result;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {
    public final static int TIME_OUT_SEC = 10;
    public final static String UNAVAILABLE_SERVICE_MESSAGE = "Dịch vụ không có sẵn. Vui lòng cài Google Play";
    public final static String LOGIN_ERROR_MESSAGE = "Đã có lỗi xảy ra! Vui lòng thử lại";
    public final static String WRONG_USER = "Tài khoản không tồn tại";
    public final static String WRONG_PASSWORD = "Mật khẩu không đúng";

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<Result<Boolean>> loginResult = new MutableLiveData<>();
    private LoginService loginService = new LoginService();
    private FirebaseService firebaseService = FirebaseService.getInstance();
    private CompositeDisposable compositeDisposable;
    public void login(String username, String password){
        Disposable d = firebaseService.loginWithEmail(username, password)
                .timeout(TIME_OUT_SEC, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(()-> loginResult.postValue(new Result.Success<>(true)), this::handleLoginWithEmailError);
        if (compositeDisposable == null){
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(d);
    }
    private void handleLoginWithEmailError(Throwable e){
        if (e instanceof FirebaseApiNotAvailableException){
            loginResult.postValue(new Result.Error(new Exception(UNAVAILABLE_SERVICE_MESSAGE)));
        }
        else if (e instanceof FirebaseAuthInvalidUserException){
            loginResult.postValue(new Result.Error(new Exception(WRONG_USER)));
        }
        else if (e instanceof FirebaseAuthInvalidCredentialsException){
            loginResult.postValue(new Result.Error(new Exception(WRONG_PASSWORD)));
        }
        else{
            loginResult.postValue(new Result.Error(new Exception(LOGIN_ERROR_MESSAGE)));
        }
    }
    public LiveData<Result<Boolean>> getLoginResult() {
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
        return password.length() >= 6 && password.length() <= 30;
    }
    private boolean isUsernameValid(String username){
        if (username == null)
            return false;
        username = username.trim();
        return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }
    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }
}
