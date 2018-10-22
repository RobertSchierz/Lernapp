package com.example.rob.lernapp;


import com.example.rob.lernapp.restdata.DatasetGroup;
import com.example.rob.lernapp.restdata.DatasetUser;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
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

    //@Get("/groups/{_id}")
    //ResponseEntity<DatasetGroup> getGroups();



    /*@Post("/signup")
    ResponseEntity<User> signup(User user);

    @Post("/signin")
    ResponseEntity<User> signin(User user);
    */

    //void setRestTemplate(RestTemplate restTemplate);

}
