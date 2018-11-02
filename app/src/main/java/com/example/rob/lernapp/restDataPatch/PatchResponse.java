package com.example.rob.lernapp.restDataPatch;

import com.example.rob.lernapp.restdataGet.Member;

public class PatchResponse {

    String message;
    Member member;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PatchResponse(String message) {

        this.message = message;
    }


    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
