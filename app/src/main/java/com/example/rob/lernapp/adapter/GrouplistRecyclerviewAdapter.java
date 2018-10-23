package com.example.rob.lernapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GrouplistRecyclerviewAdapter extends RecyclerView.Adapter<GrouplistRecyclerviewAdapter.GroupViewHolder>{

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder groupViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder{

        CardView grouplistcardview;
        TextView groupname;
        TextView creator;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
