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
import com.example.rob.lernapp.dialoge.JointhroughlinkDialog;
import com.example.rob.lernapp.dialoge.JointhroughlinkDialog_;
import com.example.rob.lernapp.restDataPatch.PatchResponse;
import com.example.rob.lernapp.restdataDelete.DeleteResponse;
import com.example.rob.lernapp.restdataGet.Learngroup;
import com.example.rob.lernapp.restdataPost.PostResponseLearngroup;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


@EActivity(R.layout.activity_learngroups)
public class LearngroupsActivity extends AppCompatActivity implements ConfirmGroupDialog.ConfirmGroupDialogListener, DeleteGroupDialog.DeleteGroupDialogListener, JointhroughlinkDialog.JointhroughlinkDialogListener {

    private String uniqueClientId;

    private String uniqueDatabaseId;

    public ConfirmGroupDialog confirmGroupDialog;
    public DeleteGroupDialog deleteGroupDialog;

    private ArrayList<Learngroup> creatorLearngroups;
    private ArrayList<Learngroup> learngroupsAll;

    private GrouplistRecyclerviewAdapter grouplistRecyclerviewAdapter;

    private boolean creatorGroupsSyncFinnish = false;
    private boolean allGroupsSyncFinnish = false;
    private boolean isFabOpen = false;

    private JointhroughlinkDialog jointhroughlinkDialog;

    private Socket learnappSocket;

    private Gson gsonhelper = new Gson();


    @ViewById(R.id.groupllist_recyclerview)
    RecyclerView grouplistRecyclerview;

    @ViewById(R.id.grouplist_creator_member_switch)
    Switch groupfilter;

    @ViewById(R.id.learngroup_actionbutton)
    FloatingActionButton fab_learngroupmain;

    @ViewById(R.id.learngroup_actionbutton_addgroup)
    FloatingActionButton fab_addgroup;

    @ViewById(R.id.learngroup_actionbutton_addgrouplink)
    FloatingActionButton fab_addgrouplink;

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


        this.learnappSocket = SocketHandler.getInstance().getlearnappSocket();
        this.learnappSocket.connect();

