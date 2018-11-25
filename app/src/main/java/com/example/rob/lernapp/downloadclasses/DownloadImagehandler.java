package com.example.rob.lernapp.downloadclasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.rob.lernapp.PersistanceDataHandler;
import com.example.rob.lernapp.adapter.ResponseRecyclerlistAdapter;
import com.example.rob.lernapp.adapter.TopiclistRecyclerviewAdapter;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadImagehandler extends AsyncTask<String, String, Bitmap> {

    public TopiclistRecyclerviewAdapter topiclistRecyclerviewAdapter;
    public ResponseRecyclerlistAdapter responseRecyclerlistAdapter;
    public String contentURL;
    public ImageView imageView;
    public boolean isTopic = false;
    public Bitmap streamedimage;


    public DownloadImagehandler( RecyclerView.Adapter recyclerviewAdapter,  ImageView imageView) {

        this.imageView = imageView;

        if (recyclerviewAdapter instanceof TopiclistRecyclerviewAdapter) {
            this.isTopic = true;
            this.topiclistRecyclerviewAdapter = (TopiclistRecyclerviewAdapter) recyclerviewAdapter;
        } else if (recyclerviewAdapter instanceof ResponseRecyclerlistAdapter) {
            this.isTopic = false;
            this.responseRecyclerlistAdapter = (ResponseRecyclerlistAdapter) recyclerviewAdapter;
        }

    }

    @Override
    protected Bitmap doInBackground(String... downloadURL) {


        String completeContentURL = PersistanceDataHandler.getApiRootURL() + downloadURL[0];
        this.contentURL = completeContentURL;

        Bitmap streamedImage = null;

        try{
            URL contentURL = new URL(this.contentURL);

            URLConnection connection = contentURL.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(10000);


            connection.connect();
            InputStream input = connection.getInputStream();
            streamedImage = BitmapFactory.decodeStream(input);
            this.streamedimage = streamedImage;
        }catch (Exception e){

        }


            return streamedImage;



    }

    @Override
    protected void onPostExecute(Bitmap s) {
        super.onPostExecute(s);

            Bitmap bitmap;

            if(s == null){
                bitmap = this.streamedimage;
            }else{
                bitmap = s;
            }

        if (isTopic) {
            topiclistRecyclerviewAdapter.setStreamedImage(this.imageView, bitmap);
        } else {
            responseRecyclerlistAdapter.setStreamedImage(this.imageView, bitmap);
        }


    }

}

