package com.example.rob.lernapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import org.androidannotations.annotations.EBean;

@EBean
public class CustomMediacontroller extends MediaController {

    public ImageButton fullScreen;
    public static boolean isFullscreen = false;
    public Activity originalactivity;
    public VideoView videoView;
    public String path;
    public CustomMediacontroller customMediacontroller;


    public CustomMediacontroller(Context context) {
        super(context);
        this.customMediacontroller = this;

    }

    public void setVars(Activity activity, String videopath, VideoView view){
        this.originalactivity = activity;
        this.path =  videopath;
        this.videoView = view;
    }

    @Override
    public void setAnchorView(final View view) {
        super.setAnchorView(view);

        fullScreen = new ImageButton (super.getContext());
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        params.rightMargin = 80;

        final Activity finaloriginactivity = this.originalactivity;

        if((originalactivity instanceof VideoviewFullscreen_)){

            fullScreen.setImageResource(R.drawable.mediaplayer_fullscreen_off);


            addView(fullScreen, params);

            final CustomMediacontroller finalcustomMediacontroller = this.customMediacontroller;

            fullScreen.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(finaloriginactivity != null){
                        finalcustomMediacontroller.hide();
                        finaloriginactivity.finish();
                    }

                }
            });
        }else{
            fromMainPlayer(params);
        }



    }

    private void fromMainPlayer(LayoutParams params) {
        fullScreen.setImageResource(R.drawable.mediaplayer_fullscreen_on);


        addView(fullScreen, params);

        final VideoView finalvideoView = this.videoView;
        final String finalpath = this.path;
        final CategoryViewActivity finaloriginactivity = (CategoryViewActivity) this.originalactivity;

        fullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(finaloriginactivity != null){
                    finaloriginactivity.resumedFromVideofullscreen = true;
                    Intent intent = new Intent(finaloriginactivity, VideoviewFullscreen_.class);
                    intent.putExtra("videopath", finalpath);
                    intent.putExtra("currenttime", finalvideoView.getCurrentPosition());
                    finaloriginactivity.startActivity(intent);
                }


            }
        });
    }


}
