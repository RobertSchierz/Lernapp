package com.example.rob.lernapp;

import java.util.ArrayList;

public class ResponseExpand {
    public ResponseExpand() {
    }

    public interface responseExpandListener {
        void onChange(boolean isExpanded);
    }

    private boolean isExpand = false;
    private ArrayList<responseExpandListener>  listener = new ArrayList<>();

    public boolean isresponseExpand() {
        return isExpand;
    }

    public void setisresponseExpand(boolean isExpand) {
        this.isExpand = isExpand;

        if (listener != null){
            for (responseExpandListener rl :
                    this.listener) {

                rl.onChange(isExpand);
            }
        }
    }

    public ArrayList<responseExpandListener> getListener() {
        return listener;
    }

    public void setListener(responseExpandListener listener) {
        this.listener.add(listener);
    }





}
