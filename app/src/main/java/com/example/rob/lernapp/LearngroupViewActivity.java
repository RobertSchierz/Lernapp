package com.example.rob.lernapp;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
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
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rob.lernapp.adapter.CategoryRecyclerlistLayoutManager;
import com.example.rob.lernapp.adapter.CategorylistRecyclerviewAdapter;
import com.example.rob.lernapp.databaseUtilityClasses.DatabaseUtilityLearngroupView;
import com.example.rob.lernapp.dialoge.AddCategoryDialog;
import com.example.rob.lernapp.dialoge.AddCategoryDialog_;
import com.example.rob.lernapp.dialoge.AddMemberDialog;
import com.example.rob.lernapp.dialoge.AddMemberDialog_;
import com.example.rob.lernapp.dialoge.InviteLinkDialog;
import com.example.rob.lernapp.dialoge.InviteLinkDialog_;
import com.example.rob.lernapp.dialoge.ShowInviteContactsDialog;
import com.example.rob.lernapp.dialoge.ShowInviteContactsDialog_;
import com.example.rob.lernapp.restDataPatch.PatchResponse;
import com.example.rob.lernapp.restdataGet.Category;
import com.example.rob.lernapp.restdataGet.Learngroup;
import com.example.rob.lernapp.restdataGet.User;
import com.example.rob.lernapp.restdataPost.PostResponseCategories;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
public class LearngroupViewActivity extends AppCompatActivity implements AddMemberDialog.AddMemberDialogListener, InviteLinkDialog.InviteLinkDialogListener, AddCategoryDialog.AddCategoryDialogListener {


    public Learngroup group;
    private AddMemberDialog addMemberDialog;
    private GetContacts getContacts;
    private User[] databaseUsers;
    public ArrayList<User> contactUsersinDatabase = new ArrayList<User>();
    public ShowInviteContactsDialog showInviteContactsDialog;
    public InviteLinkDialog inviteLinkDialog;
    public AddCategoryDialog addCategoryDialog;

    private boolean isFabOpen = false;
    private boolean iamCreator = false;

    public ArrayList<Category> allCategories;

    private CategorylistRecyclerviewAdapter categorylistRecyclerviewAdapter;

    Socket learnappSocket;
    Gson gsonhelper = new Gson();

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

        setTitle(getResources().getText(R.string.categoryactivitylabel) + " - " + this.group.getName());

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

