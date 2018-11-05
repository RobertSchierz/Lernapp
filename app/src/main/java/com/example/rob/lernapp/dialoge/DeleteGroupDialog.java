package com.example.rob.lernapp.dialoge;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

import com.example.rob.lernapp.R;
import com.example.rob.lernapp.restdataGet.Learngroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.delete_group_dialog_layout)
public class DeleteGroupDialog extends DialogFragment {


    public interface DeleteGroupDialogListener {
        public void onDeleteGroupDialogPositiveClick(DialogFragment dialog, DeleteGroupDialog deleteGroupDialog, Learngroup deletedGroup);
        public void onDeleteGroupDialogNegativeClick(DialogFragment dialog, DeleteGroupDialog deleteGroupDialog);
    }
    private Activity activity;
    DeleteGroupDialog.DeleteGroupDialogListener dgdListener;
    private Learngroup deletedGroup;

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    @AfterViews
    void onCreate(){
        deleteGroupname.setText(this.groupname);
    }

    private String groupname;

    @ViewById(R.id.delete_group_dialog_groupname)
    TextView deleteGroupname;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dgdListener = (DeleteGroupDialog.DeleteGroupDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    @Click(R.id.delete_group_dialog_positivebutton)
    void positivButtonClicked(){
        dgdListener.onDeleteGroupDialogPositiveClick(DeleteGroupDialog.this, this, this.deletedGroup);
    }

    @Click(R.id.delete_group_dialog_negativebutton)
    void negativButtonClicked(){
        dgdListener.onDeleteGroupDialogNegativeClick(DeleteGroupDialog.this, this);
    }


    public void setdeletedGroup(Learngroup deletedGroup){
        this.deletedGroup = deletedGroup;

    }

    public void setActivity(Activity activity){
        this.activity = activity;

    }



}
