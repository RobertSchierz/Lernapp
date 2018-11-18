package com.example.rob.lernapp.adapter;

import android.app.Activity;
import android.media.MediaPlayer;
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
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.rob.lernapp.R;
import com.example.rob.lernapp.downloadclasses.Downloadhandler;
import com.example.rob.lernapp.restdataGet.Response;
import com.example.rob.lernapp.restdataGet.Topic;

import java.io.File;
import java.util.ArrayList;

public class TopiclistRecyclerviewAdapter extends RecyclerView.Adapter<TopiclistRecyclerviewAdapter.TopicViewHolder> {

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



    public void tester(String path, VideoView videoView){

        File file = new File(path);



        if(file != null){

            videoView.setVideoPath(file.getAbsolutePath());

            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    return false;
                }
            });



            MediaController mediaController = new MediaController(originactivity);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mp.setOnCompletionListener(null);
                }
            });




        }











    }

    public void setVideoToTopic(VideoView videoView, String contentURL) {

        String filePath = null;

        Downloadhandler downloadhandler = (Downloadhandler) new Downloadhandler(originactivity, this, videoView).execute(contentURL);
        filePath = downloadhandler.filePath;




    }

    @Override
    public void onBindViewHolder(@NonNull TopiclistRecyclerviewAdapter.TopicViewHolder topicViewHolder, int i) {

        topicViewHolder.topicname.setText(this.topics.get(i).getName());
        topicViewHolder.topiccreator.setText("~ " + this.topics.get(i).getCreator().getName());

        String mediatype = this.topics.get(i).getMediatype();

        switch (mediatype) {
            case "text":
                topicViewHolder.linearLayout_media.setVisibility(View.GONE);
                break;

            case "video":
                setVideoToTopic(topicViewHolder.topicvideo, this.topics.get(i).getContenturl());
                topicViewHolder.topicvideo.setVisibility(View.VISIBLE);



        }

        if (this.topics.get(i).getType().equals("question")) {
            topicViewHolder.topictype.setText("#Frage");
        } else if (this.topics.get(i).getType().equals("explanation")) {
            topicViewHolder.topictype.setText("#Erklärung");
        }

        if (!this.topics.get(i).getText().isEmpty()) {
            topicViewHolder.topictext.setText(this.topics.get(i).getText());
        }

        int animationID = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(originactivity, animationID);

        if (topicViewHolder.responserecyclerview != null) {
            topicViewHolder.responserecyclerview.setHasFixedSize(true);
            LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(originactivity, LinearLayoutManager.VERTICAL, false);
            topicViewHolder.responserecyclerview.setLayoutManager(verticalLayoutManager);


            this.responseRecyclerlistAdapter = new ResponseRecyclerlistAdapter(this.responses, this.topics.get(i), originactivity);

            topicViewHolder.responserecyclerview.setAdapter(this.responseRecyclerlistAdapter);


            topicViewHolder.responserecyclerview.setVisibility(View.VISIBLE);
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

    public static class TopicViewHolder extends RecyclerView.ViewHolder {

        CardView topiccardview;
        TextView topicname;
        TextView topiccreator;
        TextView topictype;
        TextView topictext;
        LinearLayout linearlayout;
        LinearLayout linearLayout_media;
        RecyclerView responserecyclerview;
        VideoView topicvideo;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topiccardview = (CardView) itemView.findViewById(R.id.topiclist_cardView);
            topicname = (TextView) itemView.findViewById(R.id.topiclist_name);
            topiccreator = (TextView) itemView.findViewById(R.id.topiclist_creator);
            topictype = (TextView) itemView.findViewById(R.id.topiclist_type);
            topictext = (TextView) itemView.findViewById(R.id.topiclist_text);
            linearlayout = (LinearLayout) itemView.findViewById(R.id.topiclist_linearlayout);
            linearLayout_media = (LinearLayout) itemView.findViewById(R.id.topiclist_linearlayout_media);
            responserecyclerview = (RecyclerView) itemView.findViewById(R.id.responserecyclerview);
            topicvideo = (VideoView) itemView.findViewById(R.id.topiclist_media_videoview);


        }
    }
}
