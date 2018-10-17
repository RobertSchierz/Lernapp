package com.example.rob.lernapp;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_learngroups)
public class Learngroups extends AppCompatActivity implements ConfirmGroupDialog.ConfirmGroupDialogListener {



    @AfterViews
    void onCreate(){

    }

    @Click(R.id.learngroup_actionbutton)
    void actionbutton_clicked(){
        ConfirmGroupDialog confirmGroupDialog = new ConfirmGroupDialog();
        confirmGroupDialog.showdialog(this);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
    }
}
