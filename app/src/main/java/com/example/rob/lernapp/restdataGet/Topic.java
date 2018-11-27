package com.example.rob.lernapp.restdataGet;

import android.os.Parcel;
import android.os.Parcelable;

public class Topic implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.name);
        dest.writeParcelable(this.creator, flags);
        dest.writeString(this.state);
        dest.writeString(this.type);
        dest.writeString(this.text);
        dest.writeString(this.mediatype);
        dest.writeString(this.contenturl);
        dest.writeParcelable(this.category, flags);
    }

    protected Topic(Parcel in) {
        this._id = in.readString();
        this.name = in.readString();
        this.creator = in.readParcelable(User.class.getClassLoader());
        this.state = in.readString();
        this.type = in.readString();
        this.text = in.readString();
        this.mediatype = in.readString();
        this.contenturl = in.readString();
        this.category = in.readParcelable(Category.class.getClassLoader());
    }

    public static final Parcelable.Creator<Topic> CREATOR = new Parcelable.Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel source) {
            return new Topic(source);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };
}
