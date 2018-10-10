package com.example.rob.lernapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_start)
public class StartActivity extends AppCompatActivity {


    @Click(R.id.button)
    void clickbutton(){
        Toast.makeText(getApplicationContext(), "AMK", Toast.LENGTH_SHORT).show();
    }
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    */
}