        this.learnappSocket = SocketHandler.getInstance().getlearnappSocket();
        this.learnappSocket.connect();
        this.learnappSocket.on("groupMemberAdded", onMemberAddedToGroupLearngroupViewActivity);
        this.learnappSocket.on("groupMemberDeleted", onMemberLeaveGroupGroupLearngroupViewActivity);
        this.learnappSocket.on("categoryAdded", onCategoryAddedLearngroupViewActivity);
        this.learnappSocket.on("deletedGroup", onDeletedGroupLearngroupViewActivity);

    }

    @Override
    protected void onPause() {
        super.onPause();
        this.learnappSocket.off("groupMemberDeleted", onMemberAddedToGroupLearngroupViewActivity);
        this.learnappSocket.off("groupMemberAdded", onMemberLeaveGroupGroupLearngroupViewActivity);
        this.learnappSocket.off("categoryAdded", onCategoryAddedLearngroupViewActivity);
        this.learnappSocket.off("deletedGroup", onDeletedGroupLearngroupViewActivity);



    }

    private Emitter.Listener onDeletedGroupLearngroupViewActivity = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            backToGroups(getJsonObjectforSocketIO(args[0]));
        }
    };

    @UiThread
    public void backToGroups(JsonObject data) {
        Learngroup deletedLearngroup = this.gsonhelper.fromJson(data, Learngroup.class);
        if (this.group.get_id().equals(deletedLearngroup.get_id())) {
            Intent openLearngroups = new Intent(this, LearngroupsActivity_.class);
            openLearngroups.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(openLearngroups);
            this.finish();
        }

    }



    private Emitter.Listener onCategoryAddedLearngroupViewActivity = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (args[0] != null && args != null) {

                addNewCategory(getJsonObjectforSocketIO(args[0]), false);
            } else {
                addNewCategory(null, true);
            }

        }
    };

    @UiThread
    void addNewCategory(JsonObject data, boolean error){
        if(!error){
            PostResponseCategories newCategoryPostResponse = this.gsonhelper.fromJson(data, PostResponseCategories.class);
            initializeCategoryList(newCategoryPostResponse);

        }else{
            Toast.makeText(this, "Fehler beim Datenempfang (Neue Kategory)", Toast.LENGTH_LONG).show();
        }
    }

    private Emitter.Listener onMemberLeaveGroupGroupLearngroupViewActivity = new Emitter.Listener() {
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
            if(this.group.get_id().equals(deletedMemberPatchResponse.getGroup())){
                this.group.deleteMember(deletedMemberPatchResponse.getMember());
            }

        } else {
            Toast.makeText(this, "Fehler beim Datenempfang (Gelöschtes Member)", Toast.LENGTH_LONG).show();
        }

    }

    private Emitter.Listener onMemberAddedToGroupLearngroupViewActivity = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (args[0] != null && args != null) {
                handleNewMember(getJsonObjectforSocketIO(args[0]), false);
            } else {
                handleNewMember(null, true);
            }
        }
    };

    @UiThread
    void handleNewMember(JsonObject data, boolean error){

        if(!error){
            PatchResponse addMemberPatchResponse = this.gsonhelper.fromJson(data, PatchResponse.class);
            if(this.group.get_id().equals(addMemberPatchResponse.getGroup())){
                this.group.setNewMember(addMemberPatchResponse.getMember());
            }


        }else{
            Toast.makeText(this, "SocketIO Error beim Hinzufügen eines neuen Members", Toast.LENGTH_LONG).show();
        }

    }

    private JsonObject getJsonObjectforSocketIO(Object arg) {
        JsonParser parser = new JsonParser();
        return parser.parse(String.valueOf(arg)).getAsJsonObject();
    }




    void initilizeRecyclerview(){
        int animationID = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), animationID);


        categoryrecyclerview.setHasFixedSize(true);
        CategoryRecyclerlistLayoutManager gridlayoutmanager = new CategoryRecyclerlistLayoutManager(this, 400);
        categoryrecyclerview.setLayoutManager(gridlayoutmanager);


        this.categorylistRecyclerviewAdapter = new CategorylistRecyclerviewAdapter(this.allCategories, this);

        categoryrecyclerview.setAdapter(this.categorylistRecyclerviewAdapter);
        categoryrecyclerview.setVisibility(View.VISIBLE);
        categoryrecyclerview.setLayoutAnimation(animation);
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
        addCategoryDialog = new AddCategoryDialog_();
        addCategoryDialog.setActivity(this);
        addCategoryDialog.show(getSupportFragmentManager(), "addCategoryDialog");
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

    public void getNewCategoryBack(PostResponseCategories postResponseCategories){
        addCategoryDialog.dismiss();
        initializeCategoryList(postResponseCategories);

        //SocketIO Function
        String newCategoryJson = this.gsonhelper.toJson(postResponseCategories);
        this.learnappSocket.emit("CategoryAdded", newCategoryJson);


    }

    private void initializeCategoryList(PostResponseCategories postResponseCategories) {
        this.allCategories.add(postResponseCategories.getCreatedCategory());
        this.categorylistRecyclerviewAdapter.setCategoryNew(this.allCategories);
        this.categorylistRecyclerviewAdapter.notifyDataSetChanged();
        this.categoryrecyclerview.scheduleLayoutAnimation();
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


    public void examineUsers() {

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


    @Override
    public void onAddCategoryDialogPositiveClick(DialogFragment dialog, EditText categoryname) {
        dataBaseUtilTask.addNewCategory(this.group.get_id(), categoryname.getText().toString(),PersistanceDataHandler.getUniqueDatabaseId());
    }

    @Override
    public void onAddCategoryDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
