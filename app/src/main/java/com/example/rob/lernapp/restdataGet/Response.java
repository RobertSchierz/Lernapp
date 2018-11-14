package com.example.rob.lernapp.restdataGet;

public class Response {

    public Response(String _id, ResponseTopic topic, User creator, String mediatype, String contenturl) {
        this._id = _id;
        this.topic = topic;
        this.creator = creator;
        this.mediatype = mediatype;
        this.contenturl = contenturl;
    }

    String _id;
    ResponseTopic topic;
    User creator;
    String mediatype;
    String contenturl;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ResponseTopic getTopic() {
        return topic;
    }

    public void setTopic(ResponseTopic topic) {
        this.topic = topic;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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
}

class ResponseTopic {

    public ResponseTopic(String _id, String name, User creator, String state, String type, String category) {
        this._id = _id;
        this.name = name;
        this.creator = creator;
        this.state = state;
        this.type = type;
        this.category = category;
    }

    String _id;
    String name;
    User creator;
    String state;
    String type;
    String category;


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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
