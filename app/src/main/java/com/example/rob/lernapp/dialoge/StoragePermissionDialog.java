package com.example.rob.lernapp.dialoge;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.widget.CheckBox;

import com.example.rob.lernapp.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.storage_permission_dialog)
public class StoragePermissionDialog extends DialogFragment {


    public interface StoragePermissionDialogListener {
        public void StoragePermissionDialogPositiveClick(DialogFragment dialog);

        public void StoragePermissionDialogNegativeClick(DialogFragment dialog, CheckBox dontAskAgain);
    }

    private Activity activity;
    StoragePermissionDialog.StoragePermissionDialogListener spdlistener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.setCancelable(false);

        try {
            spdlistener = (StoragePermissionDialog.StoragePermissionDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @ViewById(R.id.storagepermission_stream_checkbox)
    CheckBox dontAskAgain;

    @Click(R.id.storagepermission_positivebutton)
    void positivButtonClicked() {

        spdlistener.StoragePermissionDialogPositiveClick(StoragePermissionDialog.this);
    }

    @Click(R.id.storagepermission_negativebutton)
    void negativButtonClicked() {
        spdlistener.StoragePermissionDialogNegativeClick(StoragePermissionDialog.this, dontAskAgain);
    }


    public void setVars(Activity activity) {
        this.activity = activity;
    }


}
