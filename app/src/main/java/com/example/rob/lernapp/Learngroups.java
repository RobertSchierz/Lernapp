package com.example.rob.lernapp;

import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.rob.lernapp.adapter.GrouplistRecyclerviewAdapter;
import com.example.rob.lernapp.restdata.Learngroup;
import com.example.rob.lernapp.restdataPost.PostResponse;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@EActivity(R.layout.activity_learngroups)
public class Learngroups extends AppCompatActivity implements ConfirmGroupDialog.ConfirmGroupDialogListener {

    private String uniqueClientId;

    private String uniqueDatabaseId;

    public static ConfirmGroupDialog_ confirmGroupDialog;

    private ArrayList<Learngroup> creatorLearngroups;
    private ArrayList<Learngroup> learngroupsAll;

    private GrouplistRecyclerviewAdapter grouplistRecyclerviewAdapter;


    @ViewById(R.id.groupllist_recyclerview)
    RecyclerView grouplistRecyclerview;

    @ViewById(R.id.grouplist_creator_member_switch)
    Switch groupfilter;

    @NonConfigurationInstance
    @Bean
    DatabaseUtility dataBaseUtilTask;


    @AfterViews
    void onCreate() {
        UniqueIDHandler uniqueIDHandler = UniqueIDHandler.getInstance(this);
        try {
            this.uniqueClientId = uniqueIDHandler.handleUniqueID();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataBaseUtilTask.getDatabaseId();

    }

    public void initializeRecyclerview() {

        grouplistRecyclerview.setHasFixedSize(true);
        LinearLayoutManager grouplist_layoutmanager = new LinearLayoutManager(getApplicationContext());
        grouplistRecyclerview.setLayoutManager(grouplist_layoutmanager);
        ArrayList<Learngroup> finalrecyclerviewList = this.learngroupsAll;
        this.grouplistRecyclerviewAdapter = new GrouplistRecyclerviewAdapter(finalrecyclerviewList, this, getSupportFragmentManager());
        grouplistRecyclerview.setAdapter(grouplistRecyclerviewAdapter);

    }

    public void updateRecyclerview(boolean creatorgroups) {

        if (creatorgroups) {
            groupfilter.setTextColor(Color.BLACK);
            this.grouplistRecyclerviewAdapter.setGroupsNew(this.creatorLearngroups);
            this.grouplistRecyclerviewAdapter.notifyDataSetChanged();


        } else {
            groupfilter.setTextColor(Color.GRAY);
            this.grouplistRecyclerviewAdapter.setGroupsNew(this.learngroupsAll);
            this.grouplistRecyclerviewAdapter.notifyDataSetChanged();

        }
    }


    public String getUniqueClientId() {
        return uniqueClientId;
    }

    public String getUniqueDatabaseId() {
        return uniqueDatabaseId;
    }


    void setUniqueId(String id) {
        this.uniqueDatabaseId = id;
        dataBaseUtilTask.initialzeGroups();
    }

    void setAllGroups(Learngroup[] learngroupsAll, boolean init) {
        this.learngroupsAll = new ArrayList<Learngroup>(Arrays.asList(learngroupsAll));
        if (init) {
            initializeRecyclerview();
        }
    }

    void setCreatorGroups(Learngroup[] learngroupsCreator) {
        this.creatorLearngroups = new ArrayList<Learngroup>(Arrays.asList(learngroupsCreator));
    }

    void handleCreateResponse(PostResponse postResponse) {

        switch (postResponse.getMessage()) {
            case "Group created":


                this.creatorLearngroups.add(postResponse.getCreatedGroups());

                this.learngroupsAll.add(postResponse.getCreatedGroups());

                initializeRecyclerview();
                updateRecyclerview(groupfilter.isChecked());


                break;

            case "Createerror":
                Toast.makeText(getApplicationContext(), "Gruppe konnte nicht erstellt werden", Toast.LENGTH_SHORT).show();

                break;

            default:
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                break;
        }

        this.confirmGroupDialog.dismiss();

    }

    @CheckedChange(R.id.grouplist_creator_member_switch)
    void switch_clicked(CompoundButton groupfilter, boolean isChecked) {

        if (isChecked) {
            updateRecyclerview(true);
        } else {
            updateRecyclerview(false);
        }

    }

    @Click(R.id.learngroup_actionbutton)
    void actionbutton_clicked() {
        this.confirmGroupDialog = new ConfirmGroupDialog_();
        this.confirmGroupDialog.setActivity(this);
        this.confirmGroupDialog.show(getSupportFragmentManager(), "groupDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, EditText groupname) {
        String groupnametext = String.valueOf(groupname.getText());
        dataBaseUtilTask.postGroup(groupnametext);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        this.confirmGroupDialog.dismiss();
    }
}
