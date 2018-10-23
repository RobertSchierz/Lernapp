package com.example.rob.lernapp.restdata;

public class User {

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
}
