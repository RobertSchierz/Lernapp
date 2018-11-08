package com.example.rob.lernapp.restdataGet;

public class Category {

    public Category(String _id, Learngroup group, String name, User creator) {
        this._id = _id;
        this.group = group;
        this.name = name;
        this.creator = creator;
    }

    String _id;
    Learngroup group;
    String name;
    User creator;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Learngroup getGroup() {
        return group;
    }

    public void setGroup(Learngroup group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
