package com.example.rob.lernapp.databaseUtilityClasses;


import android.widget.Button;

import com.example.rob.lernapp.LearngroupViewActivity;
import com.example.rob.lernapp.RestClient;
import com.example.rob.lernapp.RestClient_;
import com.example.rob.lernapp.RestExceptionAndErrorHandler;
import com.example.rob.lernapp.restDataPatch.NewMemberToGroupPatch;
import com.example.rob.lernapp.restDataPatch.PatchResponse;
import com.example.rob.lernapp.restdataGet.DatasetUser;
import com.example.rob.lernapp.restdataGet.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;

@EBean
public class DatabaseUtilityLearngroupView {

    @RootContext
    LearngroupViewActivity activity;


    @RestService
    RestClient restClient;

    @Bean
    RestExceptionAndErrorHandler restExceptionAndErrorHandler;


    private User[] databaseusers;


    @AfterInject
    void afterInject() {
        restClient = new RestClient_(activity);
        restClient.setRestErrorHandler(restExceptionAndErrorHandler);
    }


    @Background
    public void getUsers() {

            ResponseEntity<DatasetUser> responseEntity = restClient.getUsers();
            DatasetUser dataSet = responseEntity.getBody();
            this.databaseusers = dataSet.gettingUsers();
            sendUsersBackToActivity(this.databaseusers);

    }

    @Background
    public void postNewMemberToGroup(String _id, String groupID, Button addMemberButton) {

        NewMemberToGroupPatch newMember = new NewMemberToGroupPatch(_id, "postMember");
        ResponseEntity<JsonObject> responseEntityNewUserGroup = restClient.postNewMemberToGroup(groupID, newMember);
        sendNewMemberGroupResponse(responseEntityNewUserGroup, addMemberButton);

    }



    @UiThread
    void sendUsersBackToActivity(User[] users) {
        activity.getUsersBack(users);
    }

    @UiThread
    void sendNewMemberGroupResponse(ResponseEntity<JsonObject> patchResponse, Button addMemberButton) {

        if(patchResponse != null){
            Gson gson = new Gson();
            PatchResponse patchResponseGSON = gson.fromJson(patchResponse.getBody(), PatchResponse.class);
            this.activity.showInviteContactsDialog.getNewMemberGroupResponse(patchResponseGSON, addMemberButton);
        }
    }


}
