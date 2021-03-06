package com.sel.smartfood.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sel.smartfood.data.model.Category;
import com.sel.smartfood.data.model.Product;
import com.sel.smartfood.data.model.ShopRepo;
import com.sel.smartfood.ui.shop.ICategoryCallbackListener;
import com.sel.smartfood.ui.shop.IProductCallbackListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ShopViewModel extends ViewModel implements ICategoryCallbackListener, IProductCallbackListener {
    private CompositeDisposable compositeDisposable;
    private ShopRepo shopRepo;
    private List<Product> allProducts;
    private MutableLiveData<List<Product>> productList = new MutableLiveData<>();
    private MutableLiveData<List<Category>> categoryList = new MutableLiveData<>();
    private MutableLiveData<Boolean> hasProductsLoaded = new MutableLiveData<>();

    public ShopViewModel(){
        shopRepo = new ShopRepo(this, this);
        compositeDisposable = new CompositeDisposable();
    }

    public void getCategories(){
        shopRepo.getCategoryList();
    }
    private int findCategoryId(int position){
        return categoryList.getValue().get(position).getId();
    }
    public void getProducts() {
        shopRepo.getProductList();
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

    public MutableLiveData<Boolean> getHasProductsLoaded() {
        return hasProductsLoaded;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null && compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
    }

    @Override
    public void OnCategoryLoadSuccess(List<Category> categories) {
        categoryList.setValue(categories);
        hasProductsLoaded.setValue(true);
    }

    @Override
    public void OnProductLoadSuccess(List<Product> products) {
        allProducts = new ArrayList<>(products);

        productList.setValue(products);
    }
}
