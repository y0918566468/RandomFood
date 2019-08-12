package com.example.randomfood;

public class FoodData {
    int id;
    String foodWords;

    public FoodData(int id, String foodWords) {
        this.id = id;
        this.foodWords = foodWords;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodWords() {
        return foodWords;
    }

    public void setFoodWords(String foodWords) {
        this.foodWords = foodWords;
    }
}
