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
import android.widget.Toast;

import com.example.rob.lernapp.restdataGet.User;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import static com.example.rob.lernapp.ShowInviteContactsDialog.learngroupviewactivity;

@EFragment(R.layout.invite_contactlist)
public class ShowInviteContactsDialog extends DialogFragment {


    public static Activity learngroupviewactivity;
    ArrayList<User> users;


    @ViewById(R.id.invite_contactlist)
    public ListView contactinvitelistview;


    @AfterViews
    void afterViews() {

        final ContactInviteDialogAdapter stableArrayAdapter = new ContactInviteDialogAdapter(learngroupviewactivity.getApplicationContext(), users);
        contactinvitelistview.setAdapter(stableArrayAdapter);

    }


    public void setActivity(Activity activity) {
        learngroupviewactivity = activity;

    }

    public void setContactUsers(ArrayList<User> users) {
        this.users = users;

    }

}

class ContactInviteDialogAdapter extends ArrayAdapter<User> {

    private final Context context;
    private final ArrayList<User> values;

    public ContactInviteDialogAdapter(Context context, ArrayList<User> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.invite_contactlist_item, parent, false);
        TextView textView_name = (TextView) rowView.findViewById(R.id.invitecontact_name);
        Button button_add = (Button) rowView.findViewById(R.id.contact_invite_button);
        button_add.setTag(values.get(position));


        textView_name.setText(values.get(position).getName());
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = (User) view.getTag();
                Toast.makeText(learngroupviewactivity, user.getName(), Toast.LENGTH_SHORT).show();
            }
        });


        return rowView;
    }

}
