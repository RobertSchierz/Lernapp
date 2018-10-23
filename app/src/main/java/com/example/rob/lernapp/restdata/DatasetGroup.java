package com.example.rob.lernapp.restdata;

public class DatasetGroup {

    public DatasetGroup(int count, Group[] rows) {
        this.count = count;
        this.groups = rows;
    }

    private int count;

    private Group[] groups;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Group[] gettingGroups() {
        return groups;
    }

    public void setGroups(Group[] users) {
        this.groups = users;
    }
}
