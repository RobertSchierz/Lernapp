package com.example.rob.lernapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.rob.lernapp.adapter.TopiclistRecyclerviewAdapter;
import com.example.rob.lernapp.databaseUtilityClasses.DatabaseUtilityCategory;
import com.example.rob.lernapp.restdataGet.Category;
import com.example.rob.lernapp.restdataGet.Response;
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
    public ArrayList<Response> responses;
    private TopiclistRecyclerviewAdapter topiclistRecyclerviewAdapter;
    public boolean Read_External_Storage_Permission = false;
    public boolean Write_External_Storgae_Permission = false;


    @NonConfigurationInstance
    @Bean
    DatabaseUtilityCategory dataBaseUtilTask;

    @ViewById(R.id.topicsrecyclerview)
    RecyclerView topicsrecyclerview;

    RecyclerView.OnScrollListener horizontalScrollListener = new RecyclerView.OnScrollListener() {

        private boolean dragged = false;
        public int finalScrollPosition;

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    dragged = false;
                    recyclerView.smoothScrollToPosition(finalScrollPosition);
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    dragged = true;
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                    dragged = true;
                    break;
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    int totalItemsInView = layoutManager.getItemCount();
                    this.finalScrollPosition = changeScrollHorizontal(totalItemsInView - 1, recyclerView);
                }
            }
        }
    };

    private int changeScrollHorizontal(int totalItemsInView, RecyclerView recyclerview) {
        int offset = recyclerview.computeHorizontalScrollOffset();
        int extent = recyclerview.computeHorizontalScrollExtent();

        int scrollValue;

        int offsetRest = offset % extent;
        int offsetRestMinus = offset - offsetRest;

        int viewposition;
        int rangecalcmodulo = offset % (extent);


        int percentage = 0;
        percentage = ((rangecalcmodulo) * 100) / extent;
        float alphaValue = ((float) percentage / 100);

        if (offsetRest > (extent / 2)) {
            int extentMinusRest = extent - offsetRest;
            scrollValue = offset + extentMinusRest;
            viewposition = scrollValue / extent;

            float alphadesc = 1;
            TopiclistRecyclerviewAdapter.TopicViewHolder lastItemVieholder = (TopiclistRecyclerviewAdapter.TopicViewHolder) recyclerview.findViewHolderForAdapterPosition(viewposition - 1);
            if (lastItemVieholder != null) {
                View lastItem = lastItemVieholder.itemView;
                lastItem.setAlpha(alphadesc - alphaValue);
            }

            TopiclistRecyclerviewAdapter.TopicViewHolder currentItemviewholder = (TopiclistRecyclerviewAdapter.TopicViewHolder) recyclerview.findViewHolderForAdapterPosition(viewposition);
            if (currentItemviewholder != null) {
                View currentItem = currentItemviewholder.itemView;
                currentItem.setAlpha(alphaValue);
            }
        } else {
            scrollValue = offsetRestMinus;
            viewposition = scrollValue / extent;

            float alphadesc = 1;
            TopiclistRecyclerviewAdapter.TopicViewHolder currentItemviewholder = (TopiclistRecyclerviewAdapter.TopicViewHolder) recyclerview.findViewHolderForAdapterPosition(viewposition);
            if (currentItemviewholder != null) {
                View currentItem = currentItemviewholder.itemView;
                currentItem.setAlpha(alphadesc - alphaValue);
            }


            TopiclistRecyclerviewAdapter.TopicViewHolder lastItemVieholder = (TopiclistRecyclerviewAdapter.TopicViewHolder) recyclerview.findViewHolderForAdapterPosition(viewposition + 1);
            if (lastItemVieholder != null) {
                View lastItem = lastItemVieholder.itemView;
                lastItem.setAlpha(alphaValue);
            }

        }

        return viewposition;
    }


    @SuppressLint("RestrictedApi")
    @AfterViews
    void onCreate() {
        if (getIntent().getExtras() != null) {
            this.category = getIntent().getExtras().getParcelable("category");
        }

        setTitle(getResources().getText(R.string.topicsactivitylabel) + " - " + this.category.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();

        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();

    }


    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Perrmission", "Permission is granted1");
                dataBaseUtilTask.getResponses();
                return true;
            } else {

                Log.v("Perrmission", "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Perrmission", "Permission is granted1");
            dataBaseUtilTask.getResponses();
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Perrmission", "Permission is granted2");
                dataBaseUtilTask.getResponses();
                return true;
            } else {

                Log.v("Perrmission", "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Perrmission", "Permission is granted2");
            dataBaseUtilTask.getResponses();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            switch (requestCode) {
                case 2:
                    Log.d("PermissionResult", "External storage2");
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.v("PermissionResult", "Permission: " + permissions[0] + "was " + grantResults[0]);
                        this.Write_External_Storgae_Permission = true;
                        if (this.Write_External_Storgae_Permission || this.Read_External_Storage_Permission) {
                            dataBaseUtilTask.getResponses();
                        }
                    } else {

                    }
                    break;

                case 3:
                    Log.d("PermissionResult", "External storage1");
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.v("", "Permission: " + permissions[0] + "was " + grantResults[0]);
                        this.Read_External_Storage_Permission = true;
                        if (this.Write_External_Storgae_Permission || this.Read_External_Storage_Permission) {
                            dataBaseUtilTask.getResponses();
                        }
                    } else {

                    }
                    break;
            }
        }

    }


    private void initilizeTopicsRecyclerview() {
        int animationID = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), animationID);


        topicsrecyclerview.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        topicsrecyclerview.setLayoutManager(horizontalLayoutManagaer);


        this.topiclistRecyclerviewAdapter = new TopiclistRecyclerviewAdapter(this.topics, this.responses, this);

        topicsrecyclerview.setAdapter(this.topiclistRecyclerviewAdapter);


        topicsrecyclerview.addOnScrollListener(horizontalScrollListener);

        topicsrecyclerview.setVisibility(View.VISIBLE);
        topicsrecyclerview.setLayoutAnimation(animation);


    }

    public void getTopicsBack(Topic[] topics) {
        this.topics = new ArrayList<Topic>(Arrays.asList(topics));
        initilizeTopicsRecyclerview();
    }

    public void getResponsesBack(Response[] responses) {
        this.responses = new ArrayList<Response>(Arrays.asList(responses));
        dataBaseUtilTask.getTopics(this.category.get_id());
    }




}
