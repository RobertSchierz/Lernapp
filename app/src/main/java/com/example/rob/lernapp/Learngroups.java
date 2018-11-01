package com.example.rob.lernapp;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.rob.lernapp.adapter.GrouplistRecyclerviewAdapter;
import com.example.rob.lernapp.databaseUtilityClasses.DatabaseUtilityLearngroups;
import com.example.rob.lernapp.restdataDelete.DeleteResponse;
import com.example.rob.lernapp.restdataGet.Learngroup;
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
public class Learngroups extends AppCompatActivity implements ConfirmGroupDialog.ConfirmGroupDialogListener, DeleteGroupDialog.DeleteGroupDialogListener {

    private String uniqueClientId;

    private String uniqueDatabaseId;

    public static ConfirmGroupDialog_ confirmGroupDialog;
    public static DeleteGroupDialog_ deleteGroupDialog;

    private ArrayList<Learngroup> creatorLearngroups;
    private ArrayList<Learngroup> learngroupsAll;

    private GrouplistRecyclerviewAdapter grouplistRecyclerviewAdapter;

    private boolean firstStart = true;

    @ViewById(R.id.groupllist_recyclerview)
    RecyclerView grouplistRecyclerview;

    @ViewById(R.id.grouplist_creator_member_switch)
    Switch groupfilter;

    @ViewById(R.id.learngroup_actionbutton)
    FloatingActionButton floatingActionButton;

    @NonConfigurationInstance
    @Bean
    DatabaseUtilityLearngroups dataBaseUtilTask;


    @AfterViews
    void onCreate() {

        UniqueIDHandler uniqueIDHandler = UniqueIDHandler.getInstance(this);
        try {
            this.uniqueClientId = uniqueIDHandler.handleUniqueID();
            PersistanceDataHandler.setUniqueClientId(this.uniqueClientId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataBaseUtilTask.getDatabaseId();
        Animation floatingactionanimation = AnimationUtils.loadAnimation(this, R.anim.floatingaction_onviewanim);
        floatingActionButton.setAnimation(floatingactionanimation);


    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.firstStart) {
            this.firstStart = false;
        } else {
            this.dataBaseUtilTask.getGroupsOfCreator();
            this.dataBaseUtilTask.getGroupsOfCreatorAll(false);
        }
    }


    public void initializeRecyclerview() {

        int animationID = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), animationID);


        grouplistRecyclerview.setHasFixedSize(true);
        LinearLayoutManager grouplist_layoutmanager = new LinearLayoutManager(getApplicationContext());
        grouplistRecyclerview.setLayoutManager(grouplist_layoutmanager);
        this.grouplistRecyclerviewAdapter = new GrouplistRecyclerviewAdapter(this.learngroupsAll, this, getSupportFragmentManager());
        grouplistRecyclerview.setAdapter(grouplistRecyclerviewAdapter);
        grouplistRecyclerview.setLayoutAnimation(animation);
    }

    public void updateRecyclerview(boolean creatorgroups) {

        if (creatorgroups) {
            groupfilter.setTextColor(Color.BLACK);
            this.grouplistRecyclerviewAdapter.setGroupsNew(this.creatorLearngroups);
            this.grouplistRecyclerviewAdapter.notifyDataSetChanged();
            this.grouplistRecyclerview.scheduleLayoutAnimation();


        } else {
            groupfilter.setTextColor(Color.GRAY);
            this.grouplistRecyclerviewAdapter.setGroupsNew(this.learngroupsAll);
            this.grouplistRecyclerviewAdapter.notifyDataSetChanged();
            this.grouplistRecyclerview.scheduleLayoutAnimation();

        }
    }


    public String getUniqueClientId() {
        return uniqueClientId;
    }

    public String getUniqueDatabaseId() {
        return uniqueDatabaseId;
    }


    public void setUniqueId(String id) {
        this.uniqueDatabaseId = id;
        PersistanceDataHandler.setUniqueDatabaseId(id);
        dataBaseUtilTask.initialzeGroups();
    }

    public void setAllGroups(Learngroup[] learngroupsAll, boolean init) {
        this.learngroupsAll = new ArrayList<Learngroup>(Arrays.asList(learngroupsAll));
        if (init) {
            initializeRecyclerview();
        }
        if (!this.firstStart) {
            updateRecyclerview(groupfilter.isChecked());
        }
    }

    public void setCreatorGroups(Learngroup[] learngroupsCreator) {
        this.creatorLearngroups = new ArrayList<Learngroup>(Arrays.asList(learngroupsCreator));
        if (!this.firstStart) {
            updateRecyclerview(groupfilter.isChecked());
        }
    }

    public void handleCreateResponse(PostResponse postResponse) {

        switch (postResponse.getMessage()) {
            case "Group created":

                this.creatorLearngroups.add(postResponse.getCreatedGroups());

                this.learngroupsAll.add(postResponse.getCreatedGroups());

                //initializeRecyclerview();
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

    public void handleDeleteResponse(DeleteResponse postResponse, Learngroup deletedGroup) {
        switch (postResponse.getMessage()) {
            case "Group deleted":

                for (Learngroup learngroup: this.creatorLearngroups) {
                    if(learngroup.get_id().equals(deletedGroup.get_id())){
                        this.creatorLearngroups.remove(learngroup);
                        break;
                    }
                }

                for (Learngroup learngroup: this.learngroupsAll) {
                    if(learngroup.get_id().equals( deletedGroup.get_id())){
                        this.learngroupsAll.remove(learngroup);
                        break;
                    }
                }
                
                updateRecyclerview(groupfilter.isChecked());

                break;
            case "deleteerror":
                Toast.makeText(getApplicationContext(), "Gruppe konnte nicht gelöscht werden", Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                break;
        }

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
    public void onCreateGroupDialogPositiveClick(DialogFragment dialog, EditText groupname) {
        String groupnametext = String.valueOf(groupname.getText());
        dataBaseUtilTask.postGroup(groupnametext);
    }

    @Override
    public void onCreateGroupDialogNegativeClick(DialogFragment dialog) {
        this.confirmGroupDialog.dismiss();
    }


    @Override
    public void onDeleteGroupDialogPositiveClick(DialogFragment dialog, DeleteGroupDialog deleteGroupDialog, Learngroup deletedGroup) {
        dataBaseUtilTask.deleteGroup(deletedGroup);
        deleteGroupDialog.dismiss();
    }

    @Override
    public void onDeleteGroupDialogNegativeClick(DialogFragment dialog, DeleteGroupDialog deleteGroupDialog) {
        deleteGroupDialog.dismiss();
    }
}
