package com.example.rob.lernapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class UniqueIDHandler {


    private UniqueIDHandler(Activity activity) {

        this.originActivity = activity;
    }

    private static UniqueIDHandler instance = null;
    final Activity originActivity;
    final private String FILENAME = "LearnappUniqueID";
    private String installationID;

    static public UniqueIDHandler getInstance(Activity activity) {
        if (instance == null) {
            instance = new UniqueIDHandler(activity);
            return instance;
        } else {
            return instance;
        }
    }


    public String handleUniqueID() throws IOException {

        File idFile = new File(this.originActivity.getFilesDir(), this.FILENAME);

        if(idFile.exists()){
            idIdFileExists(idFile);
        }else{
            ifIdFileDoesntExists(idFile);
        }
        return this.installationID;
    }

    private void idIdFileExists(File idFile) throws IOException {
        InputStream inputStream = this.originActivity.openFileInput(this.FILENAME);
        if(inputStream != null){

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receivedID = null;
            StringBuilder stringBuilder = new StringBuilder();

            while ( (receivedID = bufferedReader.readLine()) != null ) {
                stringBuilder.append(receivedID);
            }

            inputStream.close();
            this.installationID = stringBuilder.toString();


        }else{
            Log.e("inputStream", "Kann die Datei nicht lesen:  " + this.FILENAME);
        }
    }

    private void ifIdFileDoesntExists(File idFile) throws IOException {
        createNewUniqueID();
        if(!this.installationID.isEmpty()){
            idFile.createNewFile();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.originActivity.openFileOutput(this.FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(this.installationID);
            outputStreamWriter.close();
        }
    }

    private void createNewUniqueID(){
        if(this.installationID == null){
            String uniqueID = UUID.randomUUID().toString();
            this.installationID = uniqueID;
        }
    }


}
