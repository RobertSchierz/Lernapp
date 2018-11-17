package com.example.rob.lernapp.downloadclasses;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
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

    public Downloadhandler(Activity a, TopiclistRecyclerviewAdapter topiclistRecyclerviewAdapter, VideoView videoView) {
        this.activity = a;
        this.topiclistRecyclerviewAdapter = topiclistRecyclerviewAdapter;
        this.videoView = videoView;
    }

    @Override
    protected String doInBackground(String... downloadURL) {

        String completeContentURL = PersistanceDataHandler.getApiRootURL() + downloadURL[0];
        File currentFile = null;

        try {

            URL contentURL = new URL(completeContentURL);

            URLConnection connection = contentURL.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(10000);

            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024 * 50);

            String filename = downloadURL[0].substring(downloadURL[0].indexOf("/")+1);

            //File file = new File(activity.getFilesDir(), filename);



            File mediadownloadsDir = new File(Environment.getExternalStorageDirectory() + "/Android" + "/media" + "/Learnapp_mediadownloads");
            if(!mediadownloadsDir.exists()){
                mediadownloadsDir.mkdir();
            }
                currentFile = new File(mediadownloadsDir.getPath() + "/" + filename);
                if(!currentFile.exists()){
                    currentFile.createNewFile();
                    FileOutputStream outStream = new FileOutputStream(currentFile);
                    byte[] buff = new byte[50 * 1024];

                    int len;
                    while ((len = bufferedInputStream.read(buff)) != -1) {
                        outStream.write(buff, 0, len);
                    }

                    outStream.flush();
                    outStream.close();
                    bufferedInputStream.close();
                }




            this.filePath =  currentFile.getAbsolutePath();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return this.filePath;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        topiclistRecyclerviewAdapter.tester(s, this.videoView);
    }
}
