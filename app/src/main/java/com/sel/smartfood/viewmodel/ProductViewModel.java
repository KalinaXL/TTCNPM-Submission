package com.sel.smartfood.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sel.smartfood.data.model.Product;
import com.sel.smartfood.data.model.ProductRepo;
import com.sel.smartfood.data.model.Result;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductViewModel extends ViewModel {
    private CompositeDisposable compositeDisposable;
    private ProductRepo productRepo;
    private MutableLiveData<List<Product>> productList = new MutableLiveData<>();

    public ProductViewModel(){
        productRepo = new ProductRepo();
    }

    public void getProducts() {
        Disposable d =  productRepo.getProductList()
                .subscribeOn(Schedulers.io())
                .subscribe(ls -> productList.postValue(ls), e -> productList.postValue(null));
        if (compositeDisposable == null){
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(d);
    }

    public LiveData<List<Product>> getProductList() {
        return productList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null && compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
    }
}
