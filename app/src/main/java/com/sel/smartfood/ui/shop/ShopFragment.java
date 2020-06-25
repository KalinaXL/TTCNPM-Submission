package com.sel.smartfood.ui.shop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.sel.smartfood.R;
import com.sel.smartfood.data.model.Category;
import com.sel.smartfood.data.model.Product;
import com.sel.smartfood.viewmodel.ShopViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {
    private EditText searchEt;
    private TextView noproductTv;
    private ViewPager2 viewPager2;
    private RecyclerView productsRv;
    private ShopViewModel shopViewModel;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;


    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchEt = view.findViewById(R.id.tv_search_product);
        productsRv = view.findViewById(R.id.rv_product_list);
        viewPager2 = view.findViewById(R.id.vg2_product);
        viewPager2.setPadding(100, 0, 100, 0);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setPageTransformer(new MarginPageTransformer(20));
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);

        categoryAdapter = new CategoryAdapter();
        viewPager2.setAdapter(categoryAdapter);
        shopViewModel.getCategories();
        shopViewModel.getCategoryList().observe(getViewLifecycleOwner(), this::updateCategoriesUI);

        productsRv.setLayoutManager(new GridLayoutManager(requireActivity(), 1, GridLayoutManager.VERTICAL, false));
        productsRv.setNestedScrollingEnabled(true);
        noproductTv = view.findViewById(R.id.tv_no_products);
        productAdapter = new ProductAdapter();
        productsRv.setAdapter(productAdapter);

        shopViewModel.getProductList().observe(getViewLifecycleOwner(), this::updateProductsUI);
        shopViewModel.getProducts();
    }

    private void updateCategoriesUI(List<Category> categories) {
        categoryAdapter.setDataChanged(categories);
    }

    private void updateProductsUI(List<Product> products) {
        productAdapter.setDataChanged(products);
        if (products == null){
            noproductTv.setVisibility(View.VISIBLE);
            productsRv.setVisibility(View.INVISIBLE);
        }
        else{
            noproductTv.setVisibility(View.GONE);
            productsRv.setVisibility(View.VISIBLE);
        }
    }
}
