package com.example.rob.lernapp.restdataPost;

public class UserPost {

    public UserPost(String _id, String name, Integer phonenumber, String uniqueclientid) {
        this._id = _id;
        this.name = name;
        this.phonenumber = phonenumber;
        this.uniqueclientid = uniqueclientid;
    }

    String _id;
    String name;
    Integer phonenumber;
    String uniqueclientid;

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

    public Integer getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(Integer phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUniqueclientid() {
        return uniqueclientid;
    }

    public void setUniqueclientid(String uniqueclientid) {
        this.uniqueclientid = uniqueclientid;
    }
}
