package com.androidtutorialshub.loginregister;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.view.Window;
import android.view.WindowManager;
import android.content.*;

import java.io.FileNotFoundException;


public class CameraActivity extends AppCompatActivity {

    Button btnpic;
    ImageView imgpic;
    TextView textTargetUri;
    ImageView targetImage;
    Button loadimage;

    private static final int CAM_REQUEST =1313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);

        btnpic = (Button) findViewById(R.id.button);
        imgpic = (ImageView)findViewById(R.id.image);
        btnpic.setOnClickListener(new btnTakePhotoClicker());
        Button buttonLoadImage = (Button)findViewById(R.id.loadimage);
        textTargetUri = (TextView)findViewById(R.id.targeturi);
        targetImage = (ImageView)findViewById(R.id.image);


        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }});

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode,data);

        if (requestCode==CAM_REQUEST)
        {
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imgpic.setImageBitmap(bitmap);


        }
        else if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            textTargetUri.setText(targetUri.toString());

            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                imgpic.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }
    class btnTakePhotoClicker implements Button.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAM_REQUEST);

        }

    }

}

