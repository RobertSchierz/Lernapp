package com.example.rob.lernapp.restdata;

public class Member {
    public Member(String _id, String role, User member) {
        this._id = _id;
        this.role = role;
        this.member = member;
    }

    String _id;
    String role;
    User member;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }


}
