package com.sel.smartfood.ui.shop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.sel.smartfood.R;
import com.sel.smartfood.ui.main.MainActivity;
import com.sel.smartfood.viewmodel.ShopCartModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 *
 */
public class ProductDetailFragment extends Fragment {

    // TODO: Rename and change types of parameters
    Toolbar toolbar_product_details;
    ImageView imageView_product_details;
    TextView txt_name, txt_price, txt_description;
    Spinner spinner;
    Button btn_order;
    View view;

    int product_id;
    String product_name;
    int product_price;
    float product_preparation_time;
    int product_ratingscore;
    String product_img;
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
                        if (ShopFragment.orderProductList.get(i).getProduct_id() == product_id){
                            // update order number
                            ShopFragment.orderProductList.get(i)
                                    .setProduct_numbers(ShopFragment.orderProductList.get(i).getProduct_numbers() + new_order_numbers);

                            if(ShopFragment.orderProductList.get(i).getProduct_numbers() > MAX_PRODUCT_NUMBER){
                                ShopFragment.orderProductList.get(i).setProduct_numbers(MAX_PRODUCT_NUMBER);
                            }

                            ShopFragment.orderProductList.get(i)
                                    .setProduct_price(product_price * ShopFragment.orderProductList.get(i).getProduct_numbers());
                            exists = true;
                        }
                    }
                    if (exists == false){
                        // add data
                        int order_product_numbers = Integer.parseInt(spinner.getSelectedItem().toString());
                        int total_price = order_product_numbers * product_price;
                        ShopFragment.orderProductList.add(new ShopCartModel(product_id, product_name, total_price, product_name, order_product_numbers));
                    }

                }else{

                    // add data
                    int order_product_numbers = Integer.parseInt(spinner.getSelectedItem().toString());
                    int total_price = order_product_numbers * product_price;
                    ShopFragment.orderProductList.add(new ShopCartModel(product_id, product_name, total_price, product_img, order_product_numbers));
                }


                Navigation.findNavController(view).navigate(R.id.nav_shopping_cart);
            }

        });

    }


    private void GetInformation() {
        if (getArguments() != null) {

            ProductDetailFragmentArgs args = ProductDetailFragmentArgs.fromBundle(getArguments());
            product_id = args.getProductId();
            product_name = args.getProductName();
            product_price = args.getProductPrice();
            product_img = args.getProductImage();
            product_preparation_time = args.getProductPreTime();
            product_ratingscore = args.getProductRatingScore();


            txt_name.setText(product_name);

            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            txt_price.setText("Giá : " + decimalFormat.format(product_price) + " Đồng ");

            // hình ảnh sản phẩm
            String imgUrl = args.getProductImage();
            Picasso.get().load(product_img)
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.error)
                    .into(imageView_product_details);

        }


    }

    private void CatchEventSpinner() {
        Integer[] number = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this.requireActivity(), android.R.layout.simple_spinner_dropdown_item, number );
        spinner.setAdapter(arrayAdapter);
    }

    private void Maps(View view) {
        toolbar_product_details = (Toolbar)view.findViewById(R.id.toolbar_product_details);
        imageView_product_details = (ImageView) view.findViewById(R.id.iv_product_details);
        txt_name = (TextView) view.findViewById(R.id.tv_name_product_details);
        txt_price = (TextView) view.findViewById(R.id.tv_price_product_details);
        txt_description = (TextView) view.findViewById(R.id.tv_description_product_details);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        btn_order = (Button) view.findViewById(R.id.btn_order);
    }
}