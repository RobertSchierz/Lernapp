package com.example.rob.lernapp.restDataPatch;

public class PatchResponse {

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PatchResponse(String message) {

        this.message = message;
    }

    String message;
}
