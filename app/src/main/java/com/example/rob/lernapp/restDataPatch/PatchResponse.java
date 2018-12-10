package com.example.rob.lernapp.restDataPatch;

import com.example.rob.lernapp.restdataGet.Member;

public class PatchResponse {

    public PatchResponse(String message, Member member, String group) {
        this.message = message;
        this.member = member;
        this.group = group;
    }

    String message;
    Member member;
    String group;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}

