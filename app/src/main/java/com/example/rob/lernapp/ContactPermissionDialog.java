package com.example.rob.lernapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ContactPermissionDialog extends DialogFragment {

    private static ContactPermissionDialog instance = null;
    private Activity activity;

    static public ContactPermissionDialog getInstance() {
        if (instance == null) {
            instance = new ContactPermissionDialog();
            return instance;
        } else {
            return instance;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstancesState){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        builder.setMessage(R.string.permission_dialog)
                .setPositiveButton(R.string.permission_dialog_positiv, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GetContacts.getInstance(getActivity()).handleDialog(true);
                    }
                })
                .setNegativeButton(R.string.permission_dialog_negativ, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GetContacts.getInstance(getActivity()).handleDialog(false);
                    }
                });

        return builder.create();
    }

    void showdialog(Activity activity){
        this.activity = activity;
        instance.onCreateDialog(new Bundle()).show();
    }
}
