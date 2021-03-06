package com.example.rob.lernapp;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.rob.lernapp.databaseUtilityClasses.DatabaseUtilityNewContent;
import com.example.rob.lernapp.restdataGet.Category;
import com.example.rob.lernapp.restdataGet.Learngroup;
import com.example.rob.lernapp.restdataGet.Topic;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

@EActivity(R.layout.activity_new_content)
public class NewContentActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    Topic topic;
    Category category;

    int source = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;

    private static final int PERMISSION_REQUEST_IMAGE = 10;
    private static final int PERMISSION_REQUEST_VIDEO = 20;
    private static final int PERMISSION_REQUEST_MIC = 30;



    Uri mCurrentPhotoPath;
    Uri mCurrentVideoPath;

    int mediatype = 0;
    File imageContentFile;
    File imageAcceptedFile;

    File videoContentFile;
    File videoAcceptedFile;

    File audioContentFile;

    MediaRecorder audio_rec;


    private Socket learnappSocket;

    @NonConfigurationInstance
    @Bean
    DatabaseUtilityNewContent dataBaseUtilTask;


    @AfterViews
    void onCreate() {

        if (getIntent().getExtras() != null) {

            Bundle bundle = getIntent().getExtras();

            if (bundle.getParcelable("topic") != null) {
                this.topic = bundle.getParcelable("topic");
                this.source = 2;
            }

            if (bundle.getParcelable("category") != null) {
                this.category = bundle.getParcelable("category");
                this.source = 1;
            }

            this.newcontentImageselector.setImageAlpha(127);
            this.newcontentMicselector.setImageAlpha(127);
            this.newcontentVideoselector.setImageAlpha(127);

            if (source == 2) {
                newcontent_radiogroup.setVisibility(View.GONE);
                setTitle(getResources().getText(R.string.contentactivitylabel) + " für - " + this.topic.getName());
            } else if (source == 1) {
                newcontent_radiogroup.setVisibility(View.VISIBLE);
                setTitle("Neuer Post für Kategory: " + this.category.getName());
            }


        } else {
            Toast.makeText(this, "Fehler beim Initialisieren der Ansicht", Toast.LENGTH_LONG).show();
            this.finish();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        this.learnappSocket = SocketHandler.getInstance().getlearnappSocket();
        this.learnappSocket.connect();
        this.learnappSocket.on("deletedGroup", onDeletedGroupNewContent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        this.learnappSocket.off("deletedGroup", onDeletedGroupNewContent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.learnappSocket.off("deletedGroup", onDeletedGroupNewContent);
    }

    private JsonObject getJsonObjectforSocketIO(Object arg) {
        JsonParser parser = new JsonParser();
        return parser.parse(String.valueOf(arg)).getAsJsonObject();
    }

    private Emitter.Listener onDeletedGroupNewContent = new Emitter.Listener() {
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

        if (this.source == 1) {
            if (this.category.getGroup().get_id().equals(deletedLearngroup.get_id())) {
                isThisGroup = true;
            }
        } else if (this.source == 2) {
            if (this.topic.getCategory().getGroup().get_id().equals(deletedLearngroup.get_id())) {
                isThisGroup = true;
            }
        }

        if (isThisGroup) {
            Toast.makeText(this, "Gruppe " + deletedLearngroup.getName() + " wurde vom Admin " + deletedLearngroup.getCreator().getName() + " gelöscht", Toast.LENGTH_LONG).show();
            Intent openLearngroups = new Intent(this, LearngroupsActivity_.class);
            openLearngroups.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(openLearngroups);
            this.finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.imageAcceptedFile != null) {
            this.imageAcceptedFile.delete();
        }
        if (this.imageContentFile != null) {
            this.imageContentFile.delete();
        }
        if (this.videoAcceptedFile != null) {
            this.videoAcceptedFile.delete();
        }
        if (this.videoContentFile != null) {
            this.videoContentFile.delete();
        }
        if (this.audioContentFile != null) {
            this.audioContentFile.delete();
        }
    }


    @TextChange(R.id.newcontent_name)
    void nameChange() {
        checkElements();
    }

    @TextChange(R.id.newcontent_text)
    void textChange() {
        checkElements();
    }

    @CheckedChange(R.id.newcontent_radiobuttonquestion)
    void radioQuestionChange() {
        checkElements();
    }

    @CheckedChange(R.id.newcontent_radiobuttonexplanation)
    void radioExplanationChange() {
        checkElements();
    }


    private void checkElements() {
        if (this.source == 1) {
            if (!newcontentName.getText().toString().isEmpty() &&
                    !newcontentText.getText().toString().isEmpty() &&
                    (newcontentRadiobuttonquestion.isChecked() || newcontentRadiobuttonexplanation.isChecked())) {
                this.newcontentSend.setEnabled(true);


                if (this.imageContentFile == null && this.videoContentFile == null && this.audioContentFile == null) {
                    this.mediatype = 4;
                }
            } else {
                this.newcontentSend.setEnabled(false);
            }
        } else if (this.source == 2) {
            if (!newcontentName.getText().toString().isEmpty() &&
                    !newcontentText.getText().toString().isEmpty()) {
                this.newcontentSend.setEnabled(true);


                if (this.imageContentFile == null && this.videoContentFile == null && this.audioContentFile == null) {
                    this.mediatype = 4;
                }
            } else {
                this.newcontentSend.setEnabled(false);
            }
        }

    }


    @Click(R.id.newcontent_imageselector)
    void clickImage() {


            this.newcontentImageselector.setEnabled(false);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    newcontentImageselector.setEnabled(true);
                }
            }, 1000);

        checkCameraPermissions();

    }

    private void makePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


            File photoFile = null;
            try {
                if (this.imageContentFile != null) {
                    this.imageAcceptedFile = this.imageContentFile;
                }
                photoFile = createMediaFile(1);
                this.mCurrentPhotoPath = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
                this.imageContentFile = photoFile;

            } catch (IOException ex) {
                Log.v("Photo", ex.getMessage());
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, this.mCurrentPhotoPath);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            }
        }
    }


    @Click(R.id.newcontent_videoselector)
    void clickVideo() {


            this.newcontentVideoselector.setEnabled(false);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    newcontentVideoselector.setEnabled(true);
                }
            }, 1000);



        checkVideoCameraPermissions();


    }

    private void takeVideo() {

        Intent takeViodepIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (takeViodepIntent.resolveActivity(getPackageManager()) != null) {


            File videoFile = null;
            try {
                if (this.videoContentFile != null) {
                    this.videoAcceptedFile = this.videoContentFile;
                }
                videoFile = createMediaFile(2);
                this.videoContentFile = videoFile;
                this.mCurrentVideoPath = FileProvider.getUriForFile(this, getPackageName() + ".provider", videoFile);

            } catch (IOException ex) {
                Log.v("Video", ex.getMessage());
            }

            if (videoFile != null) {
                takeViodepIntent.putExtra(MediaStore.EXTRA_OUTPUT, this.mCurrentVideoPath);
                startActivityForResult(takeViodepIntent, REQUEST_VIDEO_CAPTURE);
            }


        }
    }

    @Touch(R.id.newcontent_micselector)
    void touchMic(MotionEvent event) {

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
        if(EasyPermissions.hasPermissions(this, perms)){
            recordAudio(event);
        }else{
            checkMicPermissions();
        }

    }


    private void recordAudio(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {


            this.newcontent_recaudiocontrolls.setVisibility(View.VISIBLE);
            this.newcontentImagePreview.setVisibility(View.GONE);


            this.audio_rec = new MediaRecorder();
            this.audio_rec.setAudioSource(MediaRecorder.AudioSource.MIC);
            this.audio_rec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            this.audio_rec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                if (this.audioContentFile != null) {
                    this.audioContentFile.delete();
                    this.audioContentFile = null;
                }
                this.audioContentFile = createMediaFile(3);
                this.audioContentFile.createNewFile();

                this.audio_rec.setOutputFile(this.audioContentFile.getAbsolutePath());


                this.audio_rec.prepare();
                this.audio_rec.start();
                this.newcontentReclength.setBase(SystemClock.elapsedRealtime());
                this.newcontentReclength.start();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            if (this.audio_rec != null) {
                this.newcontent_recaudiocontrolls.setVisibility(View.GONE);
                this.audio_rec.stop();
                this.audio_rec.release();
                this.audio_rec = null;
                this.newcontentReclength.setBase(SystemClock.elapsedRealtime());
                this.newcontentReclength.stop();

                this.newcontentImageselector.setImageAlpha(127);
                this.newcontentMicselector.setImageAlpha(255);
                this.newcontentVideoselector.setImageAlpha(127);
                this.mediatype = 3;
                deleteImage();
                deleteVideo();


                setVideoPreview();
                this.newcontentVideoPreview.setVideoPath(this.audioContentFile.getAbsolutePath());


                this.newcontentVideoPreview.setVisibility(View.GONE);
                this.newcontentVideoPreview.setVisibility(View.VISIBLE);
                this.newcontentVideoPreview.setBackground(ContextCompat.getDrawable(this, R.drawable.media_audio));

                this.newcontentMicselector.setEnabled(false);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        newcontentMicselector.setEnabled(true);
                    }
                }, 1000);

            }


        }
    }

    @AfterPermissionGranted(PERMISSION_REQUEST_IMAGE)
    private void checkCameraPermissions(){
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if(EasyPermissions.hasPermissions(this, perms)){
            makePicture();
        }else{
            EasyPermissions.requestPermissions(this, "Diese Berechtigungen sind wichtig um auf die Kamera zuzugreifen", PERMISSION_REQUEST_IMAGE, perms);
        }
    }

    @AfterPermissionGranted(PERMISSION_REQUEST_VIDEO)
    private void checkVideoCameraPermissions(){
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if(EasyPermissions.hasPermissions(this, perms)){
            takeVideo();
        }else{
            EasyPermissions.requestPermissions(this, "Diese Berechtigungen sind wichtig um auf die Kamera zuzugreifen", PERMISSION_REQUEST_VIDEO, perms);
        }
    }

    @AfterPermissionGranted(PERMISSION_REQUEST_MIC)
    private void checkMicPermissions(){
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
        if(EasyPermissions.hasPermissions(this, perms)){
            Toast.makeText(this, "Die Mikrofonfunktion kann benutzt werden.", Toast.LENGTH_SHORT).show();
        }else{
            EasyPermissions.requestPermissions(this, "Diese Berechtigungen sind wichtig um auf das Mikrofon zuzugreifen", PERMISSION_REQUEST_MIC, perms);
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private File createMediaFile(int option) throws IOException {

        String suffix = null;

        if (option == 1) {
            suffix = ".jpg";
        } else if (option == 2) {
            suffix = ".mp4";
        } else if (option == 3) {
            suffix = ".3gp";
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Learnapp_media");
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File file = File.createTempFile(
                imageFileName,
                suffix,
                storageDir
        );

        return file;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (this.audioContentFile != null) {
                this.audioContentFile.delete();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            this.newcontentImageselector.setImageAlpha(255);
            this.newcontentMicselector.setImageAlpha(127);
            this.newcontentVideoselector.setImageAlpha(127);
            this.newcontentVideoPreview.setVisibility(View.GONE);
            this.mediatype = 1;

            try {
                this.imageContentFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), this.mCurrentPhotoPath);
                this.newcontentImagePreview.setVisibility(View.VISIBLE);
                this.newcontentImagePreview.setImageBitmap(bitmap);
                deleteVideo();


                final Uri pathuri = this.mCurrentPhotoPath;
                final Topic imagepreviewTopic;
                final Category imagepreviewCategory;
                final int imagepreviewSource = this.source;


                imagepreviewCategory = this.category;

                imagepreviewTopic = this.topic;


                this.newcontentImagePreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent previewImageIntent = new Intent(v.getContext(), ImageviewPreviewActivity_.class);

                        Bundle extras = new Bundle();
                        if (imagepreviewSource == 1) {
                            extras.putParcelable("group", imagepreviewCategory.getGroup());
                            extras.putBoolean("fromCategory", true);
                        } else if (imagepreviewSource == 2) {
                            extras.putParcelable("group", imagepreviewTopic.getCategory().getGroup());
                        }

                        extras.putString("imagepath", pathuri.toString());
                        extras.putBoolean("isStreamed", false);
                        extras.putBoolean("fromNewContent", true);
                        previewImageIntent.putExtras(extras);
                        v.getContext().startActivity(previewImageIntent);
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {
            this.imageContentFile.delete();

            if (this.imageAcceptedFile != null) {
                this.imageContentFile = this.imageAcceptedFile;
                this.mCurrentPhotoPath = FileProvider.getUriForFile(this, getPackageName() + ".provider", this.imageContentFile);
                this.imageAcceptedFile = null;
            } else {
                this.imageContentFile = null;
                this.mCurrentPhotoPath = null;
            }

        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {

            this.newcontentImageselector.setImageAlpha(127);
            this.newcontentMicselector.setImageAlpha(127);
            this.newcontentVideoselector.setImageAlpha(255);
            this.newcontentImagePreview.setVisibility(View.GONE);
            this.mediatype = 2;

            try {
                this.videoContentFile.createNewFile();
                deleteImage();
            } catch (IOException e) {
                e.printStackTrace();
            }

            setVideoPreview();
            this.newcontentVideoPreview.setBackground(null);
            this.newcontentVideoPreview.setVideoPath(this.videoContentFile.getAbsolutePath());


        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_CANCELED) {
            this.videoContentFile.delete();

            if (this.videoAcceptedFile != null) {
                this.videoContentFile = this.videoAcceptedFile;
                this.mCurrentVideoPath = FileProvider.getUriForFile(this, getPackageName() + ".provider", this.videoContentFile);
                this.videoAcceptedFile = null;
            } else {
                this.videoContentFile = null;
                this.videoAcceptedFile = null;
            }
        }


    }

    private void deleteImage() {
        if (this.imageContentFile != null) {
            this.imageContentFile.delete();
            this.imageContentFile = null;
        }

        if (this.imageAcceptedFile != null) {
            this.imageAcceptedFile.delete();
            this.imageAcceptedFile = null;
        }
    }

    private void deleteVideo() {
        if (this.videoContentFile != null) {
            this.videoContentFile.delete();
            this.videoContentFile = null;
        }

        if (this.videoAcceptedFile != null) {
            this.videoAcceptedFile.delete();
            this.videoAcceptedFile = null;
        }
    }

    void setVideoPreview() {

        this.newcontentVideoPreview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });

        final MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(this.newcontentVideoPreview);
        this.newcontentVideoPreview.setMediaController(mediaController);

        this.newcontentVideoPreview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });

        this.newcontentVideoPreview.setVisibility(View.VISIBLE);


    }


    @Click(R.id.newcontent_send)
    void clickSend() {


        String mediatypeString = "noMediatype";

        if (this.mediatype != 0) {
            switch (this.mediatype) {
                case 1:
                    mediatypeString = "image";
                    break;
                case 2:
                    mediatypeString = "video";
                    break;
                case 3:
                    mediatypeString = "audio";
                    break;
                case 4:
                    mediatypeString = "text";
                    break;
            }

        }

        if (this.source == 1) {

            int selectedRadiobutton = this.newcontent_radiogroup.getCheckedRadioButtonId();
            String type = "noType";
            if (selectedRadiobutton != -1) {
                if (selectedRadiobutton == this.newcontentRadiobuttonexplanation.getId()) {
                    type = "explanation";
                } else if (selectedRadiobutton == this.newcontentRadiobuttonquestion.getId()) {
                    type = "question";
                }
            }

            if (this.imageContentFile != null) {
                this.dataBaseUtilTask.postTopic(this.newcontentName.getText().toString(), "open", type, this.newcontentText.getText().toString(), mediatypeString, this.category.get_id(), this.imageContentFile.getAbsolutePath(), PersistanceDataHandler.getUniqueDatabaseId());
            } else if (this.videoContentFile != null) {
                this.dataBaseUtilTask.postTopic(this.newcontentName.getText().toString(), "open", type, this.newcontentText.getText().toString(), mediatypeString, this.category.get_id(), this.videoContentFile.getAbsolutePath(), PersistanceDataHandler.getUniqueDatabaseId());
            } else if (this.audioContentFile != null) {
                this.dataBaseUtilTask.postTopic(this.newcontentName.getText().toString(), "open", type, this.newcontentText.getText().toString(), mediatypeString, this.category.get_id(), this.audioContentFile.getAbsolutePath(), PersistanceDataHandler.getUniqueDatabaseId());
            } else {
                this.dataBaseUtilTask.postTopic(this.newcontentName.getText().toString(), "open", type, this.newcontentText.getText().toString(), mediatypeString, this.category.get_id(), "", PersistanceDataHandler.getUniqueDatabaseId());
            }


        } else if (source == 2) {
            if (this.imageContentFile != null) {
                this.dataBaseUtilTask.postResponse(this.newcontentName.getText().toString(), this.newcontentText.getText().toString(), mediatypeString, this.topic.get_id(), this.imageContentFile.getAbsolutePath(), PersistanceDataHandler.getUniqueDatabaseId());
            } else if (this.videoContentFile != null) {
                this.dataBaseUtilTask.postResponse(this.newcontentName.getText().toString(), this.newcontentText.getText().toString(), mediatypeString, this.topic.get_id(), this.videoContentFile.getAbsolutePath(), PersistanceDataHandler.getUniqueDatabaseId());
            } else if (this.audioContentFile != null) {
                this.dataBaseUtilTask.postResponse(this.newcontentName.getText().toString(), this.newcontentText.getText().toString(), mediatypeString, this.topic.get_id(), this.audioContentFile.getAbsolutePath(), PersistanceDataHandler.getUniqueDatabaseId());
            } else {
                this.dataBaseUtilTask.postResponse(this.newcontentName.getText().toString(), this.newcontentText.getText().toString(), mediatypeString, this.topic.get_id(), "", PersistanceDataHandler.getUniqueDatabaseId());
            }
        }

        this.newcontent_loadingcircle.setVisibility(View.VISIBLE);
        progressbarAnimation();

    }


    void progressbarAnimation() {
        ObjectAnimator anim = ObjectAnimator.ofInt(this.newcontent_loadingcircle, "progress", 0, 100);
        anim.setDuration(1200);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }


    public void handleCreateTopic(JsonObject postResponseTopic) {
        if (postResponseTopic != null) {
            this.newcontent_loadingcircle.setVisibility(View.GONE);

            //SocketIO Function
            Gson gson = new Gson();

            String addedTopicJson = gson.toJson(postResponseTopic);

            this.learnappSocket.emit("TopicAdded", addedTopicJson);

            Intent openCategoryActivity = new Intent(this, CategoryViewActivity_.class);

         /*   String contentID = null;
            if(!postResponseTopic.get("createdTopic").getAsJsonObject().get("_id").toString().isEmpty()){
                contentID = postResponseTopic.get("createdTopic").getAsJsonObject().get("_id").toString();
            }
            openCategoryActivity.putExtra("contentID", contentID);*/
            openCategoryActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(openCategoryActivity, 0);
            this.finish();


        }
    }


    public void handleCreateResponse(JsonObject postResponseResponse) {
        if (postResponseResponse != null) {


            this.newcontent_loadingcircle.setVisibility(View.GONE);

            //SocketIO Function
            Gson gson = new Gson();
            String addedResponseJson = gson.toJson(postResponseResponse);
            this.learnappSocket.emit("ResponseAdded", addedResponseJson);


            Intent openCategoryActivity = new Intent(this, CategoryViewActivity_.class);
            openCategoryActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(openCategoryActivity, 0);
            this.finish();
        }
    }


    @ViewById(R.id.newcontent_radiogroup)
    RadioGroup newcontent_radiogroup;

    @ViewById(R.id.newcontent_radiobuttonquestion)
    RadioButton newcontentRadiobuttonquestion;

    @ViewById(R.id.newcontent_radiobuttonexplanation)
    RadioButton newcontentRadiobuttonexplanation;

    @ViewById(R.id.newcontent_name)
    EditText newcontentName;

    @ViewById(R.id.newcontent_text)
    EditText newcontentText;

    @ViewById(R.id.newcontent_medialayout)
    LinearLayout newcontentMedialayout;

    @ViewById(R.id.newcontent_imageselector)
    ImageView newcontentImageselector;

    @ViewById(R.id.newcontent_micselector)
    ImageView newcontentMicselector;

    @ViewById(R.id.newcontent_videoselector)
    ImageView newcontentVideoselector;


    @ViewById(R.id.newcontent_image)
    ImageView newcontentImagePreview;

    @ViewById(R.id.newcontent_videoview)
    VideoView newcontentVideoPreview;

    @ViewById(R.id.newcontent_mediaviewlayout)
    LinearLayout newcontentMediaViewLayout;

    @ViewById(R.id.newcontent_send)
    Button newcontentSend;

    @ViewById(R.id.newcontent_recaudio)
    ImageView newcontentRecaudio;

    @ViewById(R.id.newcontent_reclength)
    Chronometer newcontentReclength;

    @ViewById(R.id.newcontent_recaudiocontrolls)
    LinearLayout newcontent_recaudiocontrolls;

    @ViewById(R.id.newcontent_loadingcircle)
    ProgressBar newcontent_loadingcircle;


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
