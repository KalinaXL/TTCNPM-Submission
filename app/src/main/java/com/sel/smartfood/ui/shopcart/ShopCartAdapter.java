package com.sel.smartfood.ui.shopcart;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sel.smartfood.R;
import com.sel.smartfood.viewmodel.ShopCartModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShopCartAdapter extends BaseAdapter {
    Context context;
    ArrayList<ShopCartModel> arrayShopcart;

    public ShopCartAdapter(Context context, ArrayList<ShopCartModel> arrayShopcart) {
        this.context = context;
        this.arrayShopcart = arrayShopcart;
    }

    @Override
    public int getCount() {
        return arrayShopcart.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayShopcart.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder{
        public TextView tv_shopcart_name, tv_shopcart_price;
        public ImageView iv_shopcart_item;
        public Button btn_minus, btn_values, btn_plus;


    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_shop_cart, null);
            viewHolder.tv_shopcart_name = (TextView) view.findViewById(R.id.tv_shopcart_name);
            viewHolder.tv_shopcart_price = (TextView) view.findViewById(R.id.tv_shopcart_price);
            viewHolder.iv_shopcart_item = (ImageView) view.findViewById(R.id.iv_shopcart_item);
            viewHolder.btn_minus = (Button) view.findViewById(R.id.btn_minus);
            viewHolder.btn_values = (Button) view.findViewById(R.id.btn_value);
            viewHolder.btn_plus = (Button) view.findViewById(R.id.btn_plus);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        ShopCartModel shopcart = (ShopCartModel) getItem(i);
        viewHolder.tv_shopcart_name.setText(shopcart.getProduct_name());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.tv_shopcart_price.setText(decimalFormat.format(shopcart.getProduct_price())+ " Đ");

        String str = shopcart.getProduct_image();
        Picasso.get().load(shopcart.getProduct_image())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.error)
                .into(viewHolder.iv_shopcart_item);
        viewHolder.btn_values.setText(String.valueOf(shopcart.getProduct_numbers()));

        return view;
    }
}
