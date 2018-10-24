package com.example.rob.lernapp;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rob.lernapp.restdata.Learngroup;
import com.example.rob.lernapp.restdata.Member;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EFragment(R.layout.group_memberlist)
public class ShowMemberDialog extends DialogFragment {


    public Activity learngroupactivity;
    Learngroup[] groups;
    int position;

    @ViewById(R.id.group_memberlistview)
    public ListView groupmemberlistview;


    @AfterViews
    void afterViews(){

                final ArrayList<Member> memberlist = new ArrayList<Member>();
                for(int i = 0; i < this.groups[position].getMembers().length; i++){
                    memberlist.add(this.groups[position].getMembers()[i]);
                }

                final MemberArrayAdapter stableArrayAdapter = new MemberArrayAdapter(this.learngroupactivity.getApplicationContext(), memberlist);
                groupmemberlistview.setAdapter(stableArrayAdapter);
    }


    public void setActivity(Activity activity){
        this.learngroupactivity = activity;

    }

    public void setGroups(Learngroup[] lerngroups, int position){
        this.groups = lerngroups;
        this.position = position;

    }

}

class MemberArrayAdapter extends ArrayAdapter<Member> {

    private final Context context;
    private final ArrayList<Member> values;

    public MemberArrayAdapter(Context context, ArrayList<Member> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.group_memberlist_item, parent, false);
        TextView textView_name = (TextView) rowView.findViewById(R.id.groupmember_name);
        TextView textView_role = (TextView) rowView.findViewById(R.id.groupmember_role);


        textView_name.setText(values.get(position).getMember().getName());
        textView_role.setText(values.get(position).getRole());


        return rowView;
    }

}
