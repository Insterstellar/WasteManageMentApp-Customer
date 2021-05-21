package com.example.wastemanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RequestPickUp extends AppCompatActivity {
    ImageButton sub;
    private static final int CAMERA_REQUEST = 1888;
    //https://github.com/rubel007cse/TuntuniNews/blob/master/app/src/main/java/com/mrubel/tuntuninews/AddingNews.java
    ImageButton  Addi;
    TextView amountt;
    TextView notes;
    TextView photoTaken;
    TextView total;
    ImageView imageView;
    Button submit;
    Bitmap finalPic;
   private float amounttt=0;
  float finalweight;
   ConstraintLayout takePic;
    private AlphaAnimation buttonClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_pick_up);
        //find views
        buttonClick = new AlphaAnimation(1F, 0.8F);
        sub=findViewById(R.id.subtract);
        Addi=findViewById(R.id.addition);
        notes=findViewById(R.id.editmap);
        imageView = (ImageView) this.findViewById(R.id.imageView8);
        amountt=findViewById(R.id.amount);
        total=findViewById(R.id.textView10);
        takePic=findViewById(R.id.picture);
        submit=findViewById(R.id.submitt);
        photoTaken=findViewById(R.id.textView17);

//final content submition of all items
submit.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {

        v.setAnimation(buttonClick);
        Toast.makeText(RequestPickUp.this, String.valueOf(finalweight) ,Toast.LENGTH_SHORT).show();
        //finalweight;
        //finalPic;
    //String finalnotes=notes.getText().toString();
        }
        });
//select weight of trash wen clicked decrement
        sub.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {

        amounttt--;
        amountt.setText(String.valueOf(amounttt));
        total.setText(String.valueOf(amounttt));
    finalweight=amounttt;
        }
        });
//select weight of trash wen clicked increment
        Addi.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        amounttt++;
        amountt.setText(String.valueOf(amounttt));
        total.setText(String.valueOf(amounttt));
    finalweight=amounttt;
        }
        });

        //take picture
        takePic.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
        });
        }

protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        finalPic=photo;
        imageView.setImageBitmap(photo);
        photoTaken.setTextColor(Color.RED);
        photoTaken.setText("Photo Taken, proceed to fill in and submit");
        }
        }
@Override
public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.request_pick_up, menu);
        return true;
        }

        }
