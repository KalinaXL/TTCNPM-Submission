package com.sel.smartfood.login.signin;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.sel.smartfood.R;
import com.sel.smartfood.login.firebase.FirebaseAuthenticationImpl;
import com.sel.smartfood.login.firebase.FirebaseService;
import com.sel.smartfood.login.firebase.FirebaseServiceBuilder;
import com.sel.smartfood.utils.Result;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SigninViewModel extends ViewModel {
    public final static int TIME_OUT_SEC = 10;
    public final static String UNAVAILABLE_SERVICE_MESSAGE = "Dịch vụ không có sẵn. Vui lòng cài Google Play";
    public final static String LOGIN_ERROR_MESSAGE = "Đã có lỗi xảy ra! Vui lòng thử lại";
    public final static String WRONG_USER = "Tài khoản không tồn tại";
    public final static String WRONG_PASSWORD = "Mật khẩu không đúng";

    private MutableLiveData<SigninFormState> signinFormState = new MutableLiveData<>();
    private MutableLiveData<Result<Boolean>> signinResult = new MutableLiveData<>();

    private FirebaseService firebaseService = new FirebaseServiceBuilder()
                                                    .addAuth(new FirebaseAuthenticationImpl())
                                                    .build();
    private CompositeDisposable compositeDisposable;
    public void login(String username, String password){
        Disposable d = firebaseService.login(username, password)
                .timeout(TIME_OUT_SEC, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(()-> signinResult.postValue(new Result.Success<>(true)), this::handleLoginWithEmailError);
        if (compositeDisposable == null){
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(d);
    }
    private void handleLoginWithEmailError(Throwable e){
        if (e instanceof FirebaseApiNotAvailableException){
            signinResult.postValue(new Result.Error(new Exception(UNAVAILABLE_SERVICE_MESSAGE)));
        }
        else if (e instanceof FirebaseAuthInvalidUserException){
            signinResult.postValue(new Result.Error(new Exception(WRONG_USER)));
        }
        else if (e instanceof FirebaseAuthInvalidCredentialsException){
            signinResult.postValue(new Result.Error(new Exception(WRONG_PASSWORD)));
        }
        else{
            signinResult.postValue(new Result.Error(new Exception(LOGIN_ERROR_MESSAGE)));
        }
    }

    public void loginDataChanged(String username, String password){
        if (!isUsernameValid(username)){
            signinFormState.setValue(new SigninFormState(R.string.invalid_username, null));
        }
        else if (!isPasswordValid(password)){
            signinFormState.setValue(new SigninFormState(null, R.string.invalid_password));
        }
        else{
            signinFormState.setValue(new SigninFormState(true));
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

    public LiveData<Result<Boolean>> getSigninResult() {
        return signinResult;
    }
    public LiveData<SigninFormState> getSigninFormState() {
        return signinFormState;
    }

    private boolean isPasswordValid(String password){
        return password != null && password.length() >= 6 && password.length() <= 30;
    }
    private boolean isUsernameValid(String username){
        return username != null && Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

}
