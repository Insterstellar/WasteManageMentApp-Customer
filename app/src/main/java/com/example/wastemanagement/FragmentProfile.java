package com.example.wastemanagement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.media.MediaRecorder.VideoSource.CAMERA;
import static android.view.View.INVISIBLE;

public class FragmentProfile extends Fragment {


    private EditText mNameField, mEmailField,city,division,country;

    private Button mConfirm;

    private ImageView mProfileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;

    private String userID;
    private String mName;
    private String mEmailFiel;
    private String mProfileImageUrl;
    public String mCity;
    public String mDivision;
    public String mCountry;
  private  ProgressDialog dialog;

    private Uri resultUri;
    private static final int CAMERA_REQUEST = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile,container,false);

        mProfileImage=view.findViewById(R.id.user_profile_photo);
        mNameField =view.findViewById(R.id.text_name);
        mEmailField=view.findViewById(R.id.editTextTextPersonName);
        city=view.findViewById(R.id.edit_text_city);
        division=view.findViewById(R.id.edit_division);
        country =view.findViewById(R.id.edit_country);
        mConfirm =view.findViewById(R.id.button_sign_update);


        //creating firebase instances
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userID);
        getUserInfo();


        mProfileImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(Intent.ACTION_PICK);
               intent.setType("image/*");
               startActivityForResult(intent, 1);
           }
       });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Updating...");
               // mConfirm.setText("Updated");
                dialog.show();
                saveUserInformation();
            }
        });

        return view;
    }
//getting user information
    private void saveUserInformation() {
        mName = mNameField.getText().toString();
        mEmailFiel = mEmailField.getText().toString();
        mDivision = division.getText().toString();
        mCity = city.getText().toString();
        mCountry= country.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("name", mName);
        userInfo.put("email", mEmailFiel);
        userInfo.put("city", mCity);
        userInfo.put("country", mCountry);
        userInfo.put("division", mDivision);
        mCustomerDatabase.updateChildren(userInfo);

        if(resultUri != null) {

            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userID);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);

          /*  uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    getActivity().finish();
                    return;
                }
            });*/

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                    Map newImage = new HashMap();
                    newImage.put("profileImageUrl", uri.toString());
                    mCustomerDatabase.updateChildren(newImage);
                    Toast.makeText(getActivity(),"updated",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                  //  getActivity(). finish();
                    return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            dialog.dismiss();
                            mConfirm.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(),"failed try again",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                }
            });}}

/*
*
* dialog.dismiss();
            mConfirm.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(),"updated",Toast.LENGTH_SHORT).show();
*
* */

    private void getUserInfo() {
        mCustomerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        mName = map.get("name").toString();
                        mNameField.setText(mName);
                    }
                    if(map.get("email")!=null){
                        mEmailFiel = map.get("email").toString();
                        mEmailField.setText(mEmailFiel);
                    }

                    if(map.get("city")!=null) {
                        mCity = map.get("city").toString();
                        city.setText(mCity);
                    }

                    if(map.get("division")!=null) {
                        mDivision = map.get("division").toString();
                        division.setText(mDivision);
                    }

                    if(map.get("country")!=null) {
                        mCountry = map.get("country").toString();
                        country.setText(mCountry);
                    }
                    if(map.get("profileImageUrl")!=null){
                        mProfileImageUrl = map.get("profileImageUrl").toString();
                      //  Glide.with(getActivity().getApplication()).load(mProfileImageUrl).into(mProfileImage);
                        Picasso.with(getActivity())
                                .load(mProfileImageUrl)
                                .fit()
                                .centerCrop()
                                .into(mProfileImage);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);

        }
    }

}
