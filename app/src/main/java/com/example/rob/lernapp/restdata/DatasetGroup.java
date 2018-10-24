package com.example.rob.lernapp.restdata;

public class DatasetGroup {

    public DatasetGroup(int count, Learngroup[] rows) {
        this.count = count;
        this.groups = rows;
    }

    private int count;

    private Learngroup[] groups;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Learngroup[] gettingGroups() {
        return groups;
    }

    public void setLearngroups(Learngroup[] users) {
        this.groups = users;
    }
}
