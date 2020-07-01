package com.sel.smartfood.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sel.smartfood.data.model.Category;
import com.sel.smartfood.data.model.Product;
import com.sel.smartfood.data.model.ShopRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ShopViewModel extends ViewModel {
    private CompositeDisposable compositeDisposable;
    private ShopRepo shopRepo;
    private List<Product> allProducts;
    private MutableLiveData<List<Product>> productList = new MutableLiveData<>();
    private MutableLiveData<List<Category>> categoryList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoaded = new MutableLiveData<>();

    public ShopViewModel(){
        shopRepo = new ShopRepo();
        compositeDisposable = new CompositeDisposable();
    }

    public void getCategories(){
        Disposable d = shopRepo.getCategoryList()
                        .subscribeOn(Schedulers.io())
                        .subscribe(ls -> categoryList.postValue(ls), e -> categoryList.postValue(null));
        compositeDisposable.add(d);
    }
    private int findCategoryId(int position){
        return categoryList.getValue().get(position).getId();
    }
    public void getProducts() {
        Disposable d =  shopRepo.getProductList()
                .subscribeOn(Schedulers.io())
                .subscribe(ls -> {
                    productList.postValue(ls);
                    this.allProducts = ls;
                    isLoaded.postValue(true);
                }, e -> productList.postValue(null));
        compositeDisposable.add(d);
    }
    public void getProducts(int position){
//        Disposable d = shopRepo.getProductList(position)
//                        .subscribeOn(Schedulers.io())
//                        .subscribe(ls-> productList.postValue(ls), e -> productList.postValue(null));
//        compositeDisposable.add(d);
        int categoryId = findCategoryId(position);
        List<Product> products = new ArrayList<>();
        for (Product product: allProducts){
            if (product.getCategoryId() == categoryId){
                products.add(product);
            }
        }
        productList.setValue(products);
    }

//    public void searchProducts(int position, String name){
//        Disposable d = shopRepo.searchProducts(position, name)
//                                .subscribeOn(Schedulers.single())
//                                .subscribe(ls -> productList.postValue(ls), e -> productList.postValue(null));
//        compositeDisposable.add(d);
//    }
    public void searchProducts(int position, String name){
        int categoryId = findCategoryId(position);
        List<Product> products = new ArrayList<>();
        for (Product product: allProducts){
            if (product.getCategoryId() == categoryId && product.getName().toLowerCase().contains(name.toLowerCase())){
                products.add(product);
            }
        }
        productList.setValue(products);
    }
    public void fetchMoreProducts(int position){
//        Disposable d = shopRepo.fetchNewProducts(position)
//                                .delay(4, TimeUnit.SECONDS)
//                                .subscribeOn(Schedulers.single())
//                                .subscribe(ls -> productList.postValue(ls), e -> productList.postValue(null));
//        compositeDisposable.add(d);
    }

    public LiveData<List<Category>> getCategoryList(){
        return categoryList;
    }
    public LiveData<List<Product>> getProductList() {
        return productList;
    }

    public LiveData<Boolean> getIsLoaded() {
        return isLoaded;
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
