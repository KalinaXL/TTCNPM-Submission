package com.sel.smartfood.ui.shop;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ReportFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavAction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.sel.smartfood.R;
import com.sel.smartfood.data.model.Category;
import com.sel.smartfood.data.model.Product;
import com.sel.smartfood.viewmodel.ShopCartModel;
import com.sel.smartfood.viewmodel.ShopViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private boolean isLoading;
    private boolean hasProducts;
    private boolean isDialogShowed;
    public static ArrayList<ShopCartModel> orderProductList;

    private LoadingDialogFragment dialog;
    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dialog = new LoadingDialogFragment();

        return inflater.inflate(R.layout.fragment_shop, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findWidgets(view);

        shopViewModel = new ViewModelProvider(getActivity()).get(ShopViewModel.class);
        if (shopViewModel.getHasProductsLoaded().getValue() == null || !shopViewModel.getHasProductsLoaded().getValue()){
            dialog.show(getChildFragmentManager(), "LOADING");
            isDialogShowed = true;
        }
        else{
            isDialogShowed = false;
        }

        shopViewModel.getHasProductsLoaded().observe(getViewLifecycleOwner(), hasProductsLoaded -> {
            if (hasProductsLoaded != null && isDialogShowed && hasProductsLoaded) {
                Handler handler = new Handler();
                handler.postDelayed(() -> dialog.dismiss(), 000);
            }
        });

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

//        shopViewModel.getCategories();
//        shopViewModel.getProducts();

        shopViewModel.getCategoryList().observe(getViewLifecycleOwner(), this::updateCategoriesUI);
        shopViewModel.getProductList().observe(getViewLifecycleOwner(), this::updateProductsUI);

        // this is array of products. If customer need to buy another food
        if(orderProductList != null){

        }else
        {
            orderProductList = new ArrayList<>();
        }

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
            page.setScaleY(.8f + (1- Math.abs(position)) * .2f);
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
        productsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)productsRv.getLayoutManager();
                if (!isLoading && hasProducts && linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == productAdapter.getItemCount() - 1){
                    productAdapter.setLoadingState();
                    isLoading = true;
                    shopViewModel.fetchMoreProducts(currentPagePosition);
                }
            }
        });

        productsRv.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(), productsRv, new RecyclerItemClickListener.OnItemClickListener(){

                    @Override
                    public void onItemClick(View view, int position) {

                        List<Product> productList = productAdapter.getProductList();
                        final NavController navController = Navigation.findNavController(view);
                        ShopFragmentDirections.ActionNavShopToProductDetailFragment action = ShopFragmentDirections.actionNavShopToProductDetailFragment();
                        // transfer data to ProductDetailFragment
                        action.setProductId(productList.get(position).getId());
                        action.setProductName(productList.get(position).getName());
                        action.setProductPrice(productList.get(position).getPrice());
                        action.setProductImage(productList.get(position).getUrl());
                        navController.navigate(action);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
    }

    private void updateCategoriesUI(List<Category> categories) {
        categoryAdapter.setDataChanged(categories);
    }

    private void updateProductsUI(List<Product> products) {
        if (products == null){
            productAdapter.setDataChanged(null);
            noproductTv.setVisibility(View.VISIBLE);
            noproductTv.setText(R.string.search_error);
            productsRv.setVisibility(View.INVISIBLE);
            hasProducts = false;
        }
        else if (products.size() == 0){
            productAdapter.setDataChanged(products);
            noproductTv.setVisibility(View.VISIBLE);
            noproductTv.setText(R.string.search_no_products);
            productsRv.setVisibility(View.INVISIBLE);
            hasProducts = false;
        }
        else{
            if (isLoading && productAdapter.getItemCount() > 4){
                if (hasProducts)
                    productAdapter.addNewData(products);
                isLoading = false;
            }
            else{
                hasProducts = true;
                productAdapter.setDataChanged(products);
            }
            productAdapter.setDataChanged(products);
            noproductTv.setVisibility(View.GONE);
            productsRv.setVisibility(View.VISIBLE);
        }
    }
}
