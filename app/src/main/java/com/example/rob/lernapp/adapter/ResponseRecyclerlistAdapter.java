package com.example.rob.lernapp.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
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
    public RecyclerView responserecyclerview;
    public TopiclistRecyclerviewAdapter.TopicViewHolder topiclistItemViewHolder;
    public static boolean topiccontentAnimatedShrink = false;
    public static boolean topiccontentAnimatedExpand = false;

    public static boolean topiccontentShrink = false;
    public static boolean topiccontentExpand = true;

    public ResponseRecyclerlistAdapter(ArrayList<Response> responses, Topic topic, Activity activity, TopiclistRecyclerviewAdapter.TopicViewHolder topicViewHolder) {
        this.responses = responses;
        this.topic = topic;
        originactivity = activity;
        this.topiclistItemViewHolder = topicViewHolder;

        this.topiclistItemViewHolder.responserecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                        int totalItemsInView = layoutManager.getItemCount();
                        handleScroll(totalItemsInView - 1, recyclerView);
                    }
                }
            }
        });

    }

    private void handleScroll(int totalItemsInView, RecyclerView recyclerview) {
        int offset = recyclerview.computeVerticalScrollOffset();

        CardView topiccardview = this.topiclistItemViewHolder.topiccardview;
        final LinearLayout item = this.topiclistItemViewHolder.linearlayout;
        final LinearLayout topiclist_info = this.topiclistItemViewHolder.topiclist_info;
        final RecyclerView responserecyclerview = this.topiclistItemViewHolder.responserecyclerview;

        final LinearLayout topiclistcontent = this.topiclistItemViewHolder.topiclist_content;



        handleResponseScroll(offset, topiccardview, topiclist_info, topiclistcontent);


    }

    private void handleResponseScroll(final int offset, final CardView topiccardview, final LinearLayout topiclist_info, final LinearLayout topiclistcontent) {
        if (offset > 10) {

            if(!topiccontentAnimatedShrink && !topiccontentAnimatedExpand && topiccontentExpand){
                animateScrollWeight(topiccardview, 700, 5).start();
               ObjectAnimator animShrink = animateScrollWeight(topiclist_info, 700, 1);
                animShrink.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        topiccontentAnimatedShrink = true;

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        topiclistcontent.setVisibility(View.GONE);
                        topiccontentAnimatedShrink = false;
                        topiccontentShrink = true;
                        topiccontentExpand = false;

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

        }else if(offset == 0){
            if(!topiccontentAnimatedExpand && !topiccontentAnimatedShrink && topiccontentShrink){

                animateScrollWeight(topiccardview, 700, 1).start();
                ObjectAnimator animExpand = animateScrollWeight(topiclist_info, 700, 8);
                animExpand.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        topiccontentAnimatedExpand = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        topiclistcontent.setVisibility(View.VISIBLE);
                        topiccontentAnimatedExpand = false;
                        topiccontentExpand = true;
                        topiccontentShrink = false;

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
    }

    private ObjectAnimator animateScrollWeight(View view, int duration, int weight){
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
    public void onBindViewHolder(@NonNull ResponseRecyclerlistAdapter.ResponseViewHolder responseViewHolder, int i) {

        responseViewHolder.responsecreatorname.setText(this.responses.get(i).getCreator().getName());

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

        public ResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            responsecardview = (CardView) itemView.findViewById(R.id.response_cardView);
            responsecreatorname = (TextView) itemView.findViewById(R.id.response_creatorname);


        }
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


