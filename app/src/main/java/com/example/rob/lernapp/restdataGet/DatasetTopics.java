package com.example.rob.lernapp.restdataGet;

public class DatasetTopics {

    public DatasetTopics(int count, Topic[] topics) {
        this.count = count;
        this.topics = topics;
    }

    private int count;
    private Topic[] topics;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Topic[] getTopics() {
        return topics;
    }

    public void setTopics(Topic[] topics) {
        this.topics = topics;
    }
}
