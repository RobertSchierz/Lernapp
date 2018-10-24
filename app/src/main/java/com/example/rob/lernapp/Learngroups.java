package com.example.rob.lernapp;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.example.rob.lernapp.adapter.GrouplistRecyclerviewAdapter;
import com.example.rob.lernapp.restdata.Learngroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

@EActivity(R.layout.activity_learngroups)
public class Learngroups extends AppCompatActivity implements ConfirmGroupDialog.ConfirmGroupDialogListener {

    private String uniqueClientId;

    private String uniqueDatabaseId;

    public static ConfirmGroupDialog_ confirmGroupDialog;

    private Learngroup[] creatorLearngroups;

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

    }

    private void initializeRecyclerview(){

        grouplistRecyclerview.setHasFixedSize(true);
        LinearLayoutManager grouplist_layoutmanager = new LinearLayoutManager(getApplicationContext());
        grouplistRecyclerview.setLayoutManager(grouplist_layoutmanager);
        GrouplistRecyclerviewAdapter grouplistRecyclerviewAdapter = new GrouplistRecyclerviewAdapter(this.creatorLearngroups, this, getSupportFragmentManager());
        grouplistRecyclerview.setAdapter(grouplistRecyclerviewAdapter);

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

    void setGroups(Learngroup[] creatorLearngroups){
        this.creatorLearngroups = creatorLearngroups;
        initializeRecyclerview();
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
