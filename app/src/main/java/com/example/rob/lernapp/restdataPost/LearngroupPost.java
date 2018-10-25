package com.example.rob.lernapp.restdataPost;

import com.example.rob.lernapp.restdataGet.Member;
import com.example.rob.lernapp.restdataGet.User;

public class LearngroupPost {


    String _id;
    String creator;
    String name;
    Member[] members;


    public LearngroupPost(String _id, String creator, String name, Member[] members) {
        this._id = _id;
        this.creator = creator;
        this.name = name;
        this.members = members;
    }


    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreator() {
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


}
