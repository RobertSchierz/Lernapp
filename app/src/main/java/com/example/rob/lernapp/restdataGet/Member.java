package com.example.rob.lernapp.restdataGet;

import android.os.Parcel;
import android.os.Parcelable;

public class Member implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.role);
        dest.writeParcelable(this.member, flags);
    }

    protected Member(Parcel in) {
        this._id = in.readString();
        this.role = in.readString();
        this.member = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel source) {
            return new Member(source);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };
}
