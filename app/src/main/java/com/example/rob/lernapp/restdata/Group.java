package com.example.rob.lernapp.restdata;

import org.json.JSONArray;

public class Group {

    String _id;
    User creator;
    String name;
    Member[] members;

    public Group(String _id, User creator, String name, Member[] members) {
        this._id = _id;
        this.creator = creator;
        this.name = name;
        this.members = members;
    }

    @Override
    public String toString() {
        return "Group{" +
                "_id='" + _id + '\'' +
                ", creator='" + creator + '\'' +
                ", name='" + name + '\'' +
                ", members=" + members +
                '}';
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User[] User) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Member[] getMembers() {
        return members;
    }

    public void setMembers(Member[] members) {
        this.members = members;
    }



}
