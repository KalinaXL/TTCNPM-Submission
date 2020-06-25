package com.sel.smartfood.data.model;

public class Product {
    private int id;
    private int categoryId;
    private String name;
    private int price;
    private float preparationTime;
    private int ratingScore;

    public Product(int id, int categoryId, String name, int price, float preparationTime, int ratingScore){
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.preparationTime = preparationTime;
        this.ratingScore = ratingScore;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getPrice() {
        return price;
    }

    public float getPreparationTime() {
        return preparationTime;
    }

    public int getRatingScore() {
        return ratingScore;
    }
}
