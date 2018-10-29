package com.example.rob.lernapp;

import android.annotation.SuppressLint;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.rob.lernapp.restdataGet.Learngroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_learngroupview)
public class LearngroupViewActivity extends AppCompatActivity implements AddMemberDialog.AddMemberDialogListener {

    private Learngroup group;
    private AddMemberDialog addMemberDialog;

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

    }

    @Override
    public void onCreateAddMemberDialogLinkClick(DialogFragment dialog) {

    }
}
