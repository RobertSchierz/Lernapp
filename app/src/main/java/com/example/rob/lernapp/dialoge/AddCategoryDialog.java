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

@EFragment(R.layout.add_category_dialog)
public class AddCategoryDialog extends DialogFragment {


    public interface AddCategoryDialogListener {
        public void onAddCategoryDialogPositiveClick(DialogFragment dialog, EditText categoryname);
        public void onAddCategoryDialogNegativeClick(DialogFragment dialog);
    }
    private Activity activity;
    AddCategoryDialogListener acdListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            acdListener = (AddCategoryDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    @ViewById(R.id.add_category_dialog_input)
    EditText categoryInput;


    @Click(R.id.add_category_dialog_positivebutton)
    void positivButtonClicked(){

        if(categoryInput.getText().toString() != null && !(categoryInput.getText().toString().isEmpty())){
            acdListener.onAddCategoryDialogPositiveClick(AddCategoryDialog.this, categoryInput);
        }else{
            Toast.makeText(this.activity, "Gruppennamen ist leer", Toast.LENGTH_SHORT).show();
        }

    }

    @Click(R.id.add_category_dialog_negativebutton)
    void negativButtonClicked(){
        acdListener.onAddCategoryDialogNegativeClick(AddCategoryDialog.this);
    }




    public void setActivity(Activity activity){
        this.activity = activity;

    }



}
