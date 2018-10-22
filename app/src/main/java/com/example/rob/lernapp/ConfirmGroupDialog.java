package com.example.rob.lernapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.confirm_group_dialog_layout)
public class ConfirmGroupDialog extends DialogFragment {



    public interface ConfirmGroupDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, EditText groupname);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    private Activity activity;
    ConfirmGroupDialogListener cgdListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            cgdListener = (ConfirmGroupDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    @ViewById(R.id.confirm_group_dialog_input)
    EditText groupnameInput;


    @Click(R.id.confirm_group_dialog_positivebutton)
    void positivButtonClicked(){

        if(groupnameInput.getText().toString() != null && !(groupnameInput.getText().toString().isEmpty())){
            cgdListener.onDialogPositiveClick(ConfirmGroupDialog.this, groupnameInput);
        }else{
            Toast.makeText(this.activity, "Gruppennamen ist leer", Toast.LENGTH_SHORT).show();
        }

    }

    @Click(R.id.confirm_group_dialog_negativebutton)
    void negativButtonClicked(){
        cgdListener.onDialogNegativeClick(ConfirmGroupDialog.this);
    }




    void setActivity(Activity activity){
        this.activity = activity;

    }

}
