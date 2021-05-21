package com.example.wastemanagement.forsign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
//facebook imports
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

//keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | "C:\Users\Mandy\Desktop\wastemangment\bin\openssl.exe"sha1 -binary | "C:\Users\Mandy\Desktop\wastemangment\bin\openssl.exe" base64

import com.example.wastemanagement.R;

public class FaceBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_book);
    }
}