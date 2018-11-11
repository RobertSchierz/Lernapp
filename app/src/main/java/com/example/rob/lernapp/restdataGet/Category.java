package com.example.rob.lernapp.restdataGet;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeParcelable(this.group, flags);
        dest.writeString(this.name);
        dest.writeParcelable(this.creator, flags);
    }

    protected Category(Parcel in) {
        this._id = in.readString();
        this.group = in.readParcelable(Learngroup.class.getClassLoader());
        this.name = in.readString();
        this.creator = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
