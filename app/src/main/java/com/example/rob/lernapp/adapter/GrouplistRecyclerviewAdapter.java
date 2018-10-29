package com.example.rob.lernapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rob.lernapp.DeleteGroupDialog_;
import com.example.rob.lernapp.LearngroupViewActivity_;
import com.example.rob.lernapp.R;
import com.example.rob.lernapp.ShowMemberDialog_;
import com.example.rob.lernapp.restdataGet.Learngroup;

import java.util.ArrayList;


public class GrouplistRecyclerviewAdapter extends RecyclerView.Adapter<GrouplistRecyclerviewAdapter.GroupViewHolder> {

    public ArrayList<Learngroup> groups;

    public static ShowMemberDialog_ showMemberDialog;
    public static DeleteGroupDialog_ deleteGroupDialog;
    public static Activity originactivity;
    public static FragmentManager originfragmentmanager;




    public void setGroupsNew(ArrayList<Learngroup> newGroup) {
        this.groups = newGroup;
    }


    public GrouplistRecyclerviewAdapter(ArrayList<Learngroup> learngroups, Activity activity, FragmentManager fragmentManager) {
        this.groups = learngroups;
        originactivity = activity;
        originfragmentmanager = fragmentManager;

    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grouplist_recyclerviewitems, viewGroup, false);
        GroupViewHolder groupViewHolder = new GroupViewHolder(view);
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder groupViewHolder, int i) {
        groupViewHolder.groupname.setText(groups.get(i).getName());
        groupViewHolder.creator.setText(groups.get(i).getCreator().getName());

        groupViewHolder.showmembers.setTag(i);
        groupViewHolder.deleteGroup.setTag(i);
        groupViewHolder.grouplistcardview.setTag(groups.get(i));

        groupViewHolder.grouplistcardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Learngroup group = (Learngroup) view.getTag();
                Intent selectedGroup = new Intent(originactivity, LearngroupViewActivity_.class);
                selectedGroup.putExtra("group", group);
                originactivity.startActivity(selectedGroup);
                //originactivity.finish();
            }
        });


        groupViewHolder.deleteGroup.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                deleteGroupDialog = new DeleteGroupDialog_();
                deleteGroupDialog.setActivity(originactivity);
                deleteGroupDialog.setGroupname(groups.get(position).getName());
                deleteGroupDialog.setdeletedGroup(groups.get(position));
                deleteGroupDialog.show(originfragmentmanager, "deletegroup");
            }
        });

        groupViewHolder.showmembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                showMemberDialog = new ShowMemberDialog_();
                showMemberDialog.setActivity(originactivity);
                showMemberDialog.setGroups(groups, position);
                if(groups.get(position).getMembers().length == 0){
                    Toast.makeText(originactivity.getBaseContext(), "Keine Mitglieder in: " + groups.get(position).getName(), Toast.LENGTH_SHORT).show();
                }else{
                    showMemberDialog.show(originfragmentmanager, "groupmember");
                }

            }
        });

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return this.groups.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        CardView grouplistcardview;
        TextView groupname;
        TextView creator;
        ImageView showmembers;
        ImageView deleteGroup;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            grouplistcardview = (CardView) itemView.findViewById(R.id.groupllist_cardview);
            groupname = (TextView) itemView.findViewById(R.id.groupname);
            creator = (TextView) itemView.findViewById(R.id.creator);
            showmembers = (ImageView) itemView.findViewById(R.id.show_member);
            deleteGroup = (ImageView) itemView.findViewById(R.id.delete_group);
        }
    }



}
