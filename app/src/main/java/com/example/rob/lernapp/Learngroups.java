package com.example.rob.lernapp;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.rob.lernapp.adapter.GrouplistRecyclerviewAdapter;
import com.example.rob.lernapp.databaseUtilityClasses.DatabaseUtilityLearngroups;
import com.example.rob.lernapp.dialoge.ConfirmGroupDialog;
import com.example.rob.lernapp.dialoge.ConfirmGroupDialog_;
import com.example.rob.lernapp.dialoge.DeleteGroupDialog;
import com.example.rob.lernapp.dialoge.DeleteGroupDialog_;
import com.example.rob.lernapp.dialoge.JointhroughlinkDialog;
import com.example.rob.lernapp.dialoge.JointhroughlinkDialog_;
import com.example.rob.lernapp.restDataPatch.PatchResponse;
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
public class Learngroups extends AppCompatActivity implements ConfirmGroupDialog.ConfirmGroupDialogListener, DeleteGroupDialog.DeleteGroupDialogListener, JointhroughlinkDialog.JointhroughlinkDialogListener {

    private String uniqueClientId;

    private String uniqueDatabaseId;

    public static ConfirmGroupDialog_ confirmGroupDialog;
    public static DeleteGroupDialog_ deleteGroupDialog;

    private ArrayList<Learngroup> creatorLearngroups;
    private ArrayList<Learngroup> learngroupsAll;

    private GrouplistRecyclerviewAdapter grouplistRecyclerviewAdapter;

    private boolean creatorGroupsSyncFinnish = false;
    private boolean allGroupsSyncFinnish = false;

    private JointhroughlinkDialog jointhroughlinkDialog;

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
    void onCreate(){
        UniqueIDHandler uniqueIDHandler = UniqueIDHandler.getInstance(this);
        try {
            this.uniqueClientId = uniqueIDHandler.handleUniqueID();
            PersistanceDataHandler.setUniqueClientId(this.uniqueClientId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        dataBaseUtilTask.getDatabaseId();
        Animation floatingactionanimation = AnimationUtils.loadAnimation(this, R.anim.floatingaction_onviewanim);
        floatingActionButton.setAnimation(floatingactionanimation);
    }

    public void initializeRecyclerview() {

        int animationID = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), animationID);


        grouplistRecyclerview.setHasFixedSize(true);
        LinearLayoutManager grouplist_layoutmanager = new LinearLayoutManager(getApplicationContext());
        grouplistRecyclerview.setLayoutManager(grouplist_layoutmanager);

        if(groupfilter.isChecked()){
            this.grouplistRecyclerviewAdapter = new GrouplistRecyclerviewAdapter(this.creatorLearngroups, this, getSupportFragmentManager());
        }else if (!groupfilter.isChecked()){
            this.grouplistRecyclerviewAdapter = new GrouplistRecyclerviewAdapter(this.learngroupsAll, this, getSupportFragmentManager());
        }


        grouplistRecyclerview.setAdapter(grouplistRecyclerviewAdapter);
        grouplistRecyclerview.setVisibility(View.VISIBLE);
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

    public void setAllGroups(Learngroup[] learngroupsAll) {
        this.learngroupsAll = new ArrayList<Learngroup>(Arrays.asList(learngroupsAll));
        this.allGroupsSyncFinnish = true;
        if(this.allGroupsSyncFinnish && this.creatorGroupsSyncFinnish){
            initializeRecyclerview();
            this.allGroupsSyncFinnish = false;
            this.creatorGroupsSyncFinnish = false;
        }



    }

    public void setCreatorGroups(Learngroup[] learngroupsCreator) {
        this.creatorLearngroups = new ArrayList<Learngroup>(Arrays.asList(learngroupsCreator));
        this.creatorGroupsSyncFinnish = true;
        if(this.allGroupsSyncFinnish && this.creatorGroupsSyncFinnish){
            initializeRecyclerview();
            this.allGroupsSyncFinnish = false;
            this.creatorGroupsSyncFinnish = false;
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

    @Click(R.id.join_throughlink)
    void joinThroughLinkClicked(){
        jointhroughlinkDialog = new JointhroughlinkDialog_();
        jointhroughlinkDialog.setVars(this);
        jointhroughlinkDialog.show(getSupportFragmentManager(), "joingroup");
    }

    @CheckedChange(R.id.grouplist_creator_member_switch)
    void switch_clicked(CompoundButton groupfilter, boolean isChecked) {

        if (isChecked) {
            updateRecyclerview(true);
        } else {
            updateRecyclerview(false);
        }

    }

    public void getResponseAddMemberLink(PatchResponse patchResponseGSON){
        dataBaseUtilTask.getDatabaseId();
        this.jointhroughlinkDialog.dismiss();
        Toast.makeText(this, "Member hinzugefügt!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onJoinLinkDialogPositiveClick(DialogFragment dialog, String memberid, String link) {
        this.dataBaseUtilTask.postNewMemberToGroupLink(memberid, link);
        this.jointhroughlinkDialog = (JointhroughlinkDialog) dialog;
    }



    @Override
    public void onJoinLInkDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
