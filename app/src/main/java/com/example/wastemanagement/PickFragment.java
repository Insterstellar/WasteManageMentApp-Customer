package com.example.wastemanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wastemanagement.history.Upload;
import com.example.wastemanagement.trash.CountryAdapter;
import com.example.wastemanagement.trash.CountryItem;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.Images.Media.getBitmap;

public class PickFragment extends Fragment {




    ImageButton sub;


    private static final int CAMERA_REQUEST = 1888;

    //images
    private ImageButton Addi;
    private TextView amountt;
    private TextView photoTaken;
    private TextView total;
    private Button submit;
    private ConstraintLayout takePic;
    private AlphaAnimation buttonClick;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    //items to be sent

    int weights;

    private EditText mynotes;
    private float amounttt = 0;
    Spinner spinnerTrash;
    private  ImageView imageView;
    Map userInfo;

    String mynoteM;
    String amountttM ;
    String spinnerTrashM;
    String  imageViewM;

    private Uri resultUri;
   private String trashname;
     StorageReference filePath;
    String userid;
//working on spinner
private ArrayList<CountryItem> mCountryList;
    private CountryAdapter mAdapter;

    ProgressDialog progressDialog ;

    //firebase
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
     ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.request_pick_up, container, false);
        weights= (int) amounttt;
        initList();
       // PickFragment pickme=new PickFragment();

//initialise spinner adapatr
         spinnerTrash=view.findViewById(R.id.spineMe);
        mAdapter = new CountryAdapter(getContext(), mCountryList);
        spinnerTrash.setAdapter(mAdapter);
        userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        //firebase instances
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userid);
        progressDialog = new ProgressDialog(getContext());

//find views
        buttonClick = new AlphaAnimation(1F, 0.8F);
        sub = view.findViewById(R.id.subtract);
        Addi = view.findViewById(R.id.addition);

        imageView = (ImageView) view.findViewById(R.id.imageView8);
        amountt = view.findViewById(R.id.amount);

        total = view.findViewById(R.id.textView10);

        takePic = view.findViewById(R.id.picture);
        submit = view.findViewById(R.id.submitt);
        photoTaken=view.findViewById(R.id.textView17);
        mProgressBar =view.findViewById(R.id.progress_bar);

        //to be sent

        mynotes=view.findViewById(R.id.editmap);



//upload interruption
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // mConfirm.setText("Updated");
                dialog.show();

            }
        });

        //initialise spinner for picking items
        spinnerTrash.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryItem clickedItem = (CountryItem) parent.getItemAtPosition(position);
                 trashname = clickedItem.getCountryName();
                Toast.makeText(getActivity(), trashname + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//select weight of trash wen clicked decrement
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amounttt--;
                amountt.setText(String.valueOf(amounttt));
                total.setText(String.valueOf(amounttt));

            }
        });
//select weight of trash wen clicked increment
        Addi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amounttt++;
                amountt.setText(String.valueOf(amounttt));
                total.setText(String.valueOf(amounttt));

            }
        });


        //take picture
        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }


        });

        //submition of all clients items
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setAnimation(buttonClick);
                submit.setVisibility(View.GONE);
                uploadFile();
               // Toast.makeText(getActivity(), String.valueOf(amounttt), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            imageView.setImageURI(resultUri);


    }

        }
    private String getFileExtension(Uri uri) {
        ContentResolver cR =  getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void initList() {

        mCountryList = new ArrayList<>();
        mCountryList.add(new CountryItem("Papers", R.drawable.paper));
        mCountryList.add(new CountryItem("Plastic", R.drawable.plastic));
        mCountryList.add(new CountryItem("Glasses", R.drawable.glass));
        mCountryList.add(new CountryItem("Organic", R.drawable.organic));
        mCountryList.add(new CountryItem("Metal", R.drawable.metal));
    }

    private void uploadFile() {

        mynoteM=mynotes.getText().toString();
        amountttM=String.valueOf(amounttt) ;
         spinnerTrashM=trashname;

       //userInfo = new HashMap();



        pictureUpload();

        }

        public void pictureUpload(){

            if(resultUri != null) {
                dialog=new ProgressDialog(getActivity());
                dialog.setMessage("Initialising map for a pick up request...");
                dialog.show();


                //StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                   //     + "." );
                filePath = FirebaseStorage.getInstance().getReference().child("uploads").child(userid).child(System.currentTimeMillis()+".");
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

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        submit.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(),"failed try again",Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Map newImage = new HashMap();
                                newImage.put("luggauge", uri.toString());
                                newImage.put("notes",mynoteM);
                                newImage.put("amount",amountttM);
                                newImage.put("items",spinnerTrashM);
                              //  mDatabaseRef.updateChildren(newImage);

                              // String  uploadId = mDatabaseRef.push().getKey();
                                mDatabaseRef.updateChildren(newImage);


                                Toast.makeText(getContext(),"updated",Toast.LENGTH_SHORT).show();
                                 dialog.dismiss();
                                Fragment someFragment = new MapsFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container, someFragment ); // give your fragment container id in first parameter
                                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                                transaction.commit();
                                //  getActivity(). finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                                return;
                            }
                        });
                    }
                });}}

    }