        this.learnappSocket.on("deletedGroup", onDeletedGroup);
        this.learnappSocket.on("groupMemberDeleted", onMemberLeaveGroup);


    }

    private Emitter.Listener onMemberLeaveGroup = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (args[0] != null && args != null) {

                socketIOMemberLeaveGroup(getJsonObjectforSocketIO(args[0]), false);
            } else {
                socketIOMemberLeaveGroup(null, true);
            }

        }
    };

    @UiThread
    void socketIOMemberLeaveGroup(JsonObject data, boolean error) {

        if (!error) {
            PatchResponse deletedMemberPatchResponse = this.gsonhelper.fromJson(data, PatchResponse.class);

            for (Learngroup learngroup : this.creatorLearngroups) {
                if (learngroup.get_id().equals(deletedMemberPatchResponse.getGroup())) {
                    if(learngroup.deleteMember(deletedMemberPatchResponse.getMember()) == false){
                        Toast.makeText(this, "SocketIO - Fehler (Delete Member from Group)", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }

            for (Learngroup learngroup : this.learngroupsAll) {
                if (learngroup.get_id().equals(deletedMemberPatchResponse.getGroup())) {
                    if(learngroup.deleteMember(deletedMemberPatchResponse.getMember()) == false){
                        Toast.makeText(this, "SocketIO - Fehler (Delete Member from Group)", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }

        } else {
            Toast.makeText(LearngroupsActivity.this, "Fehler beim Datenempfang (Gelöschtes Member)", Toast.LENGTH_LONG).show();
        }

    }

    private Emitter.Listener onDeletedGroup = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (args[0] != null && args != null) {

                socketIODeletedGroup(getJsonObjectforSocketIO(args[0]), false);
            } else {
                socketIODeletedGroup(null, true);
            }

        }
    };


    @UiThread
    void socketIODeletedGroup(JsonObject data, boolean error) {
        if (!error) {
            Learngroup deletedLearngroup = this.gsonhelper.fromJson(data, Learngroup.class);
            if (deleteGroupLocally(deletedLearngroup)) {
                updateRecyclerview(groupfilter.isChecked());
            }

        } else {
            Toast.makeText(LearngroupsActivity.this, "Fehler beim Datenempfang (Gelöschte Gruppe)", Toast.LENGTH_LONG).show();
        }


    }

    private JsonObject getJsonObjectforSocketIO(Object arg) {
        JsonParser parser = new JsonParser();
        return parser.parse(String.valueOf(arg)).getAsJsonObject();
    }


    @Override
    protected void onResume() {
        super.onResume();
        dataBaseUtilTask.getDatabaseId();
        Animation floatingactionanimation = AnimationUtils.loadAnimation(this, R.anim.floatingaction_onviewanim);
        fab_learngroupmain.setAnimation(floatingactionanimation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.isFabOpen = false;
        this.closeFabMenu();
    }

    public void initializeRecyclerview() {

        int animationID = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), animationID);


        grouplistRecyclerview.setHasFixedSize(true);
        LinearLayoutManager grouplist_layoutmanager = new LinearLayoutManager(getApplicationContext());
        grouplistRecyclerview.setLayoutManager(grouplist_layoutmanager);

        if (groupfilter.isChecked()) {
            this.grouplistRecyclerviewAdapter = new GrouplistRecyclerviewAdapter(this.creatorLearngroups, this, getSupportFragmentManager());
        } else if (!groupfilter.isChecked()) {
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
        if (this.allGroupsSyncFinnish && this.creatorGroupsSyncFinnish) {
            initializeRecyclerview();
            this.allGroupsSyncFinnish = false;
            this.creatorGroupsSyncFinnish = false;
        }


    }

    public void setCreatorGroups(Learngroup[] learngroupsCreator) {
        this.creatorLearngroups = new ArrayList<Learngroup>(Arrays.asList(learngroupsCreator));
        this.creatorGroupsSyncFinnish = true;
        if (this.allGroupsSyncFinnish && this.creatorGroupsSyncFinnish) {
            initializeRecyclerview();
            this.allGroupsSyncFinnish = false;
            this.creatorGroupsSyncFinnish = false;
        }
    }

    public void handleCreateResponse(PostResponseLearngroup postResponseLearngroup) {

        if (postResponseLearngroup != null) {
            this.creatorLearngroups.add(postResponseLearngroup.getCreatedGroups());
            this.learngroupsAll.add(postResponseLearngroup.getCreatedGroups());
            updateRecyclerview(groupfilter.isChecked());
            this.confirmGroupDialog.dismiss();
        }
    }

    public void handleDeleteResponse(DeleteResponse postResponse, Learngroup deletedGroup) {


        if (postResponse != null) {
            deleteGroupLocally(deletedGroup);
            this.deleteGroupDialog.dismiss();
            updateRecyclerview(groupfilter.isChecked());

            //SocketIO Function
            String deletedGroupJson = this.gsonhelper.toJson(deletedGroup);
            this.learnappSocket.emit("GroupDeleted", deletedGroupJson);

        }
    }

    private boolean deleteGroupLocally(Learngroup deletedGroup) {
        boolean booldeletedGroup = false;
        for (Learngroup learngroup : this.creatorLearngroups) {
            if (learngroup.get_id().equals(deletedGroup.get_id())) {
                this.creatorLearngroups.remove(learngroup);
                booldeletedGroup = true;
                break;
            }
        }

        for (Learngroup learngroup : this.learngroupsAll) {
            if (learngroup.get_id().equals(deletedGroup.get_id())) {
                this.learngroupsAll.remove(learngroup);
                booldeletedGroup = true;
                break;
            }
        }

        return booldeletedGroup;
    }

    @Click(R.id.learngroup_actionbutton_addgrouplink)
    void joinThroughLinkClicked() {
        jointhroughlinkDialog = new JointhroughlinkDialog_();
        jointhroughlinkDialog.setVars(this);
        jointhroughlinkDialog.show(getSupportFragmentManager(), "joingroup");
    }

    @Click(R.id.learngroup_actionbutton_addgroup)
    void addgroupClicked() {
        this.confirmGroupDialog = new ConfirmGroupDialog_();
        this.confirmGroupDialog.setActivity(this);
        this.confirmGroupDialog.show(getSupportFragmentManager(), "groupDialog");
    }

    @CheckedChange(R.id.grouplist_creator_member_switch)
    void switch_clicked(CompoundButton groupfilter, boolean isChecked) {

        if (isChecked) {
            updateRecyclerview(true);
        } else {
            updateRecyclerview(false);
        }

    }

    public void getResponseDeleteMember(PatchResponse patchResponseGSON) {

        //SocketIO Function
        String deletedMemberPatchJson = this.gsonhelper.toJson(patchResponseGSON);
        this.learnappSocket.emit("GroupMemberDeleted", deletedMemberPatchJson);

        dataBaseUtilTask.getDatabaseId();
        this.deleteGroupDialog.dismiss();
    }

    public void getResponseJoinThroughLink(PatchResponse patchResponseGSON) {
        dataBaseUtilTask.getDatabaseId();
        this.jointhroughlinkDialog.dismiss();
    }

    @Click(R.id.learngroup_actionbutton)
    void actionbuttonClicked() {

        if (!this.isFabOpen) {
            this.isFabOpen = true;
            showFabMenu();
        } else {
            this.isFabOpen = false;
            closeFabMenu();
        }


    }

    private void showFabMenu() {
        this.fab_addgroup.animate().translationY(-getResources().getDimension(R.dimen.fabmargin_1));
        this.fab_addgrouplink.animate().translationY(-getResources().getDimension(R.dimen.fabmargin_2));
        this.fab_addgroup.animate().alpha(1);
        this.fab_addgrouplink.animate().alpha(1);
    }

    private void closeFabMenu() {
        this.fab_addgroup.animate().translationY(0);
        this.fab_addgrouplink.animate().translationY(0);
        this.fab_addgroup.animate().alpha(0);
        this.fab_addgrouplink.animate().alpha(0);
    }

    @Override
    public void onCreateGroupDialogPositiveClick(DialogFragment dialog, EditText groupname) {
        String groupnametext = String.valueOf(groupname.getText());
        dataBaseUtilTask.postGroup(groupnametext);
        this.isFabOpen = false;
        this.closeFabMenu();
    }

    @Override
    public void onCreateGroupDialogNegativeClick(DialogFragment dialog) {
        this.confirmGroupDialog.dismiss();
    }


    @Override
    public void onDeleteGroupDialogPositiveClick(DialogFragment dialog, DeleteGroupDialog originDeleteGroupDialog, Learngroup deletedGroup) {
        this.deleteGroupDialog = originDeleteGroupDialog;
        if (PersistanceDataHandler.getUniqueDatabaseId().equals(deletedGroup.getCreator().get_id())) {
            dataBaseUtilTask.deleteGroup(deletedGroup);
        } else {
            dataBaseUtilTask.deleteMemberOfGroup(deletedGroup.get_id());
        }

    }

    @Override
    public void onDeleteGroupDialogNegativeClick(DialogFragment dialog, DeleteGroupDialog deleteGroupDialog) {
        this.deleteGroupDialog = deleteGroupDialog;
        this.deleteGroupDialog.dismiss();
    }

    @Override
    public void onJoinLinkDialogPositiveClick(DialogFragment dialog, String memberid, String link) {
        this.dataBaseUtilTask.postNewMemberToGroupLink(memberid, link);
        this.jointhroughlinkDialog = (JointhroughlinkDialog) dialog;
        this.isFabOpen = false;
        this.closeFabMenu();
    }


    @Override
    public void onJoinLInkDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
