package com.example.textrecognizerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;
    TextView detectedText;
    Button btn_detect,btn_capture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=(ImageView)findViewById(R.id.img);
        detectedText=(TextView)findViewById(R.id.text);
        btn_detect=(Button)findViewById(R.id.btn);
        btn_capture=(Button)findViewById(R.id.btn1);

        btn_capture.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dispatchTakePictureIntent();
             }
         });

        btn_detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detect();
            }
        });
    }

    private void dispatchTakePictureIntent () {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }

    }
    
    public void detect()
    {
        //perform text detection here

        //1. define TextRecognizer
        TextRecognizer recognizer=new TextRecognizer.Builder(MainActivity.this).build();

        //2. Get bitmap from imageview
        Bitmap bitmap =((BitmapDrawable)imageView.getDrawable()).getBitmap();

        //3. get frame from bitmap
        Frame frame=new Frame.Builder().setBitmap(bitmap).build();

        //4. get data from frame
        SparseArray<TextBlock> sparseArray=recognizer.detect(frame);

        //5. set data on textview
        StringBuilder stringBuilder = new StringBuilder();

        for (int i=0; i<sparseArray.size(); i++)
        {
            TextBlock tx=sparseArray.get(i);
            String str=tx.getValue();
            stringBuilder.append(str);
        }
        detectedText.setText(stringBuilder);
    }
}