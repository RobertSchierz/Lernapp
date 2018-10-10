package com.example.rob.lernapp.restdata;

public class User {

    String _id;
    String name;
    int phonenumber;

    public User(String _id, String name, int phonenumber) {
        this._id = _id;
        this.name = name;
        this.phonenumber = phonenumber;
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

    public int getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(int phonenumber) {
        this.phonenumber = phonenumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", phonenumber=" + phonenumber +
                '}';
    }
}
