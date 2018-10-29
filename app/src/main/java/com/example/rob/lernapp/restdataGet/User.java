package com.example.rob.lernapp.restdataGet;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    String _id;
    String name;
    long phonenumber;
    String uniqueclientid;

    public String getUniqueclientid() {
        return uniqueclientid;
    }

    public void setUniqueclientid(String uniqueclientid) {
        this.uniqueclientid = uniqueclientid;
    }

    public User(String _id, String name, long phonenumber, String uniqueclientid ) {
        this._id = _id;
        this.name = name;
        this.phonenumber = phonenumber;
        this.uniqueclientid = uniqueclientid;
    }

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

    public long getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(long phonenumber) {
        this.phonenumber = phonenumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", phonenumber=" + phonenumber +
                ", uniqueclientid='" + uniqueclientid + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.name);
        dest.writeLong(this.phonenumber);
        dest.writeString(this.uniqueclientid);
    }

    protected User(Parcel in) {
        this._id = in.readString();
        this.name = in.readString();
        this.phonenumber = in.readLong();
        this.uniqueclientid = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
