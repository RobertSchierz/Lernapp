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
import com.example.rob.lernapp.restdataGet.Response;
import com.example.rob.lernapp.restdataGet.Topic;

import java.util.ArrayList;

public class ResponseRecyclerlistAdapter extends RecyclerView.Adapter<ResponseRecyclerlistAdapter.ResponseViewHolder> {

    public ArrayList<Response> responses;
    public ArrayList<Response> currentResponses = new ArrayList<Response>();
    public static Activity originactivity;
    public Topic topic;

    public ResponseRecyclerlistAdapter(ArrayList<Response> responses, Topic topic, Activity activity) {
        this.responses = responses;
        this.topic = topic;
        originactivity = activity;

    }

    public void setResponsesNew(ArrayList<Response> newResponse) {
        this.responses = newResponse;
    }

    @NonNull
    @Override
    public ResponseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.responselist_recyclerviewitem, viewGroup, false);
        ResponseViewHolder responseViewHolder = new ResponseViewHolder(view);
        return responseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ResponseRecyclerlistAdapter.ResponseViewHolder responseViewHolder, int i) {

        if(this.currentResponses.size() != 0){
            responseViewHolder.responsemediatype.setText(this.currentResponses.get(i).getCreator().getName());
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {

        if(this.responses.size() != 0){
            for (Response response : this.responses) {
                if (response.getTopic().get_id().equals(this.topic.get_id()) && !this.currentResponses.contains(response)) {
                    this.currentResponses.add(response);
                }
            }
        }

        return this.currentResponses.size();
    }

    public static class ResponseViewHolder extends RecyclerView.ViewHolder {

        CardView responsecardview;
        TextView responsemediatype;
        LinearLayout linearlayout;

        public ResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            responsecardview = (CardView) itemView.findViewById(R.id.responselist_cardView);
            responsemediatype = (TextView) itemView.findViewById(R.id.responselist_creatorname);
            linearlayout = (LinearLayout) itemView.findViewById(R.id.responselist_relativeLayout);


        }
    }
}
