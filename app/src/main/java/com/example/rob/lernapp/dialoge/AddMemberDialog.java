package com.example.rob.lernapp.dialoge;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;

import com.example.rob.lernapp.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.add_member_dialog)
public class AddMemberDialog extends DialogFragment  {



    private Activity activity;
    AddMemberDialogListener amdListener;


    public interface AddMemberDialogListener {
        public void onCreateAddMemberDialogContactClick(DialogFragment dialog);
        public void onCreateAddMemberDialogLinkClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            amdListener = (AddMemberDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Click(R.id.addmemberdialog_contact)
    void contactImageClick(){
        amdListener.onCreateAddMemberDialogContactClick(AddMemberDialog.this);
    }

    @Click(R.id.addmemberdialog_link)
    void linkImageClick(){
        amdListener.onCreateAddMemberDialogLinkClick(AddMemberDialog.this);
    }


    public void setActivity(Activity activity) {
        this.activity = activity;
    }

}
