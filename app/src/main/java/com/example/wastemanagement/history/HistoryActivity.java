package com.example.wastemanagement.history;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wastemanagement.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends Fragment {
    private String customerOrDriver, userId;

    private RecyclerView mHistoryRecyclerView;
    private RecyclerView.Adapter mHistoryAdapter;
    private RecyclerView.LayoutManager mHistoryLayoutManager;
TextView mytextt;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_history,container,false);
       mytextt=view.findViewById(R.id.kilograms);
        mHistoryRecyclerView = view. findViewById(R.id.historyRecyclerView);
        mHistoryRecyclerView.setNestedScrollingEnabled(false);
        mHistoryRecyclerView.setHasFixedSize(true);
        mHistoryLayoutManager = new LinearLayoutManager(getContext());
        mHistoryRecyclerView.setLayoutManager(mHistoryLayoutManager);
        mHistoryAdapter = new HistoryAdapter(getDataSetHistory(), getContext());
        mHistoryRecyclerView.setAdapter(mHistoryAdapter);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getUserHistoryIds();

        return view;
    }
    private ArrayList resultsHistory = new ArrayList<HistoryObject>();
    private List<HistoryObject> getDataSetHistory() {
        return resultsHistory;
    }
    private void getUserHistoryIds() {
        DatabaseReference userHistoryDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userId).child("history");
        userHistoryDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot history : dataSnapshot.getChildren()){
                        FetchRideInformation(history.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void FetchRideInformation(String rideKey) {
        DatabaseReference historyDatabase = FirebaseDatabase.getInstance().getReference().child("history").child(rideKey);
        historyDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String rideId= null;
                    String itemType = null;
                    String kilgram = null;
                    String mImageUrl=null;


                    if(dataSnapshot.child("items").getValue() != null) {
                         rideId = dataSnapshot.child("items").getValue().toString();
                    }

                   if(dataSnapshot.child("weight").getValue() != null) {
                          itemType = dataSnapshot.child("weight").getValue().toString();

                   }


                    if(dataSnapshot.child("notes").getValue() != null) {
                         kilgram = dataSnapshot.child("notes").getValue().toString();

                    }
                    if(dataSnapshot.child("myImaging").getValue() != null) {
                        mImageUrl= (String) dataSnapshot.child("myImaging").getValue();
                    }

                    Long timestamp = 0L;
                    String distance = "";
                    Double ridePrice = 0.0;

                    if(dataSnapshot.child("timestamp").getValue() != null){
                        timestamp = Long.valueOf(dataSnapshot.child("timestamp").getValue().toString());
                    }


                    HistoryObject obj = new HistoryObject(rideId, getDate(timestamp),kilgram,itemType,mImageUrl);
                    resultsHistory.add(obj);
                    mHistoryAdapter.notifyDataSetChanged();


                }
    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}
    private String getDate(Long time) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time*1000);
        String date = DateFormat.format("MM-dd-yyyy hh:mm", cal).toString();
        return date;
    }


}