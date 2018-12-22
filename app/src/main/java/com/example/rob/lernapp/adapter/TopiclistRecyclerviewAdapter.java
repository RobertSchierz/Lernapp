package com.example.rob.lernapp.adapter;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.rob.lernapp.CategoryViewActivity;
import com.example.rob.lernapp.ImageviewPreviewActivity_;
import com.example.rob.lernapp.NewContentActivity_;
import com.example.rob.lernapp.PersistanceDataHandler;
import com.example.rob.lernapp.R;
import com.example.rob.lernapp.downloadclasses.DownloadImagehandler;
import com.example.rob.lernapp.downloadclasses.Downloadhandler;
import com.example.rob.lernapp.restdataGet.Category;
import com.example.rob.lernapp.restdataGet.Response;
import com.example.rob.lernapp.restdataGet.Topic;

import java.util.ArrayList;

public class TopiclistRecyclerviewAdapter extends RecyclerView.Adapter<TopiclistRecyclerviewAdapter.TopicViewHolder> {

    public ArrayList<Topic> topics;
    public static CategoryViewActivity originactivity;
    public ArrayList<Response> responses;
    public ResponseRecyclerlistAdapter responseRecyclerlistAdapter;


    public TopiclistRecyclerviewAdapter(ArrayList<Topic> topics, ArrayList<Response> responses, CategoryViewActivity activity) {
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

    @UiThread
    public void notEnoughSpace(Button streamingButton) {
        streamingButton.setVisibility(View.VISIBLE);
        Toast.makeText(originactivity, "Nicht genug Speicherplatz für die Medien frei.", Toast.LENGTH_LONG).show();
    }


    @UiThread
    public void setMediaPath(String path, ProgressBar circlebar, VideoView videoView, ImageView imageView, String contentURL) {

        if (imageView != null && videoView == null) {
            try {
                Bitmap image = BitmapFactory.decodeFile(path);
                int newheight = (int) (image.getHeight() * (512.0 / image.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(image, 512, newheight, true);
                imageView.setImageBitmap(scaled);
                setImagePreview(imageView, this.responseRecyclerlistAdapter.topic.getCategory(), false, path);
            } catch (Exception e) {
                Bitmap image = BitmapFactory.decodeFile(contentURL);
                int newheight = (int) (image.getHeight() * (512.0 / image.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(image, 512, newheight, true);
                imageView.setImageBitmap(scaled);
            }

            imageView.setVisibility(View.VISIBLE);
            circlebar.setVisibility(View.GONE);

        } else {
            try {
                videoView.setVideoPath(path);
                finishVideoView(videoView, circlebar);
            } catch (Exception e) {
                Log.v("SetVideoPathError", e.getMessage());
                videoView.setVideoPath(contentURL);
                finishVideoView(videoView, circlebar);
            }
        }
    }

    private void finishVideoView(final VideoView videoView, ProgressBar circlebar) {

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return false;
            }
        });


        final MediaController mediaController = new MediaController(originactivity);
        videoView.setTag(R.string.mediacontroller, mediaController);


        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
            }
        });


