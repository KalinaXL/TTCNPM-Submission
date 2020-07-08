package com.sel.smartfood.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sel.smartfood.data.local.Preferences;
import com.sel.smartfood.data.model.User;
import com.sel.smartfood.data.remote.firebase.FirebaseInfo;
import com.sel.smartfood.data.remote.firebase.FirebaseService;
import com.sel.smartfood.data.remote.firebase.FirebaseServiceBuilder;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InfoViewModel extends AndroidViewModel {
    private MutableLiveData<User> user = new MutableLiveData<>();
    private FirebaseService firebaseService;
    private Preferences preferences;
    private CompositeDisposable compositeDisposable;

    public InfoViewModel(@NonNull Application application){
        super(application);
        firebaseService = new FirebaseServiceBuilder().addInfo(new FirebaseInfo()).build();
        preferences = new Preferences(application, SigninViewModel.PREFERENCE_NAME);
        compositeDisposable = new CompositeDisposable();
    }

    public void getUserInfo(){
        String email = getEmailSaved();
        Disposable d = firebaseService.getUser(email.split("@")[0])
                .subscribeOn(Schedulers.io())
                .subscribe(u -> user.postValue(u), e -> user.postValue(null));
        compositeDisposable.add(d);
    }

    public String getEmailSaved(){
        return preferences.getStringValue(SigninViewModel.SAVED_EMAIL_KEY);
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void logOut(){
        preferences.saveBooleanValue(SigninViewModel.LOGGED_IN_STATE_KEY, false);
        preferences.saveStringValue(SigninViewModel.SAVED_EMAIL_KEY, "");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
        if (!compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }
}
