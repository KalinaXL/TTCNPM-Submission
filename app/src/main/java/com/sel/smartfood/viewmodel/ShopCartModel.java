package com.sel.smartfood.viewmodel;

public class ShopCartModel {
    public int product_id;
    public String product_name;
    public int product_price;
    public String product_image;
    public int product_numbers;

    public ShopCartModel(int product_id, String product_name, int product_price, String product_image, int product_numbers) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_image = product_image;
        this.product_numbers = product_numbers;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public int getProduct_numbers() {
        return product_numbers;
    }

    public void setProduct_numbers(int product_numbers) {
        this.product_numbers = product_numbers;
    }
}
