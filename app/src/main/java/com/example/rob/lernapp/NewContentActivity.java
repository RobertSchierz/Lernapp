package com.example.rob.lernapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.VideoView;

import com.example.rob.lernapp.databaseUtilityClasses.DatabaseUtilityNewContent;
import com.example.rob.lernapp.restdataGet.Category;
import com.example.rob.lernapp.restdataGet.Topic;
import com.example.rob.lernapp.restdataPost.PostResponse;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@EActivity(R.layout.activity_new_content)
public class NewContentActivity extends AppCompatActivity {

    Topic topic;
    Category category;

    int source = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    Uri mCurrentPhotoPath;
    int mediatype = 0;
    File contentFile;
    File acceptedFile;

    @NonConfigurationInstance
    @Bean
    DatabaseUtilityNewContent dataBaseUtilTask;


    @AfterViews
    void onCreate() {

        if (getIntent().getExtras() != null) {

            Bundle bundle = getIntent().getExtras();

            if (bundle.getParcelable("topic") != null) {
                this.topic = bundle.getParcelable("topic");
                this.source = 1;
            }

            if (bundle.getParcelable("category") != null) {
                this.category = bundle.getParcelable("category");
                this.source = 2;
            }

            this.newcontent_imageselector.setImageAlpha(127);
            this.newcontentMicselector.setImageAlpha(127);
            this.newcontentVideoselector.setImageAlpha(127);
        }

        if(source == 2){
            newcontent_radiogroup.setVisibility(View.GONE);
        }else if (source == 1){
            newcontent_radiogroup.setVisibility(View.VISIBLE);
        }


        setTitle(getResources().getText(R.string.contentactivitylabel) + " für - " + this.topic.getName());

    }

    @TextChange(R.id.newcontent_name)
    void nameChange(){
        checkElements();
    }

    @TextChange(R.id.newcontent_text)
    void textChange(){
        checkElements();
    }

    @CheckedChange(R.id.newcontent_radiobuttonquestion)
    void radioQuestionChange(){
        checkElements();
    }

    @CheckedChange(R.id.newcontent_radiobuttonexplanation)
    void radioExplanationChange(){
        checkElements();
    }


    private void checkElements(){
        if(!newcontentName.getText().toString().isEmpty() &&
                !newcontentText.getText().toString().isEmpty() &&
                (newcontentRadiobuttonquestion.isChecked() || newcontentRadiobuttonexplanation.isChecked())){
            newcontentSend.setEnabled(true);
            if(this.contentFile == null){
                this.mediatype = 4;
            }
        }else{
            newcontentSend.setEnabled(false);
        }
    }


    @Click(R.id.newcontent_imageselector)
    void clickImage(){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                if(this.contentFile != null){
                    this.acceptedFile = this.contentFile;
                }
                photoFile = createImageFile();
                this.contentFile = photoFile;

            } catch (IOException ex) {
                Log.v("Photo", ex.getMessage());
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, this.mCurrentPhotoPath);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            this.newcontent_imageselector.setImageAlpha(255);
            this.newcontentMicselector.setImageAlpha(127);
            this.newcontentVideoselector.setImageAlpha(127);
            this.mediatype = 1;

            try {
                this.contentFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), this.mCurrentPhotoPath);
                this.newcontentImagePreview.setVisibility(View.VISIBLE);
                this.newcontentImagePreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }



        }else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED){
           this.contentFile.delete();

           if(this.acceptedFile != null){
               this.contentFile = this.acceptedFile;
               this.mCurrentPhotoPath = FileProvider.getUriForFile(this, getPackageName() + ".provider",this.contentFile);
               this.acceptedFile = null;
           }else{
               this.contentFile = null;
               this.mCurrentPhotoPath = null;
           }

        }
    }


    @Click(R.id.newcontent_send)
    void clickSend(){



        if(this.source == 1){

            int selectedRadiobutton = this.newcontent_radiogroup.getCheckedRadioButtonId();
            String type = "noType";
            if(selectedRadiobutton != -1){
                if(selectedRadiobutton == this.newcontentRadiobuttonexplanation.getId()){
                    type = "explanation";
                }else if(selectedRadiobutton == this.newcontentRadiobuttonquestion.getId()){
                    type = "question";
                }
            }

            String mediatypeString = "noMediatype";

            if(this.mediatype != 0){
                switch (this.mediatype){
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

            if(this.contentFile == null){
                this.dataBaseUtilTask.postTopic(this.newcontentName.getText().toString(), "open", type, this.newcontentText.getText().toString(), mediatypeString, this.topic.getCategory().get_id(), "", PersistanceDataHandler.getUniqueDatabaseId());
            }else{
                this.dataBaseUtilTask.postTopic(this.newcontentName.getText().toString(), "open", type, this.newcontentText.getText().toString(), mediatypeString, this.topic.getCategory().get_id(), this.contentFile.getAbsolutePath(), PersistanceDataHandler.getUniqueDatabaseId());
            }

        }

    }


    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Learnapp_media");
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );


        this.mCurrentPhotoPath = FileProvider.getUriForFile(this, getPackageName() + ".provider",image);
        return image;
    }

    public void handleCreateTopic(PostResponse postResponse) {

        if(postResponse != null){

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
    ImageView newcontent_imageselector;

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


}
