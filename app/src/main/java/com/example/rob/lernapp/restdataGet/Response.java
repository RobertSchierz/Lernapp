package com.example.rob.lernapp.restdataGet;

public class Response {

    public Response(String _id, Topic topic, User creator, String text, String mediatype, String contenturl) {
        this._id = _id;
        this.topic = topic;
        this.creator = creator;
        this.text = text;
        this.mediatype = mediatype;
        this.contenturl = contenturl;
    }


    String _id;
    Topic topic;
    User creator;
    String text;
    String mediatype;
    String contenturl;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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
}


