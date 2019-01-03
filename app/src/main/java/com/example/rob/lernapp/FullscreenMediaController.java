package com.example.rob.lernapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;

public class FullscreenMediaController extends MediaController {


    private ImageButton fullScreen;
    private String isFullScreen;
    public static Activity originActivity;

    public void setActivity(Activity activity){
        this.originActivity = activity;
    }

    public FullscreenMediaController(Context context) {
        super(context);
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
        addView(fullScreen, params);

        //fullscreen indicator from intent
        /*isFullScreen =  ((Activity)getContext()).getIntent().
                getStringExtra("fullScreenInd");*/

        isFullScreen = originActivity.getIntent().getStringExtra("fullScreenInd");

        if("y".equals(isFullScreen)){
            fullScreen.setImageResource(R.drawable.mediaplayer_fullscreen_off);
        }else{
            fullScreen.setImageResource(R.drawable.mediaplayer_fullscreen_on);
        }

        //add listener to image button to handle full screen and exit full screen events
        fullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(originActivity != null){
                    Intent intent = new Intent(originActivity.getApplicationContext(),originActivity.getClass());
                    if("y".equals(isFullScreen)){
                        intent.putExtra("fullScreenInd", "");
                    }else{
                        intent.putExtra("fullScreenInd", "y");
                    }
                    //((Activity)getContext()).startActivity(intent);
                    originActivity.getApplicationContext().startActivity(intent);
                }

            }
        });
    }
}
