package com.example.rob.lernapp.databaseUtilityClasses;

import com.example.rob.lernapp.Learngroups;
import com.example.rob.lernapp.PersistanceDataHandler;
import com.example.rob.lernapp.RestClient;
import com.example.rob.lernapp.RestClient_;
import com.example.rob.lernapp.RestExceptionAndErrorHandler;
import com.example.rob.lernapp.restDataPatch.NewMemberToGroupPatch;
import com.example.rob.lernapp.restDataPatch.PatchResponse;
import com.example.rob.lernapp.restdataDelete.DeleteResponse;
import com.example.rob.lernapp.restdataGet.DatasetGroup;
import com.example.rob.lernapp.restdataGet.DatasetUser;
import com.example.rob.lernapp.restdataGet.Learngroup;
import com.example.rob.lernapp.restdataGet.User;
import com.example.rob.lernapp.restdataPost.LearngroupPost;
import com.example.rob.lernapp.restdataPost.PostResponse;
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
public class DatabaseUtilityLearngroups {

    @RootContext
    Learngroups activity;

    @RestService
    RestClient restClient;

    @Bean
    RestExceptionAndErrorHandler restExceptionAndErrorHandler;


    private User[] userinfos;
    private Learngroup[] creatorLearngroups;
    private Learngroup[] allLearngroups;


    @AfterInject
    void afterInject() {
        restClient = new RestClient_(activity);
        restClient.setRestErrorHandler(restExceptionAndErrorHandler);
    }

    @Background
    public void getGroupsOfCreator() {
        ResponseEntity<DatasetGroup> responseEntityGroup = restClient.getUserCreatorGroups(activity.getUniqueDatabaseId());
        DatasetGroup datasetGroup = responseEntityGroup.getBody();
        this.creatorLearngroups = datasetGroup.gettingGroups();
        sendCreatorGroupsToActivity(this.creatorLearngroups);
    }

    @Background
    public void getGroupsOfCreatorAll() {
        ResponseEntity<DatasetGroup> responseEntityGroup = restClient.getUserGroupsAll(activity.getUniqueDatabaseId());
        DatasetGroup datasetGroup = responseEntityGroup.getBody();
        this.allLearngroups = datasetGroup.gettingGroups();
        sendAllGroupsToActivity(this.allLearngroups);
    }

    @Background
    public void postGroup(String groupname) {

        LearngroupPost newgroup = new LearngroupPost(null, this.activity.getUniqueDatabaseId(), groupname, null);
        ResponseEntity<JsonObject> responseEntityGroupcreate = restClient.postGroup(newgroup);
        Gson gson = new Gson();
        PostResponse postResponse = gson.fromJson(responseEntityGroupcreate.getBody(), PostResponse.class);
        sendcreateResponseToActivity(postResponse);
    }

    @Background
    public void deleteGroup(Learngroup deletedGroup) {
        ResponseEntity<JsonObject> responseEntityGroupdelete = restClient.deleteGroup(deletedGroup.get_id());
        Gson gson = new Gson();
        DeleteResponse deleteResponse = gson.fromJson(responseEntityGroupdelete.getBody(), DeleteResponse.class);
        senddeleteResponseToActivity(deleteResponse, deletedGroup);

    }


    public void initialzeGroups() {
        getGroupsOfCreator();
        getGroupsOfCreatorAll();
    }


    @Background
    public void getDatabaseId() {


            ResponseEntity<DatasetUser> responseEntity = restClient.getUsers();
            DatasetUser dataSet = responseEntity.getBody();
            this.userinfos = dataSet.gettingUsers();
            for (int i = 0; i < this.userinfos.length; i++) {
                if (this.userinfos[i].getUniqueclientid().equals(activity.getUniqueClientId())) {
                    sendIdToActivity(this.userinfos[i].get_id());
                }
            }


    }

    @Background
    public void postNewMemberToGroupLink(String _id, String groupLink) {
        NewMemberToGroupPatch newMember = new NewMemberToGroupPatch(_id, null);
        ResponseEntity<JsonObject> responseEntityNewUserGroup = restClient.postNewMemberToGroupLink(groupLink, newMember);
        sendNewMemberGroupLinkResponse(responseEntityNewUserGroup);

    }

    @Background
    public void deleteMemberOfGroup(String _id) {
        NewMemberToGroupPatch newMember = new NewMemberToGroupPatch(PersistanceDataHandler.getUniqueDatabaseId(), "deleteMember");
        ResponseEntity<JsonObject> responseEntityDeleteUserGroup = restClient.deleteMemberOfGroup(_id, newMember);
        sendDeleteMemberGroupResponse(responseEntityDeleteUserGroup);

    }

    @UiThread
    void sendDeleteMemberGroupResponse(ResponseEntity<JsonObject> patchResponse){
        if(patchResponse != null){
            Gson gson = new Gson();
            PatchResponse patchResponseGSON = gson.fromJson(patchResponse.getBody(), PatchResponse.class);
            this.activity.getResponseDeleteMember(patchResponseGSON);
        }
    }


    @UiThread
    void sendNewMemberGroupLinkResponse(ResponseEntity<JsonObject> patchResponse) {
            if(patchResponse != null){
                Gson gson = new Gson();
                PatchResponse patchResponseGSON = gson.fromJson(patchResponse.getBody(), PatchResponse.class);
                this.activity.getResponseJoinThroughLink(patchResponseGSON);
            }
    }


    @UiThread
    void sendIdToActivity(String id) {
        activity.setUniqueId(id);
    }

    @UiThread
    void sendAllGroupsToActivity(Learngroup[] learngroups) {
        activity.setAllGroups(learngroups);
    }

    @UiThread
    void sendCreatorGroupsToActivity(Learngroup[] learngroups) {
        activity.setCreatorGroups(learngroups);
    }

    @UiThread
    void sendcreateResponseToActivity(PostResponse postResponse) {
        activity.handleCreateResponse(postResponse);
    }

    @UiThread
    void senddeleteResponseToActivity(DeleteResponse deleteResponse, Learngroup deletedGroup) {
        activity.handleDeleteResponse(deleteResponse, deletedGroup);
    }

}
