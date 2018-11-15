package com.example.rob.lernapp.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rob.lernapp.R;
import com.example.rob.lernapp.restdataGet.Response;
import com.example.rob.lernapp.restdataGet.Topic;

import java.util.ArrayList;

public class TopiclistRecyclerviewAdapter  extends RecyclerView.Adapter<TopiclistRecyclerviewAdapter.TopicViewHolder> {

    public ArrayList<Topic> topics;
    public static Activity originactivity;
    public ArrayList<Response> responses;
    private ResponseRecyclerlistAdapter responseRecyclerlistAdapter;

    public TopiclistRecyclerviewAdapter(ArrayList<Topic> topics, ArrayList<Response> responses, Activity activity) {
        this.topics = topics;
        this.responses = responses;
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
        topicViewHolder.topiccreator.setText( "~ " + this.topics.get(i).getCreator().getName());

        if(this.topics.get(i).getType().equals("question")){
            topicViewHolder.topictype.setText("#Frage");
        }else if(this.topics.get(i).getType().equals("explanation")){
            topicViewHolder.topictype.setText("#Erklärung");
        }

        if(!this.topics.get(i).getText().isEmpty()){
            topicViewHolder.topictext.setText(this.topics.get(i).getText());
        }

        int animationID = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(originactivity, animationID);

        if( topicViewHolder.responserecyclerview != null){
            topicViewHolder.responserecyclerview.setHasFixedSize(true);
            LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(originactivity, LinearLayoutManager.VERTICAL, false);
            topicViewHolder.responserecyclerview.setLayoutManager(verticalLayoutManager);


            this.responseRecyclerlistAdapter = new ResponseRecyclerlistAdapter(this.responses, this.topics.get(i) ,originactivity);

            topicViewHolder.responserecyclerview.setAdapter(this.responseRecyclerlistAdapter);


            topicViewHolder. responserecyclerview.setVisibility(View.VISIBLE);
            topicViewHolder.responserecyclerview.setLayoutAnimation(animation);
        }

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
        TextView topiccreator;
        TextView topictype;
        TextView topictext;
        LinearLayout linearlayout;
        RecyclerView responserecyclerview;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topiccardview = (CardView) itemView.findViewById(R.id.topiclist_cardView);
            topicname = (TextView) itemView.findViewById(R.id.topiclist_name);
            topiccreator = (TextView) itemView.findViewById(R.id.topiclist_creator);
            topictype = (TextView) itemView.findViewById(R.id.topiclist_type);
            topictext = (TextView) itemView.findViewById(R.id.topiclist_text);
            linearlayout = (LinearLayout) itemView.findViewById(R.id.topiclist_linearlayout);
            responserecyclerview = (RecyclerView) itemView.findViewById(R.id.responserecyclerview);


        }
    }
}
