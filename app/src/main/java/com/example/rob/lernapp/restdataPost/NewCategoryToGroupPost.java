package com.example.rob.lernapp.restdataPost;

public class NewCategoryToGroupPost {

    public NewCategoryToGroupPost(String group, String name, String creator) {
        this.group = group;
        this.name = name;
        this.creator = creator;
    }

    String group;
    String name;
    String creator;


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }


}
