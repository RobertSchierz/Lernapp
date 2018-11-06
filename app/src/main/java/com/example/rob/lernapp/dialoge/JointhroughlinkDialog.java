package com.example.rob.lernapp.dialoge;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rob.lernapp.PersistanceDataHandler;
import com.example.rob.lernapp.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.join_throughlink_dialog)
public class JointhroughlinkDialog extends DialogFragment {


    public interface JointhroughlinkDialogListener {
        public void onJoinLinkDialogPositiveClick(DialogFragment dialog, String memberid, String link);

        public void onJoinLInkDialogNegativeClick(DialogFragment dialog);
    }

    private Activity activity;
    JointhroughlinkDialog.JointhroughlinkDialogListener jtlListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            jtlListener = (JointhroughlinkDialog.JointhroughlinkDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    @ViewById(R.id.join_link_textbox)
    EditText linkboxInput;


    @Click(R.id.joinlink_dialog_positivebutton)
    void positivButtonClicked() {

        if (linkboxInput.getText().toString() != null && !(linkboxInput.getText().toString().isEmpty())) {

            jtlListener.onJoinLinkDialogPositiveClick(JointhroughlinkDialog.this, PersistanceDataHandler.getUniqueDatabaseId(), linkboxInput.getText().toString());


        } else {
            Toast.makeText(this.activity, "Link ist leer", Toast.LENGTH_SHORT).show();
        }

    }

    @Click(R.id.joinlink_dialog_negativebutton)
    void negativButtonClicked() {
        jtlListener.onJoinLInkDialogNegativeClick(JointhroughlinkDialog.this);
    }


    public void setVars(Activity activity) {
        this.activity = activity;
    }

}
