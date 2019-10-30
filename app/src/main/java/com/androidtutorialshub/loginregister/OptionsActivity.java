package com.androidtutorialshub.loginregister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }

    public void speech(View view){
        Intent intent=new Intent(this,SpeechActivity.class);
        startActivity(intent);
    }
    public void camera(View view){
        Intent intent=new Intent(this,CameraActivity.class);
        startActivity(intent);
    }
    public void hospitals(View view){
        Intent intent=new Intent(this,HospitalActivity.class);
        startActivity(intent);
    }
}
