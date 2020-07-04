package com.sel.smartfood.data.remote.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sel.smartfood.data.model.Category;
import com.sel.smartfood.data.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.core.Single;

public class FirebaseProducts {
    private DatabaseReference productsRef;
    private DatabaseReference categoriesRef;
    public FirebaseProducts(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        productsRef = firebaseDatabase.getReference().child("Products");
        categoriesRef = firebaseDatabase.getReference().child("Categories");
    }
    // lay cac loai mon an
    public Single<List<Category>> getCategories(){
        List<Category> categories = new ArrayList<>();
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot node: snapshot.getChildren()){
                    categories.add(node.getValue(Category.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return Single.just(categories);
    }
    // lay het cac mon an
    public Single<List<Product>> getProducts(){
        List<Product> products = new ArrayList<>();
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot node : snapshot.getChildren()){
                    products.add(node.getValue(Product.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return Single.just(products);
    }
}
