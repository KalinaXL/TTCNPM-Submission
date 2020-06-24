package com.sel.smartfood.data.model;

import com.sel.smartfood.data.model.Product;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class ProductRepo {
    private List<Product> productList = new ArrayList<>();

    public Single<List<Product>> getProductList(){
        productList.add(new Product(1,"Mi xao", 10000));
        productList.add(new Product(2,"Xi xi", 1000));
        productList.add(new Product(3,"Co ca", 9000));
        return Single.just(productList);
    }
}
