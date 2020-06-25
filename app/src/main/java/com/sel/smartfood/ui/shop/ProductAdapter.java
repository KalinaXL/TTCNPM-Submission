package com.sel.smartfood.ui.shop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.sel.smartfood.R;
import com.sel.smartfood.data.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private List<Product> productList;
    private LayoutInflater layoutInflater;

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view = layoutInflater.inflate(R.layout.item_product, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(String.valueOf(product.getPrice()));
        holder.ratingBar.setRating(product.getRatingScore());
        holder.tvPreparationTime.setText(String.valueOf(product.getPreparationTime()));
    }


    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    public void setDataChanged(@Nullable List<Product> productList){
        this.productList = productList;
        notifyDataSetChanged();
    }

     static class ProductHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName;
        TextView tvPreparationTime;
        TextView tvPrice;
        AppCompatRatingBar ratingBar;
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_product_image);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvPreparationTime = itemView.findViewById(R.id.tv_preparation_time);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
            ratingBar = itemView.findViewById(R.id.rb_product_score);
        }
    }
}
