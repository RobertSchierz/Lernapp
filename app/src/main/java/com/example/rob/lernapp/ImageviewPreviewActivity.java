package com.example.rob.lernapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.rob.lernapp.downloadclasses.DownloadImagehandler;
import com.example.rob.lernapp.restdataGet.Learngroup;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jsibbold.zoomage.ZoomageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

@EActivity(R.layout.activity_imageview_preview)
public class ImageviewPreviewActivity extends AppCompatActivity {

    String imagePath;
    Boolean isStreamed;
    private Socket learnappSocket;
    Boolean fromCategory = false;
    Boolean fromNewContent = false;

    Learngroup group;


    @SuppressLint("RestrictedApi")
    @AfterViews
    void onCreate() {

        if (getIntent().getExtras() != null) {
            this.imagePath = getIntent().getExtras().getString("imagepath");
            this.isStreamed = getIntent().getExtras().getBoolean("isStreamed");
            this.fromCategory = getIntent().getExtras().getBoolean("fromCategory");
            this.fromNewContent = getIntent().getExtras().getBoolean("fromNewContent");


            Bundle bundle = getIntent().getExtras();


            if (bundle.getParcelable("group") != null) {
                this.group = bundle.getParcelable("group");
            }

            if (this.isStreamed) {

                DownloadImagehandler downloadImagehandler = new DownloadImagehandler(this, true);
                if (!this.imagePath.isEmpty() || this.imagePath != null) {
                    downloadImagehandler.execute(this.imagePath);
                }else{
                    Toast.makeText(this, "Fehler beim Anzeigen des Bildes", Toast.LENGTH_SHORT).show();
                    this.finish();
                }


            } else {
                if (!this.imagePath.isEmpty() || this.imagePath != null) {

                    if(this.fromNewContent){
                        try {
                            Uri imageUri = Uri.parse(this.imagePath);
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri);
                            this.zoomageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Bitmap bitmap = BitmapFactory.decodeFile(this.imagePath);
                        this.zoomageView.setImageBitmap(bitmap);
                    }

                } else {
                    Toast.makeText(this, "Fehler beim Anzeigen des Bildes", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
            }


        }

        setTitle("Fotovorschau");


    }

    public void setStreamedImage(Bitmap streamedImage){
        if(streamedImage != null){
            this.zoomageView.setImageBitmap(streamedImage);
        }else{
            Toast.makeText(this, "Fehler beim Anzeigen des Bildes", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }


    @ViewById(R.id.previewimage)
    ZoomageView zoomageView;


    private JsonObject getJsonObjectforSocketIO(Object arg) {
        JsonParser parser = new JsonParser();
        return parser.parse(String.valueOf(arg)).getAsJsonObject();
    }


    @Override
    protected void onResume() {
        super.onResume();
        this.learnappSocket = SocketHandler.getInstance().getlearnappSocket();
        this.learnappSocket.connect();
        this.learnappSocket.on("deletedGroup", onDeletedGroupImagePreview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.learnappSocket.off("deletedGroup", onDeletedGroupImagePreview);
        this.finish();
    }


    private Emitter.Listener onDeletedGroupImagePreview = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            backToGroups(getJsonObjectforSocketIO(args[0]));
        }
    };

    @UiThread
    public void backToGroups(JsonObject data) {
        Gson gson = new Gson();
        Learngroup deletedLearngroup = gson.fromJson(data, Learngroup.class);

        boolean isThisGroup = false;


            if (this.group.get_id().equals(deletedLearngroup.get_id())) {
                isThisGroup = true;
            }


        if (isThisGroup) {
            Toast.makeText(this, "Gruppe " + deletedLearngroup.getName() + " wurde vom Admin " + deletedLearngroup.getCreator().getName() + " gelöscht", Toast.LENGTH_LONG).show();
            Intent openLearngroups = new Intent(this, LearngroupsActivity_.class);
            openLearngroups.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(openLearngroups);
            this.finish();
        }
    }


}
