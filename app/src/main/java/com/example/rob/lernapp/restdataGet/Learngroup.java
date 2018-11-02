package com.example.rob.lernapp.restdataGet;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

public class Learngroup implements Parcelable {

    String _id;
    User creator;
    String name;
    Member[] members;


    public Learngroup(String _id, User creator, String name, Member[] members) {
        this._id = _id;
        this.creator = creator;
        this.name = name;
        this.members = members;
    }


    public void setNewMember(Member member){
        ArrayList<Member> tempArraylistMember = new ArrayList<Member>(Arrays.asList(this.members));
        tempArraylistMember.add(member);

        Member[] tempmeberarray = new Member[tempArraylistMember.size()];
        tempmeberarray = tempArraylistMember.toArray(tempmeberarray);

        this.members = tempmeberarray;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeParcelable(this.creator, flags);
        dest.writeString(this.name);
        dest.writeTypedArray(this.members, flags);
    }

    protected Learngroup(Parcel in) {
        this._id = in.readString();
        this.creator = in.readParcelable(User.class.getClassLoader());
        this.name = in.readString();
        this.members = in.createTypedArray(Member.CREATOR);
    }

    public static final Parcelable.Creator<Learngroup> CREATOR = new Parcelable.Creator<Learngroup>() {
        @Override
        public Learngroup createFromParcel(Parcel source) {
            return new Learngroup(source);
        }

        @Override
        public Learngroup[] newArray(int size) {
            return new Learngroup[size];
        }
    };
}
