package com.example.rob.lernapp.restdataPost;

import com.example.rob.lernapp.restdata.Learngroup;

public class PostResponse {


    private Learngroup createdGroups;
    private String message;
    private Request request;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }




    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Learngroup getCreatedGroups() {
        return createdGroups;
    }

    public void setCreatedGroups(Learngroup createdGroups) {
        this.createdGroups = createdGroups;
    }

    public PostResponse(String message, Learngroup createdGroups, Request request) {
        this.message = message;
        this.createdGroups = createdGroups;
        this.request = request;
    }

}

class Request {


    String type;
    String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Request(String type, String url) {
        this.type = type;
        this.url = url;
    }
}
