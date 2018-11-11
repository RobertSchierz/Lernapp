package com.example.rob.lernapp.restdataGet;

public class Topic {

    public Topic(String _id, String name, String content, User creator, String state, String type, Category category) {
        this._id = _id;
        this.name = name;
        this.content = content;
        this.creator = creator;
        this.state = state;
        this.type = type;
        this.category = category;

    }

    String _id;
    String name;
    String content;
    User creator;
    String state;
    String type;
    Category category;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
