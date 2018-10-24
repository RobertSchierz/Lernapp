package com.example.rob.lernapp;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.example.rob.lernapp.adapter.GrouplistRecyclerviewAdapter;
import com.example.rob.lernapp.restdata.Learngroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
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
    private Learngroup[] learngroupsAll;
    private Learngroup[] finalrecyclerviewList;


    @ViewById(R.id.groupllist_recyclerview)
    RecyclerView grouplistRecyclerview;

    @ViewById(R.id.grouplist_creator_member_switch)
    Switch groupfilter;

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

    public void initializeRecyclerview(){

        grouplistRecyclerview.setHasFixedSize(true);
        LinearLayoutManager grouplist_layoutmanager = new LinearLayoutManager(getApplicationContext());
        grouplistRecyclerview.setLayoutManager(grouplist_layoutmanager);
        this.finalrecyclerviewList = this.learngroupsAll;
        GrouplistRecyclerviewAdapter grouplistRecyclerviewAdapter = new GrouplistRecyclerviewAdapter(this.finalrecyclerviewList, this, getSupportFragmentManager());
        grouplistRecyclerview.setAdapter(grouplistRecyclerviewAdapter);

    }

    public void updateRecyclerview(boolean creatorgroups){
        grouplistRecyclerview.
        if(creatorgroups){
            for (int i = 0; i < this.finalrecyclerviewList.length; i++){
                boolean isIn = false;
                for (int j = 0; j < this.creatorLearngroups.length; j++){
                    if(this.creatorLearngroups[j].get_id() == this.finalrecyclerviewList[i].get_id()){
                        isIn = true;
                    }
                    if(!isIn){
                        this.finalrecyclerviewList.s
                    }
                }
            }
        }else{

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
        idTask.initialzeGroups();
    }

    void setAllGroups(Learngroup[] learngroupsAll, boolean init){
        this.learngroupsAll = learngroupsAll;
        if(init){
            initializeRecyclerview();
        }

    }
    void setCreatorGroups(Learngroup[] learngroupsCreator){
        this.creatorLearngroups = learngroupsCreator;

    }

    @CheckedChange(R.id.grouplist_creator_member_switch)
    void switch_clicked(CompoundButton groupfilter, boolean isChecked){

        if (isChecked) {
            updateRecyclerview(true);
        }else{
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

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        this.confirmGroupDialog.dismiss();
    }
}
