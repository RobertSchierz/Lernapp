package com.example.rob.lernapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

public class ConfirmGroupDialog extends DialogFragment {






    public interface ConfirmGroupDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    private Activity activity;
    ConfirmGroupDialogListener cgdListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            cgdListener = (ConfirmGroupDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstancesState){
        LayoutInflater inflater = this.activity.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        builder.setMessage(R.string.permission_dialog)
                .setView(inflater.inflate(R.layout.confirm_group_dialog_layout, null))
                .setPositiveButton(R.string.confirm_group_dialog_positiv, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cgdListener.onDialogPositiveClick(ConfirmGroupDialog.this);

                    }
                })
                .setNegativeButton(R.string.confirm_group_dialog_negativ, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cgdListener.onDialogNegativeClick(ConfirmGroupDialog.this);

                    }
                });

        return builder.create();
    }

    void showdialog(Activity activity){
        this.activity = activity;
        onCreateDialog(new Bundle()).show();


    }

}
