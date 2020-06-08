package com.sel.smartfood.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.sel.smartfood.R;
import com.sel.smartfood.data.model.RegisterFormState;
import com.sel.smartfood.data.model.SigninFormState;
import com.sel.smartfood.data.remote.firebase.FirebaseAuthenticationImpl;
import com.sel.smartfood.data.remote.firebase.FirebaseRegistrationImpl;
import com.sel.smartfood.data.remote.firebase.FirebaseService;
import com.sel.smartfood.data.remote.firebase.FirebaseServiceBuilder;
import com.sel.smartfood.data.model.Result;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterFragment extends Fragment {
    private EditText fullnameEt;
    private EditText emailEt;
    private EditText passwordEt;
    private EditText repasswordEt;
    private EditText phoneNumberEt;
    private TextView signinhintTv;
    private ProgressBar loadingPb;
    private RegisterViewModel registerViewModel;
    private Button registerBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.layout_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findWidgets(view);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        registerViewModel.getRegisterFormState().observe(getViewLifecycleOwner(), this::handleRegisterFormState);
        registerViewModel.getRegisterResult().observe(getViewLifecycleOwner(), this::handleRegisterResult);

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.registerDataChanged(fullnameEt.getText().toString(),
                                                      emailEt.getText().toString(),
                                                      phoneNumberEt.getText().toString(),
                                                      passwordEt.getText().toString(),
                                                      repasswordEt.getText().toString());
            }
        };
        fullnameEt.addTextChangedListener(afterTextChangedListener);
        emailEt.addTextChangedListener(afterTextChangedListener);
        phoneNumberEt.addTextChangedListener(afterTextChangedListener);
        passwordEt.addTextChangedListener(afterTextChangedListener);
        repasswordEt.addTextChangedListener(afterTextChangedListener);

        registerBtn.setOnClickListener(v -> {
            registerViewModel.register(emailEt.getText().toString(), passwordEt.getText().toString());
            registerBtn.setEnabled(false);
            loadingPb.setVisibility(View.VISIBLE);
        });
        signinhintTv.setOnClickListener(v -> {
            getParentFragmentManager()
            .beginTransaction()
            .replace(R.id.fcv_login_activity, new SigninFragment())
            .addToBackStack(null)
            .commit();
        });
    }

    private void handleRegisterResult(Result<Boolean> booleanResult) {
        if (booleanResult == null)
            return;
        if (booleanResult instanceof Result.Success){
            Toast.makeText(getActivity(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            loadingPb.setVisibility(View.INVISIBLE);
            getParentFragmentManager()
            .beginTransaction()
            .setCustomAnimations(R.anim.fragment_close_enter, R.anim.fragment_close_exit)
            .replace(R.id.fcv_login_activity, new SigninFragment())
            .addToBackStack(null)
            .commit();
        }
        else{
            Toast.makeText(getActivity(), ((Result.Error)booleanResult).getError().getMessage(), Toast.LENGTH_SHORT).show();
            loadingPb.setVisibility(View.INVISIBLE);
        }
    }

    private void handleRegisterFormState(RegisterFormState registerFormState){
        if (registerFormState == null)
            return;

        registerBtn.setEnabled(registerFormState.isDataValid());
        if (registerFormState.getFullnameError() != null){
            fullnameEt.setError(getString(registerFormState.getFullnameError()));
        }
        if (registerFormState.getEmailError() != null){
            emailEt.setError(getString(registerFormState.getEmailError()));
        }
        if (registerFormState.getPhoneNumberError() != null){
            phoneNumberEt.setError(getString(registerFormState.getPhoneNumberError()));
        }
        if (registerFormState.getPasswordError() != null){
            passwordEt.setError(getString(registerFormState.getPasswordError()));
        }
        if (registerFormState.getRepasswordError() != null){
            repasswordEt.setError(getString(registerFormState.getRepasswordError()));
        }
    }

    void findWidgets(View v){
        this.fullnameEt = v.findViewById(R.id.et_new_fullname);
        this.emailEt = v.findViewById(R.id.et_new_email);
        this.phoneNumberEt = v.findViewById(R.id.et_new_phone_number);
        this.passwordEt = v.findViewById(R.id.et_new_password);
        this.repasswordEt = v.findViewById(R.id.et_renew_password);
        this.signinhintTv = v.findViewById(R.id.tv_sign_in_hint);
        this.registerBtn = v.findViewById(R.id.btn_register);
        this.loadingPb  = v.findViewById(R.id.pb_loading_register);
    }

    public static class RegisterViewModel extends ViewModel {
        public final static int TIME_OUT_SEC = 10;
        public final static String INVALID_EMAIL = "Email đã tồn tại! Thử email khác.";
        public final static String ERROR_MESSAGE = "Đăng ký thất bại! Vui lòng thử lại sau.";

        private MutableLiveData<Result<Boolean>> registerResult = new MutableLiveData<>();
        private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
        private FirebaseService firebaseService = new FirebaseServiceBuilder()
                                                    .addRegistration(new FirebaseRegistrationImpl())
                                                    .build();
        private CompositeDisposable compositeDisposable;

        public void register(String email, String password){
            Disposable d = firebaseService.register(email, password)
                                          .timeout(TIME_OUT_SEC, TimeUnit.SECONDS)
                                          .subscribeOn(Schedulers.io())
                                          .subscribe(() -> registerResult.postValue(new Result.Success<>(true)), this::handleRegisterError);
            if (compositeDisposable == null)
                compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(d);
        }

        private void handleRegisterError(Throwable e) {
            if (e instanceof FirebaseAuthUserCollisionException){
                registerResult.postValue(new Result.Error(new Exception(INVALID_EMAIL)));
            }
            else{
                registerResult.postValue(new Result.Error(new Exception(ERROR_MESSAGE)));
            }
        }

        public void registerDataChanged(String fullname, String email, String phoneNumber, String password, String repassword){
            Integer name, mail, phone, pass, repass;
            name = mail = phone = pass = repass = null;
            boolean inValid = false;

            if (!isFullnameValid(fullname)){
                inValid = true;
                name = R.string.invalid_new_fullname;
            }
            if (!isEmailValid(email)){
                inValid = true;
                mail = R.string.invalid_new_email;
            }
            if (!isPhoneNumberValid(phoneNumber)){
                inValid = true;
                phone = R.string.invalid_new_phone_number;
            }
            if (!isPasswordValid(password)){
                inValid = true;
                pass = R.string.invalid_new_password;
            }
            if (repassword == null){
                inValid = true;
                repass = R.string.invalid_renew_password;
            }
            else if (!repassword.equals(password)){
                inValid = true;
                repass = R.string.invalid_renew_password;
            }
            if (inValid){
                registerFormState.setValue(new RegisterFormState(name, mail, phone, pass, repass));
            }
            else{
                registerFormState.setValue(new RegisterFormState(true));
            }
        }

        @Override
        protected void onCleared() {
            super.onCleared();
            if (compositeDisposable != null && compositeDisposable.isDisposed()){
                compositeDisposable.dispose();
                compositeDisposable.clear();
                compositeDisposable = null;
            }
        }

        public LiveData<RegisterFormState> getRegisterFormState() {
            return registerFormState;
        }

        public LiveData<Result<Boolean>> getRegisterResult() {
            return registerResult;
        }

        private boolean isFullnameValid(String fullname){
            return fullname != null && fullname.length() > 2;
        }
        private boolean isEmailValid(String email){
            return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        private boolean isPhoneNumberValid(String phoneNumber){
            return phoneNumber != null && Patterns.PHONE.matcher(phoneNumber).matches();
        }
        private boolean isPasswordValid(String password){
            return password != null && password.length() > 5 && password.length() < 30;
        }
    }

    public static class SigninViewModel extends ViewModel {
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
}
