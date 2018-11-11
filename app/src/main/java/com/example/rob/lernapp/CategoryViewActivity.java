package com.example.rob.lernapp;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;

import com.example.rob.lernapp.restdataGet.Category;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_categoryview)
public class CategoryViewActivity extends AppCompatActivity {


    public Category category;

    @SuppressLint("RestrictedApi")
    @AfterViews
    void onCreate(){
        if(getIntent().getExtras() != null){
            this.category = getIntent().getExtras().getParcelable("category");
        }

        setTitle(getResources().getText(R.string.topicsactivitylabel) + " - " + this.category.getName());
        
    }

}
