package com.example.rob.lernapp.dialoge;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.TextView;

import com.example.rob.lernapp.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.invite_link)
public class InviteLinkDialog extends DialogFragment {



    public interface InviteLinkDialogListener {
        public void onInviteLinkDialogCopyButton(DialogFragment dialog, TextView grouplink, Button copybutton);

    }

    InviteLinkDialogListener ildlistener;
    private Activity activity;
    private String grouplink;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            ildlistener = (InviteLinkDialog.InviteLinkDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @ViewById(R.id.invitemember_link_linkbox)
    TextView linkbox;

    @ViewById(R.id.invitemember_link_copybutton)
    Button copybutton;

    @Click(R.id.invitemember_link_copybutton)
    void clickCopyButton(){
        ildlistener.onInviteLinkDialogCopyButton(InviteLinkDialog.this, linkbox, copybutton);
    }

    public void setVars(Activity activity, String link){
        this.activity = activity;
        this.grouplink = link;
    }

    @AfterViews
    void uiCreate(){
        linkbox.setText(this.grouplink);
    }

}
