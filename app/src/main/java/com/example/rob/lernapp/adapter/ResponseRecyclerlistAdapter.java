package com.example.rob.lernapp.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.rob.lernapp.CategoryViewActivity;
import com.example.rob.lernapp.PersistanceDataHandler;
import com.example.rob.lernapp.R;
import com.example.rob.lernapp.ResponseExpand;
import com.example.rob.lernapp.downloadclasses.Downloadhandler;
import com.example.rob.lernapp.restdataGet.Response;
import com.example.rob.lernapp.restdataGet.Topic;

import java.util.ArrayList;


public class ResponseRecyclerlistAdapter extends RecyclerView.Adapter<ResponseRecyclerlistAdapter.ResponseViewHolder> {

    public ArrayList<Response> responses;
    public ArrayList<Response> currentResponses = new ArrayList<Response>();
    public static CategoryViewActivity originactivity;
    public Topic topic;

    public TopiclistRecyclerviewAdapter.TopicViewHolder topiclistItemViewHolder;
    public static boolean topiccontentAnimated = false;


    ResponseExpand responseExpand = new ResponseExpand();


    public ResponseRecyclerlistAdapter(ArrayList<Response> responses, Topic topic, CategoryViewActivity activity, TopiclistRecyclerviewAdapter.TopicViewHolder topicViewHolder) {
        this.responses = responses;
        this.topic = topic;
        originactivity = activity;
        this.topiclistItemViewHolder = topicViewHolder;


        this.topiclistItemViewHolder.responserecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {

            public int finalScrollPosition;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        recyclerView.smoothScrollToPosition(finalScrollPosition);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {

                        //handleExpand(recyclerView, 0);

                        int totalItemsInView = layoutManager.getItemCount();
                        this.finalScrollPosition = changeScrollVertical(totalItemsInView - 1, recyclerView);
                    }
                }
            }
        });

    }

    private int changeScrollVertical(int totalItemsInView, RecyclerView recyclerView) {

        int offset = recyclerView.computeVerticalScrollOffset();
        int extent = recyclerView.computeVerticalScrollExtent();

        int scrollValue;

        int offsetRest = offset % extent;
        int offsetRestMinus = offset - offsetRest;

        int viewposition;

        if (offsetRest > (extent / 2)) {
            int extentMinusRest = extent - offsetRest;
            scrollValue = offset + extentMinusRest;
            viewposition = scrollValue / extent;
        } else {
            scrollValue = offsetRestMinus;
            viewposition = scrollValue / extent;
        }

        return viewposition;
    }

    private void handleExpand(RecyclerView recyclerview) {
        // int offset = recyclerview.computeVerticalScrollOffset();

        CardView topiccardview = this.topiclistItemViewHolder.topiccardview;
        final LinearLayout topiclist_info = this.topiclistItemViewHolder.topiclist_info;
        VideoView videoView = this.topiclistItemViewHolder.topicvideo;
        final LinearLayout topiclistcontent = this.topiclistItemViewHolder.topiclist_content;
        handleResponseScroll(topiccardview, topiclist_info, topiclistcontent, videoView);

    }

    private void handleResponseScroll(final CardView topiccardview, final LinearLayout topiclist_info, final LinearLayout topiclistcontent, VideoView videoView) {


        if (!topiccontentAnimated && !this.responseExpand.isresponseExpand()) {
            videoView.pause();
            animateScrollWeight(topiccardview, 500, 5).start();
            ObjectAnimator animShrink = animateScrollWeight(topiclist_info, 700, 1);

            animShrink.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    topiccontentAnimated = true;
                    responseExpand.setisresponseExpand(true);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    topiclistcontent.setVisibility(View.GONE);
                    topiccontentAnimated = false;


                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animShrink.start();
        }


        if (!topiccontentAnimated && this.responseExpand.isresponseExpand()) {

            animateScrollWeight(topiccardview, 500, 1).start();
            ObjectAnimator animExpand = animateScrollWeight(topiclist_info, 700, 8);
            animExpand.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    topiccontentAnimated = true;
                    topiclistcontent.setVisibility(View.VISIBLE);
                    responseExpand.setisresponseExpand(false);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    topiccontentAnimated = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animExpand.start();


        }

    }

    private ObjectAnimator animateScrollWeight(View view, int duration, int weight) {
        ViewWeightAnimationWrapper animationWrapper = new ViewWeightAnimationWrapper(view);
        ObjectAnimator anim = ObjectAnimator.ofFloat(animationWrapper,
                "weight",
                animationWrapper.getWeight(),
                weight);
        anim.setDuration(duration);

        return anim;
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
    public void onBindViewHolder(@NonNull final ResponseRecyclerlistAdapter.ResponseViewHolder responseViewHolder, int i) {



        this.responseExpand.setListener(new ResponseExpand.responseExpandListener() {
            @Override
            public void onChange(boolean isExpand) {
                if (responseViewHolder.responseExpand != null) {


                    if (isExpand) {
                        responseViewHolder.responseExpand.setImageResource(R.drawable.shrink_response);
                    } else {
                        responseViewHolder.responseExpand.setImageResource(R.drawable.expand_response);
                    }
                }


            }
        });


        responseViewHolder.responseExpand.setVisibility(View.VISIBLE);
        responseViewHolder.responseExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!responseExpand.isresponseExpand()) {




                    if (topiclistItemViewHolder.responserecyclerview != null) {
                        handleExpand(topiclistItemViewHolder.responserecyclerview);

                    }

                } else if (responseExpand.isresponseExpand()) {

                    if (topiclistItemViewHolder.responserecyclerview != null) {
                        handleExpand(topiclistItemViewHolder.responserecyclerview);

                    }


                }
            }
        });


        responseViewHolder.responsecreatorname.setText("~ " + this.responses.get(i).getCreator().getName());
        responseViewHolder.responsetext.setText(this.responses.get(i).getText());

        String mediatype = this.responses.get(i).getMediatype();


        switch (mediatype) {
            case "text":
                responseViewHolder.responsemediatype.setText("#Nachricht");
                responseViewHolder.responseLayoutMedia.setVisibility(View.GONE);
                break;

            case "video":
                responseViewHolder.responsemediatype.setText("#Video");
                //topicViewHolder.topicvideo.setTag(new Integer(1));
                responseViewHolder.circlebar.setVisibility(View.VISIBLE);
                progressbarAnimation(responseViewHolder);
                setMediaToResponse(responseViewHolder.responsevideoView, null, responseViewHolder.circlebar, this.responses.get(i).getContenturl(), 1);

                break;

            case "audio":
                responseViewHolder.responsemediatype.setText("#Audio");
                //topicViewHolder.topicvideo.setTag(new Integer(2));
                ViewGroup.LayoutParams params = responseViewHolder.responsevideoView.getLayoutParams();
                params.height = 300;
                responseViewHolder.responsevideoView.setLayoutParams(params);
                responseViewHolder.circlebar.setVisibility(View.VISIBLE);
                progressbarAnimation(responseViewHolder);
                setMediaToResponse(responseViewHolder.responsevideoView, null, responseViewHolder.circlebar, this.responses.get(i).getContenturl(), 2);
                break;

            case "image":
                responseViewHolder.responsemediatype.setText("#Bild");
                responseViewHolder.circlebar.setVisibility(View.VISIBLE);
                progressbarAnimation(responseViewHolder);
                setMediaToResponse(null, responseViewHolder.responseImageView, responseViewHolder.circlebar, this.responses.get(i).getContenturl(), 3);
                break;

        }

    }

    @UiThread
    public void setMediaPath(String path, ProgressBar circlebar, VideoView videoView, ImageView imageView, String contentURL) {

        if (imageView != null && videoView == null) {
            try {

                Bitmap image = BitmapFactory.decodeFile(path);
                imageView.setImageBitmap(image);
            } catch (Exception e) {
                Bitmap image = BitmapFactory.decodeFile(contentURL);
                imageView.setImageBitmap(image);
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

    public void setMediaToResponse(VideoView videoView, ImageView imageView, ProgressBar circlebar, String contentURL, int option) {


        switch (option) {
            case 1:
                if (!originactivity.streamContent) {
                    Downloadhandler downloadhandler = (Downloadhandler) new Downloadhandler(originactivity, circlebar, this, videoView, null);
                    downloadhandler.execute(contentURL);
                } else {
                    videoView.setVideoPath(PersistanceDataHandler.getApiRootURL() + contentURL);
                    finishVideoView(videoView, circlebar);
                }
                break;

            case 2:


                if (!originactivity.streamContent) {
                    Downloadhandler downloadhandler = (Downloadhandler) new Downloadhandler(originactivity, circlebar, this, videoView, null);
                    downloadhandler.execute(contentURL);
                } else {
                    videoView.setVideoPath(PersistanceDataHandler.getApiRootURL() + contentURL);
                    finishVideoView(videoView, circlebar);
                }

                break;

            case 3:
                if (!originactivity.streamContent) {
                    Downloadhandler downloadhandler = (Downloadhandler) new Downloadhandler(originactivity, circlebar, this, null, imageView);
                    downloadhandler.execute(contentURL);
                } else {
                    Bitmap image = BitmapFactory.decodeFile(PersistanceDataHandler.getApiRootURL() + contentURL);
                    imageView.setImageBitmap(image);
                    circlebar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }
                break;
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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {

        if (this.responses.size() != 0) {
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
        TextView responsecreatorname;
        TextView responsetext;
        TextView responsemediatype;
        LinearLayout responseLayoutMedia;
        ProgressBar circlebar;
        VideoView responsevideoView;
        ImageView responseImageView;
        ImageView responseExpand;

        public ResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            responsecardview = (CardView) itemView.findViewById(R.id.response_cardView);
            responsecreatorname = (TextView) itemView.findViewById(R.id.response_creatorname);
            responsetext = (TextView) itemView.findViewById(R.id.response_text);
            responsemediatype = (TextView) itemView.findViewById(R.id.response_mediatype);
            responseLayoutMedia = (LinearLayout) itemView.findViewById(R.id.response_linearlayout_media);
            circlebar = (ProgressBar) itemView.findViewById(R.id.response_loadingcircle);
            responsevideoView = (VideoView) itemView.findViewById(R.id.response_media_videoview);
            responseImageView = (ImageView) itemView.findViewById(R.id.response_media_imageview);
            responseExpand = (ImageView) itemView.findViewById(R.id.expand_response);


        }
    }

    private void progressbarAnimation(@NonNull ResponseRecyclerlistAdapter.ResponseViewHolder responseViewHolder) {
        ObjectAnimator anim = ObjectAnimator.ofInt(responseViewHolder.circlebar, "progress", 0, 100);
        anim.setDuration(1200);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }

    public class ViewWeightAnimationWrapper {
        private View view;

        public ViewWeightAnimationWrapper(View view) {
            if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                this.view = view;
            } else {
                throw new IllegalArgumentException("The view should have LinearLayout as parent");
            }
        }

        public void setWeight(float weight) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.weight = weight;
            view.getParent().requestLayout();
        }

        public float getWeight() {
            return ((LinearLayout.LayoutParams) view.getLayoutParams()).weight;
        }
    }
}


