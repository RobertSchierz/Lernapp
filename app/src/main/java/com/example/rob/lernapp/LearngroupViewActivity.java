package com.example.rob.lernapp;

import android.support.v7.app.AppCompatActivity;

import com.example.rob.lernapp.restdataGet.Learngroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_learngroupview)
public class LearngroupViewActivity extends AppCompatActivity {

    private Learngroup group;


    @AfterViews
    void onCreate(){
        if(getIntent().getExtras() != null){
            this.group = getIntent().getExtras().getParcelable("group");
        }

    }

}
