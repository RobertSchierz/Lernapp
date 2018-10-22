package com.example.rob.lernapp.restdata;

public class Dataset {

    public Dataset(int count, User[] rows) {
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
