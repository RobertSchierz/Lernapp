package com.example.rob.lernapp.dialoge;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rob.lernapp.R;
import com.example.rob.lernapp.SocketHandler;
import com.example.rob.lernapp.databaseUtilityClasses.DatabaseUtilityLearngroupView;
import com.example.rob.lernapp.restDataPatch.PatchResponse;
import com.example.rob.lernapp.restdataGet.Learngroup;
import com.example.rob.lernapp.restdataGet.User;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EFragment(R.layout.invite_contactlist)
public class ShowInviteContactsDialog extends DialogFragment {


    public static Activity learngroupviewactivity;
    private ArrayList<User> users;
    private Learngroup group;
    private DatabaseUtilityLearngroupView databaseUtilityLearngroupView;

    private Socket learnappSocket;
    private Gson gsonhelper = new Gson();


    @ViewById(R.id.invite_contactlist)
    public ListView contactinvitelistview;


    @AfterViews
    void afterViews() {


        setList();

        this.learnappSocket = SocketHandler.getInstance().getlearnappSocket();
        this.learnappSocket.connect();
        this.learnappSocket.on("groupMemberDeleted", onMemberLeaveGroupInviteContactDilaog);
        this.learnappSocket.on("groupMemberAdded", onMemberAddedGroupInviteContactDilaog);


    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        this.learnappSocket.off("groupMemberDeleted", onMemberLeaveGroupInviteContactDilaog);
        this.learnappSocket.off("groupMemberAdded", onMemberAddedGroupInviteContactDilaog);
        super.onDismiss(dialog);

    }

    private void setList() {

        if (users.size() == 0) {
            this.dismiss();
            Toast.makeText(learngroupviewactivity, "Der letzte potentielle Member ist soeben der Gruppe hinzugekommen", Toast.LENGTH_LONG).show();
        } else {
            final ContactInviteDialogAdapter stableArrayAdapter = new ContactInviteDialogAdapter(this.group, learngroupviewactivity.getApplicationContext(), users, this.databaseUtilityLearngroupView);
            if (contactinvitelistview != null) {
                contactinvitelistview.setAdapter(stableArrayAdapter);
            }

        }

    }

    private Emitter.Listener onMemberAddedGroupInviteContactDilaog = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (args[0] != null && args != null) {
                JsonParser parser = new JsonParser();
                handleMemberlist(parser.parse(String.valueOf(args[0])).getAsJsonObject(), false, false, false);
            } else {
                handleMemberlist(null, true, false, true);
            }
        }
    };

    private Emitter.Listener onMemberLeaveGroupInviteContactDilaog = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (args[0] != null && args != null) {
                JsonParser parser = new JsonParser();
                handleMemberlist(parser.parse(String.valueOf(args[0])).getAsJsonObject(), false, false, true);
            } else {
                handleMemberlist(null, true, false, true);
            }
        }
    };

    @UiThread
    void handleMemberlist(JsonObject data, boolean error, boolean listempty, boolean memberLeaved) {
        if (error) {
            Toast.makeText(learngroupviewactivity, "SocketIO Error (Fehler beim Handling der Member)", Toast.LENGTH_LONG).show();
        } else {
            PatchResponse patchResponse = this.gsonhelper.fromJson(data, PatchResponse.class);
            if (memberLeaved) {
                boolean isIn = false;
                for (User user :
                        this.users) {
                    if (user.get_id().equals(patchResponse.getMember().getMember().get_id())) {
                        isIn = true;
                    }
                }
                if(!isIn){
                    this.users.add(patchResponse.getMember().getMember());
                }

            } else {
                for (User user :
                        this.users) {
                    if (user.get_id().equals(patchResponse.getMember().getMember().get_id())) {
                        this.users.remove(user);
                        break;
                    }
                }
            }
            setList();
        }

    }


    public void setVars(Activity activity, ArrayList<User> users, Learngroup group, DatabaseUtilityLearngroupView databaseUtilityLearngroupView) {
        learngroupviewactivity = activity;
        this.users = users;
        this.group = group;
        this.databaseUtilityLearngroupView = databaseUtilityLearngroupView;
    }


    public void getNewMemberGroupResponse(PatchResponse patchResponse, Button addMemberButton) {
        this.group.setNewMember(patchResponse.getMember());
        addMemberButton.setEnabled(false);
        addMemberButton.setText("Hinzugefügt");

        //SocketIO Function
        String deletedGroupJson = this.gsonhelper.toJson(patchResponse);
        this.learnappSocket.emit("GroupMemberAdded", deletedGroupJson);

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
