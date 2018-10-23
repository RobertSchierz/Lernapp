package com.example.rob.lernapp;

import android.util.Log;

import com.example.rob.lernapp.restdata.DatasetGroup;
import com.example.rob.lernapp.restdata.DatasetUser;
import com.example.rob.lernapp.restdata.User;
import com.example.rob.lernapp.restdata.Group;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

@EBean
public class DatabaseUtility {

    @RootContext
    Learngroups activity;

    @RestService
    RestClient restClient;

    private User[] userinfos;
    private Group[] groupinfos;

    @AfterInject
    void afterInject() {
        restClient = new RestClient_(activity);
    }

    @Background
    public void getGroupsOfCreator(){
        ResponseEntity<DatasetGroup> responseEntityGroup = restClient.getUserCreatorGroups(activity.getUniqueDatabaseId());
        DatasetGroup datasetGroup = responseEntityGroup.getBody();
        this.groupinfos = datasetGroup.gettingGroups();
    }


    @Background
    public void getDatabaseId() {
        try {

            ResponseEntity<DatasetUser> responseEntity = restClient.getUsers();
            DatasetUser dataSet = responseEntity.getBody();
            this.userinfos = dataSet.gettingUsers();
            for (int i = 0; i < this.userinfos.length; i++) {
                if (this.userinfos[i].getUniqueclientid().equals(activity.getUniqueClientId())) {
                    sendIdToActivity(this.userinfos[i].get_id());
                }
            }

        } catch (RestClientException e) {
            Log.e("Rest error", e.toString());
        }

    }

    @UiThread
    void sendIdToActivity(String id) {
        activity.setUniqueId(id);
    }


}
