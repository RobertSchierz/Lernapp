package com.example.rob.lernapp.restdataPost;

import com.example.rob.lernapp.restdataGet.Topic;

public class PostResponseTopic {

    public PostResponseTopic(String message, Topic createdTopic) {
        this.message = message;
        this.createdTopic = createdTopic;
    }

    private String message;
    private Topic createdTopic;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Topic getCreatedTopic() {
        return createdTopic;
    }

    public void setCreatedTopic(Topic createdTopic) {
        this.createdTopic = createdTopic;
    }
}
