package com.example.rob.lernapp.dialoge;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rob.lernapp.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.confirm_group_dialog_layout)
public class ConfirmGroupDialog extends DialogFragment {



    public interface ConfirmGroupDialogListener {
        public void onCreateGroupDialogPositiveClick(DialogFragment dialog, EditText groupname);
        public void onCreateGroupDialogNegativeClick(DialogFragment dialog);
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
            cgdListener.onCreateGroupDialogPositiveClick(ConfirmGroupDialog.this, groupnameInput);
        }else{
            Toast.makeText(this.activity, "Gruppennamen ist leer", Toast.LENGTH_SHORT).show();
        }

    }

    @Click(R.id.confirm_group_dialog_negativebutton)
    void negativButtonClicked(){
        cgdListener.onCreateGroupDialogNegativeClick(ConfirmGroupDialog.this);
    }




    public void setActivity(Activity activity){
        this.activity = activity;

    }

}
