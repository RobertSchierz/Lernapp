package com.example.rob.lernapp.databaseUtilityClasses;

import com.example.rob.lernapp.NewContentActivity;
import com.example.rob.lernapp.RestClient;
import com.example.rob.lernapp.RestClient_;
import com.example.rob.lernapp.restdataPost.PostResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@EBean
public class DatabaseUtilityNewContent {

    @RootContext
    NewContentActivity activity;


    @RestService
    RestClient restClient;

    @AfterInject
    void afterInject() {
        restClient = new RestClient_(activity);
    }

    @Background
    public void postTopic(String name, String state, String type, String text, String mediatype, String category, String contenturl, String creator) {

        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();

        data.set("name", name);
        data.set("state", state);
        data.set("type", type);
        data.set("text", text);
        data.set("mediatype", mediatype);
        data.set("category", category);

        if(!contenturl.isEmpty()){
            data.set("contenturl", new FileSystemResource(contenturl));
        }else{
            data.set("contenturl", "");
        }
        data.set("creator", creator);


        ResponseEntity<JsonObject> responseEntityTopiccreate = restClient.postTopic(data);
        if(responseEntityTopiccreate != null){
            Gson gson = new Gson();
            PostResponse postResponse = gson.fromJson(responseEntityTopiccreate.getBody(), PostResponse.class);
            sendcreateTopicResponseToActivity(postResponse);
        }
    }

    @UiThread
    void sendcreateTopicResponseToActivity(PostResponse postResponse) {
        activity.handleCreateTopic(postResponse);
    }

}
