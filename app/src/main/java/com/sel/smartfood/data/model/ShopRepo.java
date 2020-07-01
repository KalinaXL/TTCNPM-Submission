package com.sel.smartfood.data.model;

import android.widget.Toast;

import com.sel.smartfood.data.remote.firebase.FirebaseProducts;
import com.sel.smartfood.data.remote.firebase.FirebaseService;
import com.sel.smartfood.data.remote.firebase.FirebaseServiceBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ShopRepo {
    private FirebaseService firebaseService;
    public ShopRepo(){
        firebaseService = new FirebaseServiceBuilder().addProducts(new FirebaseProducts()).build();
    }

    public Single<List<Category>> getCategoryList(){
        return firebaseService.getCategories();
    }

//    private int findCategoryId(int position){
//        if (categoryList == null)
//            return -1;
//        return categoryList.get(position).getId();
//    }

    public Single<List<Product>> getProductList(){
        return firebaseService.getProducts();
    }

//    public Single<List<Product>> getProductList(int position){
//        int categoryId = findCategoryId(position);
//        List<Product> products = new ArrayList<>();
//        for (Product product: productList){
//            if (product.getCategoryId() == categoryId){
//                products.add(product);
//            }
//        }
//        return Single.just(products);
//    }

    public Single<List<Product>> fetchNewProducts(int position){
        List<Product> productList = new ArrayList<>();
//        int categoryId = findCategoryId(position);
        // find the products which have the same category_id with category_id of current page
        // data sample
        productList.add(new Product(6, 4, "Com ca", 3000, 10, 3, null));
        productList.add(new Product(7, 2, "Sinh to", 6000, 8, 4, null));
        productList.add(new Product(8, 4, "Cam vat", 10000, 7, 5, null));
        return Single.just(productList);
    }

//    public Single<List<Product>> searchProducts(int position, String name){
//        int categoryId = findCategoryId(position);
//        List<Product> products = new ArrayList<>();
//        for (Product product: productList){
//            if (product.getCategoryId() == categoryId && product.getName().toLowerCase().contains(name.toLowerCase())){
//                products.add(product);
//            }
//        }
//        return Single.just(products);
//    }
}
