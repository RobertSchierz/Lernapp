package com.example.rob.lernapp;


import com.example.rob.lernapp.restDataPatch.NewMemberToGroupPatch;
import com.example.rob.lernapp.restdataGet.DatasetGroup;
import com.example.rob.lernapp.restdataGet.DatasetUser;
import com.example.rob.lernapp.restdataPost.LearngroupPost;
import com.google.gson.JsonObject;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Delete;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Patch;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

@Rest(rootUrl = "http://learnapp.enif.uberspace.de/restapi/", converters = { FormHttpMessageConverter.class, GsonHttpMessageConverter.class })
public interface RestClient {

    @Get("/users")
    ResponseEntity<DatasetUser> getUsers();

    @Get("/users/{_id}")
    Object getUser(@Path String _id);

    @Get("/groups")
    ResponseEntity<DatasetGroup> getGroups();

    @Get("/groups/usergroups/{_id}")
    ResponseEntity<DatasetGroup> getUserCreatorGroups(@Path String _id);

    @Get("/groups/usergroupsall/{_id}")
    ResponseEntity<DatasetGroup> getUserGroupsAll(@Path String _id);

    @Post("/groups/")
    ResponseEntity<JsonObject> postGroup(@Body LearngroupPost body);

    @Delete("/groups/{_id}")
    ResponseEntity<JsonObject> deleteGroup(@Path String _id);

    @Patch("/groups/{_id}")
    ResponseEntity<JsonObject> postNewMemberToGroup(@Path String _id, @Body NewMemberToGroupPatch body);



    //void setRestTemplate(RestTemplate restTemplate);

}
