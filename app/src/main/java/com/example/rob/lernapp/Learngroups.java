package com.example.rob.lernapp;

import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rob.lernapp.restdata.DatasetGroup;
import com.example.rob.lernapp.restdata.DatasetUser;
import com.example.rob.lernapp.restdata.User;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

@EActivity(R.layout.activity_learngroups)
public class Learngroups extends AppCompatActivity implements ConfirmGroupDialog.ConfirmGroupDialogListener {

    private String uniqueClientId;

    private String uniqueDatabaseId;

    public static ConfirmGroupDialog_ confirmGroupDialog;

    @ViewById(R.id.groupllist_recyclerview)
    RecyclerView grouplistRecyclerview;


    @NonConfigurationInstance
    @Bean
    DatabaseUtility idTask;


    @AfterViews
    void onCreate() {
        UniqueIDHandler uniqueIDHandler = UniqueIDHandler.getInstance(this);
        try {
            this.uniqueClientId = uniqueIDHandler.handleUniqueID();
        } catch (IOException e) {
            e.printStackTrace();
        }
        idTask.getDatabaseId();

        initializeRecyclerview();



    }

    private void initializeRecyclerview(){


        grouplistRecyclerview.setHasFixedSize(true);
        LinearLayoutManager grouplist_layoutmanager = new LinearLayoutManager(getApplicationContext());
        grouplistRecyclerview.setLayoutManager(grouplist_layoutmanager);

    }


    public String getUniqueClientId() {
        return uniqueClientId;
    }

    public String getUniqueDatabaseId() {
        return uniqueDatabaseId;
    }

    void setUniqueId(String id) {
        this.uniqueDatabaseId = id;
        idTask.getGroupsOfCreator();
    }


    @Click(R.id.learngroup_actionbutton)
    void actionbutton_clicked() {
        this.confirmGroupDialog = new ConfirmGroupDialog_();
        this.confirmGroupDialog.setActivity(this);
        this.confirmGroupDialog.show(getSupportFragmentManager(), "groupDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, EditText groupname) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        this.confirmGroupDialog.dismiss();
    }
}
