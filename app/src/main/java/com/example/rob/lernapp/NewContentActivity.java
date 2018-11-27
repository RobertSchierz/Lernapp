package com.example.rob.lernapp;

import android.support.v7.app.AppCompatActivity;

import com.example.rob.lernapp.restdataGet.Topic;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_new_content)
public class NewContentActivity extends AppCompatActivity {

    Topic topic;

    @AfterViews
    void onCreate() {

        if (getIntent().getExtras() != null) {
            this.topic = getIntent().getExtras().getParcelable("topic");

        }


        setTitle(getResources().getText(R.string.contentactivitylabel) + " für - " + this.topic.getName());

    }

}
