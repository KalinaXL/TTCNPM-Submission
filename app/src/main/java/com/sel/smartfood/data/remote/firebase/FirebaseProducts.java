package com.sel.smartfood.data.remote.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sel.smartfood.data.model.Category;
import com.sel.smartfood.data.model.Product;
import com.sel.smartfood.ui.shop.ICategoryCallbackListener;
import com.sel.smartfood.ui.shop.IProductCallbackListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseProducts {
    private DatabaseReference productsRef;
    private DatabaseReference categoriesRef;
    private ICategoryCallbackListener categoryCallbackListener;
    private IProductCallbackListener productCallbackListener;


    public FirebaseProducts(ICategoryCallbackListener categoryCallbackListener, IProductCallbackListener productCallbackListener){
        this.categoryCallbackListener = categoryCallbackListener;
        this.productCallbackListener = productCallbackListener;

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        firebaseDatabase.setPersistenceEnabled(true);
        productsRef = firebaseDatabase.getReference().child("Products");
        categoriesRef = firebaseDatabase.getReference().child("Categories");
    }
    // lay cac loai mon an
    public void getCategories(){
        List<Category> categories = new ArrayList<>();
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot node: snapshot.getChildren()){
                    categories.add(node.getValue(Category.class));
                }
                categoryCallbackListener.OnCategoryLoadSuccess(categories);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                categoryCallbackListener.OnCategoryLoadSuccess(categories);
            }
        });
    }
    // lay het cac mon an
    public void getProducts() {
        List<Product> products = new ArrayList<>();
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot node : snapshot.getChildren()) {
                    products.add(node.getValue(Product.class));
                }
                productCallbackListener.OnProductLoadSuccess(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                productCallbackListener.OnProductLoadSuccess(products);
            }
        });
    }
}
