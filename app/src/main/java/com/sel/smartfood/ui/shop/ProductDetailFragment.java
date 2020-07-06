package com.sel.smartfood.ui.shop;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.sel.smartfood.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

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

        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        Maps(view);
        if (getArguments() != null) {
            ProductDetailFragmentArgs args = ProductDetailFragmentArgs.fromBundle(getArguments());
            txt_name.setText(args.getProductName());

            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            txt_price.setText("Giá : " + decimalFormat.format(args.getProductPrice()) + " Đồng ");

            // hình ảnh sản phẩm
            String imgUrl = args.getProductImage();
            Picasso.get().load(args.getProductImage()).into(imageView_product_details);
        }


        return view;
    }

    private void Maps(View view) {
        toolbar_product_details = (Toolbar)view.findViewById(R.id.toolbar_product_details);
        imageView_product_details = (ImageView) view.findViewById(R.id.iv_product_details);
        txt_name = (TextView) view.findViewById(R.id.tv_name_product_details);
        txt_price = (TextView) view.findViewById(R.id.tv_price_product_details);
        txt_description = (TextView) view.findViewById(R.id.tv_description_product_details);
        spinner = view.findViewById(R.id.spinner);
        btn_order = view.findViewById(R.id.btn_order);
    }
}