package com.example.rob.lernapp.databaseUtilityClasses;


import android.util.Log;

import com.example.rob.lernapp.LearngroupViewActivity;
import com.example.rob.lernapp.RestClient;
import com.example.rob.lernapp.RestClient_;
import com.example.rob.lernapp.restdataGet.DatasetUser;
import com.example.rob.lernapp.restdataGet.User;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

@EBean
public class DatabaseUtilityLearngroupView {

    @RootContext
    LearngroupViewActivity activity;

    @RestService
    RestClient restClient;

    private User[] databaseusers;



    @AfterInject
    void afterInject() {
        restClient = new RestClient_(activity);
    }


    @Background
    public void getUsers() {
        try {
            ResponseEntity<DatasetUser> responseEntity = restClient.getUsers();
            DatasetUser dataSet = responseEntity.getBody();
            this.databaseusers = dataSet.gettingUsers();
            sendUsersBackToActivity(this.databaseusers);
        } catch (RestClientException e) {
            Log.e("Rest error", e.toString());
        }
    }


    @UiThread
    void sendUsersBackToActivity(User[] users){
        activity.getUsersBack(users);
    }

}
