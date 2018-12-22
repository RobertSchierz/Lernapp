package com.example.rob.lernapp.restdataPost;

import com.example.rob.lernapp.restdataGet.User;

public class PostUserResponse {

    public PostUserResponse(User createdUser, String message) {
        this.createdUser = createdUser;
        this.message = message;
    }

    private User createdUser;
    private String message;

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
