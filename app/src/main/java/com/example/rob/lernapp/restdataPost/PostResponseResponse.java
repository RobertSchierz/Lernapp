package com.example.rob.lernapp.restdataPost;

import com.example.rob.lernapp.restdataGet.Response;

public class PostResponseResponse {

    public PostResponseResponse(String message, Response createdResponse) {
        this.message = message;
        this.createdResponse = createdResponse;
    }

    private String message;
    private Response createdResponse;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Response getCreatedResponse() {
        return createdResponse;
    }

    public void setCreatedResponse(Response createdResponse) {
        this.createdResponse = createdResponse;
    }
}
