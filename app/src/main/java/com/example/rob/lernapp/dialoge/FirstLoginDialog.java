package com.example.rob.lernapp.dialoge;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rob.lernapp.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fistlogin_dialog)
public class FirstLoginDialog extends DialogFragment {

    private Activity activity;
    FirstLoginDialog.FirstLoginDialogListener fldlistener;


    @ViewById(R.id.login_start)
    Button start;

    @ViewById(R.id.login_name)
    EditText loginName;

    @ViewById(R.id.login_phonenumber)
    EditText loginPhonenumber;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.setCancelable(false);

        try {
            fldlistener = (FirstLoginDialog.FirstLoginDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FirstLoginDialog");
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.start.setEnabled(false);
    }

    @TextChange(R.id.login_name)
    void nameChange(TextView v){
        if(!v.getText().toString().isEmpty()){
            this.start.setEnabled(true);
        }else{
            this.start.setEnabled(false);
        }
    }

    public interface FirstLoginDialogListener {
        public void FirstLoginDialogPositiveClick(DialogFragment dialog, String name, Integer phonenumber);
    }

    public void setVars(Activity activity) {
        this.activity = activity;
    }



    @Click(R.id.login_start)
    void positivButtonClicked() {

        Integer phonenumber;
        if(!this.loginPhonenumber.getText().toString().isEmpty()){
            phonenumber = Integer.valueOf(this.loginPhonenumber.getText().toString());

        }else{
            phonenumber = null;
        }

        fldlistener.FirstLoginDialogPositiveClick(FirstLoginDialog.this, this.loginName.getText().toString(), phonenumber);
    }
}
