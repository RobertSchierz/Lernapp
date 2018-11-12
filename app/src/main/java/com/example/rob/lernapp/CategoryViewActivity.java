package com.example.rob.lernapp;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.rob.lernapp.adapter.TopiclistRecyclerviewAdapter;
import com.example.rob.lernapp.databaseUtilityClasses.DatabaseUtilityCategory;
import com.example.rob.lernapp.restdataGet.Category;
import com.example.rob.lernapp.restdataGet.Topic;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;

@EActivity(R.layout.activity_categoryview)
public class CategoryViewActivity extends AppCompatActivity {


    public Category category;
    public ArrayList<Topic> topics;
    private TopiclistRecyclerviewAdapter topiclistRecyclerviewAdapter;

    @NonConfigurationInstance
    @Bean
    DatabaseUtilityCategory dataBaseUtilTask;

    @ViewById(R.id.topicsrecyclerview)
    RecyclerView topicsrecyclerview;

    @SuppressLint("RestrictedApi")
    @AfterViews
    void onCreate(){
        if(getIntent().getExtras() != null){
            this.category = getIntent().getExtras().getParcelable("category");
        }

        setTitle(getResources().getText(R.string.topicsactivitylabel) + " - " + this.category.getName());
        dataBaseUtilTask.getTopics(this.category.get_id());
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataBaseUtilTask.getTopics(this.category.get_id());
    }

    void initilizeRecyclerview(){
        int animationID = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), animationID);


        topicsrecyclerview.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        topicsrecyclerview.setLayoutManager(horizontalLayoutManagaer);


        this.topiclistRecyclerviewAdapter = new TopiclistRecyclerviewAdapter(this.topics, this);

        topicsrecyclerview.setAdapter(this.topiclistRecyclerviewAdapter);
        topicsrecyclerview.setVisibility(View.VISIBLE);
        topicsrecyclerview.setLayoutAnimation(animation);
    }

    public void getTopicsBack(Topic[] topics){
        this.topics = new ArrayList<Topic>(Arrays.asList(topics));
        initilizeRecyclerview();
    }

}
