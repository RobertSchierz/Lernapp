package com.example.rob.lernapp.downloadclasses;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.example.rob.lernapp.PersistanceDataHandler;
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
    public VideoView videoView;
    public String contentURL;
    public ProgressBar circlebar;
    public ImageView imageView;



    public Downloadhandler(Activity a, ProgressBar circlebar , TopiclistRecyclerviewAdapter topiclistRecyclerviewAdapter, VideoView videoView, ImageView imageView) {
        this.activity = a;
        this.topiclistRecyclerviewAdapter = topiclistRecyclerviewAdapter;
        this.videoView = videoView;
        this.circlebar = circlebar;
        this.imageView = imageView;

    }

    @Override
    protected String doInBackground(String... downloadURL) {


        String completeContentURL = PersistanceDataHandler.getApiRootURL() + downloadURL[0];
        this.contentURL = completeContentURL;
        File mediadownloadsDir = new File(Environment.getExternalStorageDirectory() + "/Learnapp_mediadownloads");

        File currentFile = null;
        try {


            URL contentURL = new URL(completeContentURL);

            URLConnection connection = contentURL.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(10000);

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

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return currentFile.getAbsolutePath();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s.isEmpty()) {
            topiclistRecyclerviewAdapter.setMediaPath(this.filePath, this.circlebar, this.videoView, this.imageView, this.contentURL);
        } else {
            topiclistRecyclerviewAdapter.setMediaPath(s, this.circlebar, this.videoView, this.imageView, this.contentURL);
        }

    }



}
