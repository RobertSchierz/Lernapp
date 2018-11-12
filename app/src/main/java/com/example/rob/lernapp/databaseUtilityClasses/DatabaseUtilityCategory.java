package com.example.rob.lernapp.databaseUtilityClasses;

import com.example.rob.lernapp.CategoryViewActivity;
import com.example.rob.lernapp.RestClient;
import com.example.rob.lernapp.RestClient_;
import com.example.rob.lernapp.RestExceptionAndErrorHandler;
import com.example.rob.lernapp.restdataGet.Category;
import com.example.rob.lernapp.restdataGet.DatasetTopics;
import com.example.rob.lernapp.restdataGet.Topic;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;

@EBean
public class DatabaseUtilityCategory {

    @RootContext
    CategoryViewActivity activity;

    @RestService
    RestClient restClient;

    @Bean
    RestExceptionAndErrorHandler restExceptionAndErrorHandler;

    private Category category;
    private Topic[] topics;

    @AfterInject
    void afterInject() {
        restClient = new RestClient_(activity);
        restClient.setRestErrorHandler(restExceptionAndErrorHandler);
    }

    @Background
    public void getTopics(String _categoryId){

        ResponseEntity<DatasetTopics> responseEntityTopics = restClient.getTopics(_categoryId);
        if(responseEntityTopics != null){
            DatasetTopics datasetTopic = responseEntityTopics.getBody();
            this.topics = datasetTopic.getTopics();
            sendTopicsBackToActivity(this.topics);
        }else{
            this.topics = new Topic[0];
            sendTopicsBackToActivity(this.topics);
        }

    }

    @UiThread
    public void sendTopicsBackToActivity(Topic[] topics){
        activity.getTopicsBack(topics);
    }

}
