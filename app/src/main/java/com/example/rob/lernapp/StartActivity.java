package com.example.rob.lernapp;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.rob.lernapp.restdata.Dataset;
import com.example.rob.lernapp.restdata.User;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

@EActivity(R.layout.activity_start)
public class StartActivity extends AppCompatActivity {

    User[] userinfos;
    GetContacts getContacts;



    @RestService
    RestClient restClient;

    @Background
    void getAsynchronous() {
        try {

            ResponseEntity<Dataset> responseEntity = restClient.getUsers();


            Dataset dataSet = responseEntity.getBody();
            this.userinfos = dataSet.gettingUsers();
            //showRequest();
            getContactExec();





        } catch (RestClientException e) {
            Log.e("Rest error", e.toString());
        }
    }

    @UiThread
    void showRequest(){
        Toast.makeText(getApplicationContext(), this.userinfos[1].getName(), Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void getContactExec(){
        if(Build.VERSION.SDK_INT >= 23){
            this.getContacts = GetContacts.getInstance(this);
            this.getContacts.handlePermission();
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        switch (requestCode){
            case GetContacts.MY_PERMISSIONS_REQUEST_READ_CONTACTS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this.getApplicationContext(), "Berechtigung erlaubt", Toast.LENGTH_SHORT).show();
                    this.getContacts.readContacts();

                }else{
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this.getApplicationContext(), "Berechtigung verweigert", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Click(R.id.button)
    void clickbutton() {
        getAsynchronous();
    }





}
