package com.example.rob.lernapp;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.rob.lernapp.databaseUtilityClasses.DatabaseUtilityLearngroupView;
import com.example.rob.lernapp.restdataGet.Learngroup;
import com.example.rob.lernapp.restdataGet.User;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_learngroupview)
public class LearngroupViewActivity extends AppCompatActivity implements AddMemberDialog.AddMemberDialogListener {

    private Learngroup group;
    private AddMemberDialog addMemberDialog;
    private GetContacts getContacts;
    private User[] databaseUsers;
    private ArrayList<User> contactUsersinDatabase = new ArrayList<User>();
    private ShowInviteContactsDialog showInviteContactsDialog;

    @NonConfigurationInstance
    @Bean
    DatabaseUtilityLearngroupView dataBaseUtilTask;

    @ViewById(R.id.learngroupview_actionbutton)
    FloatingActionButton learngroupview_actionbutton;

    @SuppressLint("RestrictedApi")
    @AfterViews
    void onCreate(){
        if(getIntent().getExtras() != null){
            this.group = getIntent().getExtras().getParcelable("group");
        }

        Animation floatingactionanimation = AnimationUtils.loadAnimation(this, R.anim.floatingaction_onviewanim);


        if(PersistanceDataHolder.getUniqueDatabaseId().equals(this.group.getCreator().get_id())){
            learngroupview_actionbutton.setVisibility(View.VISIBLE);
            learngroupview_actionbutton.startAnimation(floatingactionanimation);
        }

    }


    @Click(R.id.learngroupview_actionbutton)
    void addMember(){

        this.addMemberDialog = new AddMemberDialog_();
        this.addMemberDialog.setActivity(this);
        this.addMemberDialog.show(getSupportFragmentManager(), "addGroupMember");

    }

    @Override
    public void onCreateAddMemberDialogContactClick(DialogFragment dialog) {
        dataBaseUtilTask.getUsers();
    }

    @Override
    public void onCreateAddMemberDialogLinkClick(DialogFragment dialog) {

    }

    public void getUsersBack(User[] users){
        this.databaseUsers = users;
        getContactExec();
    }



    @UiThread
    void getContactExec() {
        if (Build.VERSION.SDK_INT >= 23) {
            this.getContacts = GetContacts.getInstance(this);
            this.getContacts.handlePermission();
        } else {
            this.getContacts.readContacts();
        }
        examineUsers();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case GetContacts.MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this.getApplicationContext(), "Berechtigung erlaubt", Toast.LENGTH_SHORT).show();
                    this.getContacts.readContacts();

                } else {
                    //Toast.makeText(this.getApplicationContext(), "Berechtigung verweigert", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    private void examineUsers() {

        boolean isAllreadyInGroup = false;
        this.contactUsersinDatabase = new ArrayList<User>();
        for (User iteratedUser : this.getContacts.contacts) {
            for (int i = 0; i < this.databaseUsers.length; i++) {
                if (iteratedUser.getPhonenumber() == this.databaseUsers[i].getPhonenumber()) {

                    boolean isInMember = false;
                    for (int j = 0; j < this.group.getMembers().length; j++) {
                        if(this.group.getMembers()[j].getMember().getPhonenumber() == iteratedUser.getPhonenumber()){
                            isInMember = true;
                        }
                    }
                    if(!isInMember){
                        this.contactUsersinDatabase.add(this.databaseUsers[i]);
                    }else{
                        isAllreadyInGroup = true;
                    }


                }
            }
        }

        if(this.contactUsersinDatabase.size() == 0){

            if(isAllreadyInGroup){
                Toast.makeText(this, "Anwender aus deinen Kontakten sind bereits in der Gruppe", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Keine deiner Kontakte benutzt diese App", Toast.LENGTH_SHORT).show();
            }

        }else{
            this.addMemberDialog.dismiss();
            showInviteContactsDialog = new ShowInviteContactsDialog_();
            showInviteContactsDialog.setActivity(this);
            showInviteContactsDialog.setContactUsers(this.contactUsersinDatabase);
            showInviteContactsDialog.show(getSupportFragmentManager(), "inviteUser");}
    }


}
