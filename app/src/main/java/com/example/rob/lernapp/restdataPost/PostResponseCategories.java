package com.example.rob.lernapp.restdataPost;

import com.example.rob.lernapp.restdataGet.Category;

public class PostResponseCategories {

    public PostResponseCategories(String message, Category createdCategory) {
        this.message = message;
        this.createdCategory = createdCategory;
    }

    String message;
    Category createdCategory;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Category getCreatedCategory() {
        return createdCategory;
    }

    public void setCreatedCategory(Category createdCategory) {
        this.createdCategory = createdCategory;
    }
}
