package com.sel.smartfood.ui.shop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.sel.smartfood.R;
import com.sel.smartfood.data.model.Product;
import com.sel.smartfood.viewmodel.ProductViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {
    private EditText searchEt;
    private TextView noproductTv;
    private RecyclerView productsRv;
    private ProductViewModel productViewModel;
    private RvProductAdapter productAdapter;

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
        searchEt = view.findViewById(R.id.et_search_product);
        productsRv = view.findViewById(R.id.rv_product_list);
        productsRv.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        noproductTv = view.findViewById(R.id.tv_no_products);
        productAdapter = new RvProductAdapter();
        productsRv.setAdapter(productAdapter);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getProductList().observe(getViewLifecycleOwner(), this::updateProductsUI);
        productViewModel.getProducts();
    }

    private void updateProductsUI(List<Product> products) {
        if (products == null){
            noproductTv.setVisibility(View.VISIBLE);
            productsRv.setVisibility(View.INVISIBLE);
        }
        else{
            productAdapter.setDataChanged(products);
            noproductTv.setVisibility(View.GONE);
            productsRv.setVisibility(View.VISIBLE);
        }
    }
}
