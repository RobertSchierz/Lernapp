package com.example.rob.lernapp.restDataPatch;

public class NewMemberToGroupPatch {

    String memberId;
    String method;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public NewMemberToGroupPatch(String memberId, String method) {

        this.memberId = memberId;
        this.method = method;
    }
}
