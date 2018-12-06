package com.example.rob.lernapp.restdataPost;

import com.example.rob.lernapp.restdataGet.Learngroup;

public class PostResponseLearngroup {

    public PostResponseLearngroup(Learngroup createdGroups, String message) {
        this.createdGroups = createdGroups;
        this.message = message;
    }

    private Learngroup createdGroups;
    private String message;


    public Learngroup getCreatedGroups() {
        return createdGroups;
    }

    public void setCreatedGroups(Learngroup createdGroups) {
        this.createdGroups = createdGroups;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

