package com.sel.smartfood.ui.shop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private int currentPagePosition;


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
        findWidgets(view);
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);

        setViewPager2();
        setRecyclerView();

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                shopViewModel.searchProducts(currentPagePosition, s.toString());
            }
        });

        shopViewModel.getCategories();
//        shopViewModel.getProducts();

        shopViewModel.getCategoryList().observe(getViewLifecycleOwner(), this::updateCategoriesUI);
        shopViewModel.getProductList().observe(getViewLifecycleOwner(), this::updateProductsUI);
    }
    private void findWidgets(View view){
        searchEt = view.findViewById(R.id.tv_search_product);
        productsRv = view.findViewById(R.id.rv_product_list);
        viewPager2 = view.findViewById(R.id.vg2_product);
        noproductTv = view.findViewById(R.id.tv_no_products);
    }
    private void setViewPager2(){
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setPadding(180, 0, 180, 0);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            page.setScaleY(.84f + (1- Math.abs(position)) * .16f);
            page.setAlpha(.5f + (1- Math.abs(position) * .5f));
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPagePosition = position;
                shopViewModel.getProducts(position);
            }
        });
        categoryAdapter = new CategoryAdapter();
        viewPager2.setAdapter(categoryAdapter);
    }
    private void setRecyclerView(){
        productsRv.setLayoutManager(new GridLayoutManager(requireActivity(), 1, GridLayoutManager.VERTICAL, false));
        productsRv.setNestedScrollingEnabled(true);
        productAdapter = new ProductAdapter();
        productsRv.setAdapter(productAdapter);
    }

    private void updateCategoriesUI(List<Category> categories) {
        categoryAdapter.setDataChanged(categories);
    }

    private void updateProductsUI(List<Product> products) {
        productAdapter.setDataChanged(products);
        if (products == null){
            noproductTv.setVisibility(View.VISIBLE);
            noproductTv.setText(R.string.search_error);
            productsRv.setVisibility(View.INVISIBLE);
        }
        else if (products.size() == 0){
            noproductTv.setVisibility(View.VISIBLE);
            noproductTv.setText(R.string.search_no_products);
            productsRv.setVisibility(View.INVISIBLE);
        }
        else{
            noproductTv.setVisibility(View.GONE);
            productsRv.setVisibility(View.VISIBLE);
        }
    }
}
