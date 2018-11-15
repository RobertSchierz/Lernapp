package com.example.rob.lernapp.restdataGet;

public class Topic {

    public Topic(String _id, String name, User creator, String state, String type, String text, String mediatype, String contenturl, Category category) {
        this._id = _id;
        this.name = name;
        this.creator = creator;
        this.state = state;
        this.type = type;
        this.text = text;
        this.mediatype = mediatype;
        this.contenturl = contenturl;
        this.category = category;
    }

    String _id;
    String name;
    User creator;
    String state;
    String type;
    String text;
    String mediatype;
    String contenturl;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }

    public String getContenturl() {
        return contenturl;
    }

    public void setContenturl(String contenturl) {
        this.contenturl = contenturl;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
