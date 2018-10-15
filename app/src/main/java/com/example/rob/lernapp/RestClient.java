package com.example.rob.lernapp;


import com.example.rob.lernapp.restdata.Dataset;
import com.example.rob.lernapp.restdata.User;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Rest(rootUrl = "http://learnapp.enif.uberspace.de/restapi/", converters = { FormHttpMessageConverter.class, GsonHttpMessageConverter.class })
public interface RestClient {

    @Get("/users")
    ResponseEntity<Dataset> getUsers();

    @Get("/users/{_id}")
    Object getUser(@Path String _id);

    /*@Post("/signup")
    ResponseEntity<User> signup(User user);

    @Post("/signin")
    ResponseEntity<User> signin(User user);
    */

    //void setRestTemplate(RestTemplate restTemplate);

}
