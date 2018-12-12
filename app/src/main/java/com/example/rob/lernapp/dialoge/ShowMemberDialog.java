
package com.example.rob.lernapp.dialoge;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rob.lernapp.R;
import com.example.rob.lernapp.SocketHandler;
import com.example.rob.lernapp.restdataGet.Learngroup;
import com.example.rob.lernapp.restdataGet.Member;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EFragment(R.layout.group_memberlist)
public class ShowMemberDialog extends DialogFragment {


    public Activity learngroupactivity;
    ArrayList<Learngroup> groups;
    int position;

    @ViewById(R.id.group_memberlistview)
    public ListView groupmemberlistview;

    private Socket learnappSocket;


    @AfterViews
    void afterViews() {

        setMemberlist();

        this.learnappSocket = SocketHandler.getInstance().getlearnappSocket();
        this.learnappSocket.connect();
        this.learnappSocket.on("groupMemberDeleted", onMemberLeaveGroupShowMember);
        this.learnappSocket.on("groupMemberAdded", onMemberAddedGroupShowMember);



    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        this.learnappSocket.off("groupMemberDeleted", onMemberLeaveGroupShowMember);
        this.learnappSocket.off("groupMemberAdded", onMemberAddedGroupShowMember);
        super.onDismiss(dialog);


    }



    private void setMemberlist() {
        final ArrayList<Member> memberlist = new ArrayList<Member>();

        for (int i = 0; i < this.groups.get(position).getMembers().length; i++) {
            memberlist.add(this.groups.get(position).getMembers()[i]);
        }

        if(memberlist.size() == 0){
            handleMemberlist(false, true);
        }else{
            final MemberArrayAdapter stableArrayAdapter = new MemberArrayAdapter(this.learngroupactivity.getApplicationContext(), memberlist);
            if(groupmemberlistview != null){
                groupmemberlistview.setAdapter(stableArrayAdapter);
            }
        }


    }


    private Emitter.Listener onMemberAddedGroupShowMember = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (args[0] != null && args != null) {

                handleMemberlist(false, false);
            } else {
                handleMemberlist(true, false);
            }
        }
    };

    private Emitter.Listener onMemberLeaveGroupShowMember = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (args[0] != null && args != null) {

                handleMemberlist(false, false);
            } else {
                handleMemberlist(true, false);
            }
        }
    };

    @UiThread
    void handleMemberlist(boolean error, boolean listempty){
        if(error){
            Toast.makeText(learngroupactivity, "SocketIO Error (Fehler beim Handling der Member)", Toast.LENGTH_LONG).show();
        }else{
            if(listempty){
                this.dismiss();
                Toast.makeText(learngroupactivity, "Der letzte Member hat soeben die Gruppe verlassen", Toast.LENGTH_LONG).show();
            }else{
                setMemberlist();
            }
        }

    }

    public void setActivity(Activity activity) {
        this.learngroupactivity = activity;

    }

    public void setGroups(ArrayList<Learngroup> lerngroups, int position) {
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
