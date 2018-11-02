package com.example.rob.lernapp;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rob.lernapp.databaseUtilityClasses.DatabaseUtilityLearngroupView;
import com.example.rob.lernapp.restDataPatch.PatchResponse;
import com.example.rob.lernapp.restdataGet.Learngroup;
import com.example.rob.lernapp.restdataGet.User;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EFragment(R.layout.invite_contactlist)
public class ShowInviteContactsDialog extends DialogFragment {


    public static Activity learngroupviewactivity;
    private ArrayList<User> users;
    private Learngroup group;
    private DatabaseUtilityLearngroupView databaseUtilityLearngroupView;


    @ViewById(R.id.invite_contactlist)
    public ListView contactinvitelistview;


    @AfterViews
    void afterViews() {

        final ContactInviteDialogAdapter stableArrayAdapter = new ContactInviteDialogAdapter(this.group, learngroupviewactivity.getApplicationContext(), users, this.databaseUtilityLearngroupView);
        contactinvitelistview.setAdapter(stableArrayAdapter);

    }

    public void setVars(Activity activity, ArrayList<User> users, Learngroup group, DatabaseUtilityLearngroupView databaseUtilityLearngroupView) {
        learngroupviewactivity = activity;
        this.users = users;
        this.group = group;
        this.databaseUtilityLearngroupView = databaseUtilityLearngroupView;
    }


    public void getNewMemberGroupResponse(PatchResponse patchResponse, Button addMemberButton) {
        String patchResponseMessage = patchResponse.getMessage();
        this.group.setNewMember(patchResponse.getMember());
        addMemberButton.setEnabled(false);
        addMemberButton.setText("Hinzugefügt");
        
    }
}

class ContactInviteDialogAdapter extends ArrayAdapter<User> {

    private final Context context;
    private final ArrayList<User> values;
    private Learngroup group;
    private DatabaseUtilityLearngroupView databaseUtilityLearngroupView;


    public ContactInviteDialogAdapter(Learngroup group, Context context, ArrayList<User> values, DatabaseUtilityLearngroupView databaseUtilityLearngroupView) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.group = group;
        this.databaseUtilityLearngroupView = databaseUtilityLearngroupView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.invite_contactlist_item, parent, false);
        TextView textView_name = (TextView) rowView.findViewById(R.id.invitecontact_name);
        Button button_add = rowView.findViewById(R.id.contact_invite_button);


        button_add.setTag(values.get(position));


        textView_name.setText(values.get(position).getName());
        button_add.setOnClickListener(contactInviteButtonOnClickListener);


        return rowView;
    }

    public View.OnClickListener contactInviteButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            User user = (User) view.getTag();
            Button addButton = (Button) view;
            databaseUtilityLearngroupView.postNewMemberToGroup(user.get_id(), group.get_id(), addButton);
        }
    };


}
