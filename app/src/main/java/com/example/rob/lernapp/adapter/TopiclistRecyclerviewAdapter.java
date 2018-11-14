package com.example.rob.lernapp.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rob.lernapp.R;
import com.example.rob.lernapp.restdataGet.Topic;

import java.util.ArrayList;

public class TopiclistRecyclerviewAdapter  extends RecyclerView.Adapter<TopiclistRecyclerviewAdapter.TopicViewHolder> {

    public ArrayList<Topic> topics;
    public static Activity originactivity;

    public TopiclistRecyclerviewAdapter(ArrayList<Topic> topics, Activity activity) {
        this.topics = topics;
        originactivity = activity;
    }

    public void setTopicNew(ArrayList<Topic> newTopic) {
        this.topics = newTopic;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.topiclist_recyclerviewitem, viewGroup, false);
        TopicViewHolder topicViewHolder = new TopicViewHolder(view);
        return topicViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TopiclistRecyclerviewAdapter.TopicViewHolder topicViewHolder, int i) {

        topicViewHolder.topicname.setText(this.topics.get(i).getName());

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return this.topics.size();
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder{

        CardView topiccardview;
        TextView topicname;
        LinearLayout linearlayout;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topiccardview = (CardView) itemView.findViewById(R.id.topiclist_cardView);
            topicname = (TextView) itemView.findViewById(R.id.topiclist_name);
            linearlayout = (LinearLayout) itemView.findViewById(R.id.topiclist_linearlayout);


        }
    }
}
