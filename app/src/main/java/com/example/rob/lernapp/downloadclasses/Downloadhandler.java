package com.example.rob.lernapp.downloadclasses;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.example.rob.lernapp.PersistanceDataHandler;
import com.example.rob.lernapp.adapter.ResponseRecyclerlistAdapter;
import com.example.rob.lernapp.adapter.TopiclistRecyclerviewAdapter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Downloadhandler extends AsyncTask<String, String, String> {

    public Activity activity;
    public String filePath;
    public TopiclistRecyclerviewAdapter topiclistRecyclerviewAdapter;
    public ResponseRecyclerlistAdapter responseRecyclerlistAdapter;
    public VideoView videoView;
    public String contentURL;
    public ProgressBar circlebar;
    public ImageView imageView;
    public boolean isTopic = false;
    public Button streamingbutton;


    public Downloadhandler(Activity a, ProgressBar circlebar, RecyclerView.Adapter recyclerviewAdapter, VideoView videoView, ImageView imageView, Button streamingbutton) {
        this.activity = a;
        this.videoView = videoView;
        this.circlebar = circlebar;
        this.imageView = imageView;
        this.streamingbutton = streamingbutton;

        if (recyclerviewAdapter instanceof TopiclistRecyclerviewAdapter) {
            this.isTopic = true;
            this.topiclistRecyclerviewAdapter = (TopiclistRecyclerviewAdapter) recyclerviewAdapter;
        } else if (recyclerviewAdapter instanceof ResponseRecyclerlistAdapter) {
            this.isTopic = false;
            this.responseRecyclerlistAdapter = (ResponseRecyclerlistAdapter) recyclerviewAdapter;
        }

    }

    @Override
    protected String doInBackground(String... downloadURL) {


        String completeContentURL = PersistanceDataHandler.getApiRootURL() + downloadURL[0];
        this.contentURL = completeContentURL;
        File mediadownloadsDir = new File(Environment.getExternalStorageDirectory() + "/Learnapp_media");

        File currentFile = null;
        try {


            URL contentURL = new URL(completeContentURL);

            URLConnection connection = contentURL.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(10000);

            int filesize = connection.getContentLength();
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long bytesAvailable;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
            } else {
                bytesAvailable = stat.getBlockSize() * stat.getAvailableBlocks();
            }

            if (bytesAvailable < filesize) {
                int u = 3;
            } else {
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 512);

                String filename = downloadURL[0].substring(downloadURL[0].indexOf("/") + 1);


                if (!mediadownloadsDir.exists()) {
                    mediadownloadsDir.mkdir();
                }
                currentFile = new File(mediadownloadsDir.getPath() + "/" + filename);
                if (!currentFile.exists()) {
                    currentFile.createNewFile();
                    FileOutputStream outStream = new FileOutputStream(currentFile);
                    byte[] buff = new byte[512];

                    int len;
                    while ((len = bufferedInputStream.read(buff)) != -1) {
                        outStream.write(buff, 0, len);
                    }


                    outStream.flush();
                    outStream.close();
                    bufferedInputStream.close();
                }

                this.filePath = currentFile.getAbsolutePath();
                return currentFile.getAbsolutePath();
            }


        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return "nospace";

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        String path;
        if(s.isEmpty()){
            path = this.filePath;
        }else{
            path = s;
        }

        if (s.equals("nospace")) {
            if(isTopic){
                topiclistRecyclerviewAdapter.notEnoughSpace(this.streamingbutton);
            }else{
                responseRecyclerlistAdapter.notEnoughSpace(this.streamingbutton);
            }

        } else {
            if (isTopic) {
                topiclistRecyclerviewAdapter.setMediaPath(path, this.circlebar, this.videoView, this.imageView, this.contentURL);
            } else {
                responseRecyclerlistAdapter.setMediaPath(path, this.circlebar, this.videoView, this.imageView, this.contentURL);
            }
        }



    }

}

