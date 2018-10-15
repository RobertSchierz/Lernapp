package com.example.rob.lernapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.widget.Toast;

import static android.support.v4.content.ContextCompat.*;

public class GetContacts {

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    final Activity originActivity;
    private static GetContacts instance = null;
    public boolean hasPermission;


    private GetContacts(Activity originActivity) {
        this.originActivity = originActivity;
    }


    static public GetContacts getInstance(Activity activity) {
        if (instance == null) {
            instance = new GetContacts(activity);
            return instance;
        } else {
            return instance;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void handleDialog(boolean response){
        if(response){
            this.originActivity.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, this.MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void handlePermission() {

        hasPermissionSetter();


        if (this.hasPermission == false) {
            if (this.originActivity.shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {


                ContactPermissionDialog contactPermissionDialog = ContactPermissionDialog.getInstance();
                contactPermissionDialog.showdialog(this.originActivity);

            } else {
                this.originActivity.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, this.MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {

            Toast.makeText(this.originActivity.getApplicationContext(), "Berechtigung bereits erlaubt", Toast.LENGTH_SHORT).show();
            readContacts();
        }

    }


    private void hasPermissionSetter() {
        if (getContactPermission()) {
            this.hasPermission = true;
        } else {
            this.hasPermission = false;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean getContactPermission() {

        if (this.originActivity.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }

    }


    public void readContacts() {
        ContentResolver contentResolver = this.originActivity.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Log.i("test", cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            }
        }
    }


}
