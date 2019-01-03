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

    private ImageButton fullScreen;
    public static boolean isFullscreen = false;
    public static Activity originalactivity;
    public static VideoView videoView;
    public static String path;

    public CustomMediacontroller(Context context) {
        super(context);
    }

    public void setVars(Activity activity, String videopath, VideoView view){
        originalactivity = activity;
        path =  videopath;
        videoView = view;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);

        fullScreen = new ImageButton (super.getContext());
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        params.rightMargin = 80;

        if((originalactivity instanceof VideoviewFullscreen_)){

            fullScreen.setImageResource(R.drawable.mediaplayer_fullscreen_off);


            addView(fullScreen, params);

            fullScreen.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(originalactivity != null){
                       originalactivity.finish();
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

        fullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(originalactivity != null){
                    Intent intent = new Intent(originalactivity, VideoviewFullscreen_.class);
                    intent.putExtra("videopath", path);
                    intent.putExtra("currenttime", videoView.getCurrentPosition());
                    originalactivity.startActivity(intent);
                }


            }
        });
    }


}
