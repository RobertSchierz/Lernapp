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
    String grouplink;


    public Learngroup(String _id, User creator, String name, Member[] members, String grouplink) {
        this._id = _id;
        this.creator = creator;
        this.name = name;
        this.members = members;
        this.grouplink = grouplink;
    }


    public void setNewMember(Member member){
        ArrayList<Member> tempArraylistMember = new ArrayList<Member>(Arrays.asList(this.members));
        tempArraylistMember.add(member);

        Member[] tempmeberarray = new Member[tempArraylistMember.size()];
        tempmeberarray = tempArraylistMember.toArray(tempmeberarray);

        this.members = tempmeberarray;
    }

    public boolean deleteMember(Member member){

        boolean success = false;

        ArrayList<Member> tempArraylistMember = new ArrayList<Member>(Arrays.asList(this.members));

        for (Member groupmember :
                tempArraylistMember) {
            if(groupmember.get_id().equals(member.get_id())){
                tempArraylistMember.remove(groupmember);

                Member[] tempmeberarray = new Member[tempArraylistMember.size()];
                tempmeberarray = tempArraylistMember.toArray(tempmeberarray);
                this.members = tempmeberarray;
                success = true;
                break;
            }else{
                success = false;
            }
        }

        return success;




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

    public String getGrouplink() {
        return grouplink;
    }

    public void setGrouplink(String grouplink) {
        this.grouplink = grouplink;
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
        dest.writeString(this.grouplink);
    }

    protected Learngroup(Parcel in) {
        this._id = in.readString();
        this.creator = in.readParcelable(User.class.getClassLoader());
        this.name = in.readString();
        this.members = in.createTypedArray(Member.CREATOR);
        this.grouplink = in.readString();
    }

    public static final Creator<Learngroup> CREATOR = new Creator<Learngroup>() {
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
