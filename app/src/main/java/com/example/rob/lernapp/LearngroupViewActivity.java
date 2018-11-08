package com.example.rob.lernapp;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rob.lernapp.adapter.CategoryRecyclerlistLayoutManager;
import com.example.rob.lernapp.adapter.CategorylistRecyclerviewAdapter;
import com.example.rob.lernapp.databaseUtilityClasses.DatabaseUtilityLearngroupView;
import com.example.rob.lernapp.dialoge.AddMemberDialog;
import com.example.rob.lernapp.dialoge.AddMemberDialog_;
import com.example.rob.lernapp.dialoge.InviteLinkDialog;
import com.example.rob.lernapp.dialoge.InviteLinkDialog_;
import com.example.rob.lernapp.dialoge.ShowInviteContactsDialog;
import com.example.rob.lernapp.dialoge.ShowInviteContactsDialog_;
import com.example.rob.lernapp.restdataGet.Category;
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
import java.util.Arrays;

@EActivity(R.layout.activity_learngroupview)
public class LearngroupViewActivity extends AppCompatActivity implements AddMemberDialog.AddMemberDialogListener, InviteLinkDialog.InviteLinkDialogListener {


    public Learngroup group;
    private AddMemberDialog addMemberDialog;
    private GetContacts getContacts;
    private User[] databaseUsers;
    private ArrayList<User> contactUsersinDatabase = new ArrayList<User>();
    public ShowInviteContactsDialog showInviteContactsDialog;
    public InviteLinkDialog inviteLinkDialog;

    private boolean isFabOpen = false;
    private boolean iamCreator = false;

    public ArrayList<Category> allCategories;

    private CategorylistRecyclerviewAdapter categorylistRecyclerviewAdapter;

    @NonConfigurationInstance
    @Bean
    DatabaseUtilityLearngroupView dataBaseUtilTask;

    @ViewById(R.id.learngroupview_actionbutton)
    FloatingActionButton learngroupview_actionbutton;

    @ViewById(R.id.learngroupview_actionbutton_addmember)
    FloatingActionButton fab_addmember;

    @ViewById(R.id.learngroupview_actionbutton_addcategorie)
    FloatingActionButton fab_addcategory;

    @ViewById(R.id.categoryrecyclerview)
    RecyclerView categoryrecyclerview;

    @SuppressLint("RestrictedApi")
    @AfterViews
    void onCreate(){
        if(getIntent().getExtras() != null){
            this.group = getIntent().getExtras().getParcelable("group");
        }

        Animation floatingactionanimation = AnimationUtils.loadAnimation(this, R.anim.floatingaction_onviewanim);


        if(PersistanceDataHandler.getUniqueDatabaseId().equals(this.group.getCreator().get_id())){
            this.iamCreator = true;
            fab_addmember.setVisibility(View.VISIBLE);
        }
        learngroupview_actionbutton.startAnimation(floatingactionanimation);

    }

    @Override
    protected void onResume() {
        super.onResume();
        dataBaseUtilTask.getCategories(this.group.get_id());

    }

    void initilizeRecyclerview(){
       // int animationID = R.anim.layout_animation_fall_down;
       // LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), animationID);


        categoryrecyclerview.setHasFixedSize(true);
        CategoryRecyclerlistLayoutManager gridlayoutmanager = new CategoryRecyclerlistLayoutManager(this, 500);
        categoryrecyclerview.setLayoutManager(gridlayoutmanager);


        this.categorylistRecyclerviewAdapter = new CategorylistRecyclerviewAdapter(this.allCategories);


        categoryrecyclerview.setAdapter(this.categorylistRecyclerviewAdapter);
        categoryrecyclerview.setVisibility(View.VISIBLE);
      //  categoryrecyclerview.setLayoutAnimation(animation);
    }

    @Click(R.id.learngroupview_actionbutton)
    void addGroupview(){

        if(!this.isFabOpen){
            this.isFabOpen = true;
            showFabMenu();
        }else{
            this.isFabOpen = false;
            closeFabMenu();
        }
    }

    private void showFabMenu(){
        if(this.iamCreator){
            this.fab_addmember.animate().translationY(-getResources().getDimension(R.dimen.fabmargin_1));
            this.fab_addcategory.animate().translationY(-getResources().getDimension(R.dimen.fabmargin_2));
            this.fab_addmember.animate().alpha(1);
            this.fab_addcategory.animate().alpha(1);
        }else{
            this.fab_addcategory.animate().translationY(-getResources().getDimension(R.dimen.fabmargin_1));
            this.fab_addcategory.animate().alpha(1);
        }

    }

    private void closeFabMenu(){
            this.fab_addmember.animate().translationY(0);
            this.fab_addcategory.animate().translationY(0);
            this.fab_addmember.animate().alpha(0);
            this.fab_addcategory.animate().alpha(0);
    }

    @Click(R.id.learngroupview_actionbutton_addmember)
    void addmember(){
        this.addMemberDialog = new AddMemberDialog_();
        this.addMemberDialog.setActivity(this);
        this.addMemberDialog.show(getSupportFragmentManager(), "addGroupMember");
    }

    @Click(R.id.learngroupview_actionbutton_addcategorie)
    void addcategory(){

    }



    @Override
    public void onCreateAddMemberDialogContactClick(DialogFragment dialog) {
        dataBaseUtilTask.getUsers();
    }

    @Override
    public void onCreateAddMemberDialogLinkClick(DialogFragment dialog) {
        this.addMemberDialog.dismiss();
        inviteLinkDialog = new InviteLinkDialog_();
        inviteLinkDialog.setVars(this, this.group.getGrouplink());
        inviteLinkDialog.show(getSupportFragmentManager(), "inviteUserLink");
    }

    @Override
    public void onInviteLinkDialogCopyButton(final DialogFragment dialog, TextView grouplink, Button copybutton) {
        String grouplinktext = this.group.getGrouplink();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(this.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Grouplink", grouplinktext);
        clipboard.setPrimaryClip(clip);
        copybutton.setEnabled(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
                Toast.makeText(LearngroupViewActivity.this, "Link kopiert", Toast.LENGTH_SHORT).show();
            }
        }, 800);
    }

    public void getUsersBack(User[] users){
        this.databaseUsers = users;
        getContactExec();
    }

    public void getCategoriesBack(Category[] categories){
        this.allCategories = new ArrayList<Category>(Arrays.asList(categories));
        initilizeRecyclerview();
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
            showInviteContactsDialog.setVars(this,this.contactUsersinDatabase, this.group, this.dataBaseUtilTask);
            showInviteContactsDialog.show(getSupportFragmentManager(), "inviteUserContacts");}
    }



}
