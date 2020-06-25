package com.sel.smartfood.data.model;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class ShopRepo {
    private List<Product> productList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();

    public ShopRepo(){
        categoryList.add(new Category(0, "Đồ ăn nhanh"));
        categoryList.add(new Category(1,"Nước uống"));
        categoryList.add(new Category(2,"Trái cây"));
        categoryList.add(new Category(3,"Kem"));

        productList.add(new Product(1, 0,"Mi xao", 10000, 10,4));
        productList.add(new Product(2,1, "Xi xi", 1000, 15, 3));
        productList.add(new Product(3,2,"Co ca", 9000, 7, 5));
        productList.add(new Product(4, 2, "Com ga", 4000, 8,1));
        productList.add(new Product(5, 3, "Com suon", 2000, 12,2));
    }

    public Single<List<Category>> getCategoryList(){
        return Single.just(categoryList);
    }

    private int findCategoryId(int position){
        return categoryList.get(position).getId();
    }

    public Single<List<Product>> getProductList(){
        return Single.just(productList);
    }

    public Single<List<Product>> getProductList(int position){
        int categoryId = findCategoryId(position);
        List<Product> products = new ArrayList<>();
        for (Product product: productList){
            if (product.getCategoryId() == categoryId){
                products.add(product);
            }
        }
        return Single.just(products);
    }

    public Single<List<Product>> searchProducts(int position, String name){
        int categoryId = findCategoryId(position);
        List<Product> products = new ArrayList<>();
        for (Product product: productList){
            if (product.getCategoryId() == categoryId && product.getName().toLowerCase().contains(name.toLowerCase())){
                products.add(product);
            }
        }
        return Single.just(products);
    }
}
