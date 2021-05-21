package com.example.wastemanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

//import FireBaseAuthentication;
public class SignIn extends AppCompatActivity {


  ImageView myProfilePic;
    private ProgressDialog dialog;

Button but;
 TextView admin;
    FirebaseUser currentUser;
    private AlphaAnimation buttonClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        but=findViewById(R.id.buttonSign);
        admin=findViewById(R.id.editTextTextPersonName);


       buttonClick = new AlphaAnimation(1F, 0.8F);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //if user has signed in move to verification part.
        if (currentUser != null) {
            String myNumber=currentUser.getPhoneNumber();

            Intent intent = new Intent(SignIn.this, MainActivity.class);
            intent.putExtra("mY_number",myNumber);
            startActivity(intent);
            finish();}
            else{


//open signIn part
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                String mobileNo = admin.getText().toString().trim();
                if (mobileNo.isEmpty() || mobileNo.length() < 12) {
                    admin.setError("Enter a valid mobile");
                    admin.requestFocus();
                    return;
                }

                Intent intent = new Intent(SignIn.this, VerifyPhoneActivity.class);
                intent.putExtra("mobile", mobileNo);
                startActivity(intent);



            }
        });
        }
//open signup activity.

    }
    public void fullRegestration(){


    }
}
/*
*  firebaseAuth.signOut();
                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(intent);
*
* */