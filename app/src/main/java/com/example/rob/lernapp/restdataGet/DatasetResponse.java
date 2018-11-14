package com.example.rob.lernapp.restdataGet;

public class DatasetResponse {

    public DatasetResponse(int count, Response[] responses) {
        this.count = count;
        this.responses = responses;
    }

    private int count;
    private Response[] responses;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Response[] getResponses() {
        return responses;
    }

    public void setResponses(Response[] responses) {
        this.responses = responses;
    }
}
