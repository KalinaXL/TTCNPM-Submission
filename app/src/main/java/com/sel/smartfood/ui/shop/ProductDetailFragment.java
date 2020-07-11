package com.sel.smartfood.ui.shop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.sel.smartfood.R;
import com.sel.smartfood.data.model.ShopCartModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 *
 */
public class ProductDetailFragment extends Fragment {

    // TODO: Rename and change types of parameters
    Toolbar toolbarProductDetails;
    ImageView ivProductDetails;
    TextView tvName, tvPrice, tvDescription;
    Spinner spinner;
    Button btn_order;
    View view;

    int productId;
    String productName;
    int productPrice;
    float productPreparationTime;
    int productRatingscore;
    String productImg;
    String productDescription;
    final int MAX_PRODUCT_NUMBER = 10;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        Maps(view);
        GetInformation();
        CatchEventSpinner();
        EventButton();
        return view;
    }


    private void EventButton() {

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShopFragment.orderProductList.size() > 0){
                    int new_order_numbers = Integer.parseInt(spinner.getSelectedItem().toString());
                    boolean exists = false;

                    for (int i = 0; i < ShopFragment.orderProductList.size();i++){
                        if (ShopFragment.orderProductList.get(i).getProduct_id() == productId){
                            // update order number
                            ShopFragment.orderProductList.get(i)
                                    .setProduct_numbers(ShopFragment.orderProductList.get(i).getProduct_numbers() + new_order_numbers);

                            if(ShopFragment.orderProductList.get(i).getProduct_numbers() > MAX_PRODUCT_NUMBER){
                                ShopFragment.orderProductList.get(i).setProduct_numbers(MAX_PRODUCT_NUMBER);
                            }

                            ShopFragment.orderProductList.get(i)
                                    .setProduct_price(productPrice * ShopFragment.orderProductList.get(i).getProduct_numbers());
                            exists = true;
                        }
                    }
                    if (exists == false){
                        // add data
                        int order_product_numbers = Integer.parseInt(spinner.getSelectedItem().toString());
                        int total_price = order_product_numbers * productPrice;
                        ShopFragment.orderProductList.add(new ShopCartModel(productId, productName, total_price, productName, order_product_numbers));
                    }

                }else{

                    // add data
                    int order_product_numbers = Integer.parseInt(spinner.getSelectedItem().toString());
                    int total_price = order_product_numbers * productPrice;
                    ShopFragment.orderProductList.add(new ShopCartModel(productId, productName, total_price, productImg, order_product_numbers));
                }


                Navigation.findNavController(view).navigate(R.id.nav_shopping_cart);
            }

        });

    }


    private void GetInformation() {
        if (getArguments() != null) {

            ProductDetailFragmentArgs args = ProductDetailFragmentArgs.fromBundle(getArguments());
            productId = args.getProductId();
            productName = args.getProductName();
            productPrice = args.getProductPrice();
            productImg = args.getProductImage();
            productPreparationTime = args.getProductPreTime();
            productRatingscore = args.getProductRatingScore();
            productDescription = args.getProductDescription();


            tvName.setText(productName);

            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            tvPrice.setText("Giá : " + decimalFormat.format(productPrice) + " Đồng ");

            // hình ảnh sản phẩm
            String imgUrl = args.getProductImage();
            Picasso.get().load(productImg)
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.error)
                    .into(ivProductDetails);

        }


    }

    private void CatchEventSpinner() {
        Integer[] number = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this.requireActivity(), android.R.layout.simple_spinner_dropdown_item, number );
        spinner.setAdapter(arrayAdapter);
    }

    private void Maps(View view) {
        toolbarProductDetails = (Toolbar)view.findViewById(R.id.toolbar_product_details);
        ivProductDetails = (ImageView) view.findViewById(R.id.iv_product_details);
        tvName = (TextView) view.findViewById(R.id.tv_name_product_details);
        tvPrice = (TextView) view.findViewById(R.id.tv_price_product_details);
        tvDescription = (TextView) view.findViewById(R.id.tv_description_product_details);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        btn_order = (Button) view.findViewById(R.id.btn_order);
    }
}