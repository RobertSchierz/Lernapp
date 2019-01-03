package com.example.rob.lernapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_videoview_fullscreen)
public class VideoviewFullscreen extends AppCompatActivity {

    String path;
    int currenttime;

    @ViewById(R.id.videofullscreen)
    VideoView videofullscreen;

    @AfterViews
    void afterview(){


        if(getIntent().getExtras() != null){
            Intent intent = getIntent();
            path = intent.getStringExtra("videopath");
            currenttime = intent.getIntExtra("currenttime", 0);

            videofullscreen.setVideoPath(path);

            videofullscreen.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    return false;
                }
            });

            CustomMediacontroller customMediacontroller = new CustomMediacontroller(this);
            customMediacontroller.setVars(this, path, videofullscreen);

            customMediacontroller.setAnchorView(videofullscreen);
            videofullscreen.setMediaController(customMediacontroller);
            videofullscreen.start();

            videofullscreen.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                }
            });

        }



    }



}