        circlebar.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);

    }

    public void setMediaToTopic(VideoView videoView, ImageView imageView, Button streamingButton, ProgressBar circlebar, String contentURL, int option, Topic topic) {


        switch (option) {
            case 1:
                if (!originactivity.streamContent) {
                    Downloadhandler downloadhandler = (Downloadhandler) new Downloadhandler(originactivity, circlebar, this, videoView, null, streamingButton);
                    downloadhandler.execute(contentURL);
                } else {
                    videoView.setVideoPath(PersistanceDataHandler.getApiRootURL() + contentURL);
                    finishVideoView(videoView, circlebar);
                }
                break;

            case 2:


                if (!originactivity.streamContent) {
                    Downloadhandler downloadhandler = (Downloadhandler) new Downloadhandler(originactivity, circlebar, this, videoView, null, streamingButton);
                    downloadhandler.execute(contentURL);
                } else {
                    videoView.setVideoPath(PersistanceDataHandler.getApiRootURL() + contentURL);
                    finishVideoView(videoView, circlebar);
                }

                break;

            case 3:
                if (!originactivity.streamContent) {
                    Downloadhandler downloadhandler = (Downloadhandler) new Downloadhandler(originactivity, circlebar, this, null, imageView, streamingButton);
                    downloadhandler.execute(contentURL);
                } else {
                    DownloadImagehandler downloadImagehandler = new DownloadImagehandler(this, imageView);
                    downloadImagehandler.execute(contentURL);
                    circlebar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    setImagePreview(imageView, topic.getCategory(), true, topic.getContenturl());
                }
                break;
        }


    }

    private void setImagePreview(ImageView imageView, Category category, final Boolean isStreamed, String contenturl) {
        final Category imagepreviewCategory = category;

        String tempPath = "";
        if(isStreamed){
            tempPath = PersistanceDataHandler.getApiRootURL() + contenturl;
        }else{
            tempPath = contenturl;
        }

        final String imagepreviewPath = tempPath;

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent previewImageIntent = new Intent(v.getContext(), ImageviewPreviewActivity_.class);

                Bundle extras = new Bundle();


                extras.putParcelable("group", imagepreviewCategory.getGroup());
                extras.putString("imagepath", imagepreviewPath);
                if(isStreamed){
                    extras.putBoolean("isStreamed", true);
                }else{
                    extras.putBoolean("isStreamed", false);
                }
                extras.putBoolean("fromNewContent", false);
                originactivity.resumedFromImagepreview = true;
                previewImageIntent.putExtras(extras);
                v.getContext().startActivity(previewImageIntent);

            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull final TopiclistRecyclerviewAdapter.TopicViewHolder topicViewHolder, final int i) {

        topicViewHolder.topicname.setText(this.topics.get(i).getName());
        topicViewHolder.topiccreator.setText("~ " + this.topics.get(i).getCreator().getName());

        String mediatype = this.topics.get(i).getMediatype();


        switch (mediatype) {
            case "text":
                topicViewHolder.topicmediatype.setText("#Nachricht");
                topicViewHolder.linearLayout_media.setVisibility(View.GONE);
                break;

            case "video":
                topicViewHolder.topicmediatype.setText("#Video");
                topicViewHolder.topicvideo.setTag(new Integer(1));
                topicViewHolder.circlebar.setVisibility(View.VISIBLE);
                progressbarAnimation(topicViewHolder);

                topicViewHolder.topicstreambutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        topicViewHolder.topicvideo.setVideoPath(PersistanceDataHandler.getApiRootURL() + topics.get(i).getContenturl());
                        finishVideoView(topicViewHolder.topicvideo, topicViewHolder.circlebar);
                        topicViewHolder.topicstreambutton.setVisibility(View.GONE);
                    }
                });

                setMediaToTopic(topicViewHolder.topicvideo, null, topicViewHolder.topicstreambutton, topicViewHolder.circlebar, this.topics.get(i).getContenturl(), 1, topics.get(i));

                break;

            case "audio":
                topicViewHolder.topicmediatype.setText("#Audio");
                topicViewHolder.topicvideo.setTag(new Integer(2));

                ViewGroup.LayoutParams params = topicViewHolder.topicvideo.getLayoutParams();
                params.height = 300;
                params.width = 300;
                topicViewHolder.topicvideo.setLayoutParams(params);

                topicViewHolder.topicvideo.setBackground(ContextCompat.getDrawable(originactivity, R.drawable.media_audio));
                topicViewHolder.circlebar.setVisibility(View.VISIBLE);
                progressbarAnimation(topicViewHolder);

                topicViewHolder.topicstreambutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        topicViewHolder.topicvideo.setVideoPath(PersistanceDataHandler.getApiRootURL() + topics.get(i).getContenturl());
                        finishVideoView(topicViewHolder.topicvideo, topicViewHolder.circlebar);
                        topicViewHolder.topicstreambutton.setVisibility(View.GONE);
                    }
                });

                setMediaToTopic(topicViewHolder.topicvideo, null, topicViewHolder.topicstreambutton, topicViewHolder.circlebar, this.topics.get(i).getContenturl(), 2, topics.get(i));
                break;

            case "image":
                topicViewHolder.topicmediatype.setText("#Bild");
                topicViewHolder.circlebar.setVisibility(View.VISIBLE);
                progressbarAnimation(topicViewHolder);
                topicViewHolder.topicstreambutton.setTag(R.string.streamingbuttonAdapter, this);

                topicViewHolder.topicstreambutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DownloadImagehandler downloadImagehandler = new DownloadImagehandler((TopiclistRecyclerviewAdapter) view.getTag(R.string.streamingbuttonAdapter), topicViewHolder.topicimage);
                        downloadImagehandler.execute(topics.get(i).getContenturl());


                        topicViewHolder.topicimage.setVisibility(View.VISIBLE);
                        topicViewHolder.topicstreambutton.setVisibility(View.GONE);
                        topicViewHolder.circlebar.setVisibility(View.GONE);

                    }
                });

                setMediaToTopic(null, topicViewHolder.topicimage, topicViewHolder.topicstreambutton, topicViewHolder.circlebar, this.topics.get(i).getContenturl(), 3, topics.get(i));
                break;

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

            ArrayList<Response> matchingResponses = new ArrayList<>();
            for (Response response :
                    this.responses) {
                if (response.getTopic().get_id().equals(this.topics.get(i).get_id())) {
                    matchingResponses.add(response);
                }
            }

            topicViewHolder.responsecount.setText("#" + matchingResponses.size() + "Antworten");
            if (matchingResponses.size() == 0) {
                topicViewHolder.responsecount.setTextColor(R.color.colorSecondaryLight);
            }

            this.responseRecyclerlistAdapter = new ResponseRecyclerlistAdapter(matchingResponses, this.topics.get(i), originactivity, topicViewHolder);

            topicViewHolder.responserecyclerview.setAdapter(this.responseRecyclerlistAdapter);


            topicViewHolder.responserecyclerview.setVisibility(View.VISIBLE);
            topicViewHolder.responserecyclerview.setLayoutAnimation(animation);
        }


        topicViewHolder.topicaddresponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newContent = new Intent(originactivity, NewContentActivity_.class);
                Bundle extras = new Bundle();
                extras.putParcelable("topic", topics.get(i));
                newContent.putExtras(extras);
                originactivity.startActivity(newContent);
            }
        });
    }

    private void progressbarAnimation(@NonNull TopicViewHolder topicViewHolder) {
        ObjectAnimator anim = ObjectAnimator.ofInt(topicViewHolder.circlebar, "progress", 0, 100);
        anim.setDuration(1200);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return this.topics.size();
    }

    public void setStreamedImage(ImageView imageView, Bitmap bitmap) {
        int newheight = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, newheight, true);
        imageView.setImageBitmap(scaled);
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {

        CardView topiccardview;
        TextView topicname;
        TextView topiccreator;
        TextView topictype;
        TextView topictext;
        TextView topicmediatype;
        TextView responsecount;
        LinearLayout linearlayout;
        LinearLayout linearLayout_media;
        LinearLayout topiclist_content;
        LinearLayout topiclist_info;
        RecyclerView responserecyclerview;
        VideoView topicvideo;
        ProgressBar circlebar;
        ImageView topicimage;
        Button topicstreambutton;
        ImageView topicaddresponse;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topiccardview = (CardView) itemView.findViewById(R.id.topiclist_cardView);
            topicname = (TextView) itemView.findViewById(R.id.topiclist_name);
            topiccreator = (TextView) itemView.findViewById(R.id.topiclist_creator);
            topictype = (TextView) itemView.findViewById(R.id.topiclist_type);
            topictext = (TextView) itemView.findViewById(R.id.topiclist_text);
            topicmediatype = (TextView) itemView.findViewById(R.id.topiclist_mediatype);
            responsecount = (TextView) itemView.findViewById(R.id.topiclist_responsecount);
            linearlayout = (LinearLayout) itemView.findViewById(R.id.topiclist_linearlayout);
            linearLayout_media = (LinearLayout) itemView.findViewById(R.id.topiclist_linearlayout_media);
            topiclist_content = (LinearLayout) itemView.findViewById(R.id.topiclist_content);
            topiclist_info = (LinearLayout) itemView.findViewById(R.id.topiclist_info);
            responserecyclerview = (RecyclerView) itemView.findViewById(R.id.responserecyclerview);
            topicvideo = (VideoView) itemView.findViewById(R.id.topiclist_media_videoview);
            circlebar = (ProgressBar) itemView.findViewById(R.id.topicmedia_loadingcircle);
            topicimage = (ImageView) itemView.findViewById(R.id.topiclist_media_imageview);
            topicstreambutton = (Button) itemView.findViewById(R.id.topiclist_streambutton);
            topicaddresponse = (ImageView) itemView.findViewById(R.id.topiclist_addresponse);


        }
    }


}

