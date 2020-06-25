package com.sel.smartfood.data.model;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class ShopRepo {
    private List<Product> productList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();

    public Single<List<Category>> getCategoryList(){
        categoryList.add(new Category("Đồ ăn nhanh"));
        categoryList.add(new Category("Nước uống"));
        categoryList.add(new Category("Trái cây"));
        categoryList.add(new Category("Kem"));
        return Single.just(categoryList);
    }

    public Single<List<Product>> getProductList(){
        productList.add(new Product(1,"Mi xao", 10000));
        productList.add(new Product(2,"Xi xi", 1000));
        productList.add(new Product(3,"Co ca", 9000));
        return Single.just(productList);
    }
}
