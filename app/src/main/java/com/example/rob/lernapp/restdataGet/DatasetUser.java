package com.example.rob.lernapp.restdataGet;

public class DatasetUser {

    public DatasetUser(int count, User[] rows) {
        this.count = count;
        this.users = rows;
    }

    private int count;

    private User[] users;

    public int getCount() {
        return count;
    }


    public void setCount(int count) {
        this.count = count;
    }

    public User[] gettingUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }
}
