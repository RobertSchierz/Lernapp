package com.example.rob.lernapp.restdataGet;

public class DatasetCategories {

    private int count;
    private Category[] categories;

    public DatasetCategories(int count, Category[] categories) {
        this.count = count;
        this.categories = categories;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Category[] getCategories() {
        return categories;
    }

    public void setCategories(Category[] categories) {
        this.categories = categories;
    }
}
