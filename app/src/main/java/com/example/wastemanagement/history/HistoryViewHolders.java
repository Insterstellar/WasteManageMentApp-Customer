package com.example.wastemanagement.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wastemanagement.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView rideId;
    public TextView time;
    public TextView itemType;
    public TextView kilgram;
    public ImageView mImageUrl;

    public HistoryViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        rideId = (TextView) itemView.findViewById(R.id.rideId);
        time = (TextView) itemView.findViewById(R.id.time);
        itemType=(TextView)itemView.findViewById(R.id.itemType);
        kilgram=(TextView) itemView.findViewById(R.id.kilograms);
         mImageUrl=(ImageView) itemView.findViewById(R.id.image_vieww);
    }

    @Override
    public void onClick(View v) {
        /*Intent intent = new Intent(v.getContext(), HistorySingleActivity.class);
        Bundle b = new Bundle();
        b.putString("rideId", rideId.getText().toString());
        intent.putExtras(b);
        v.getContext().startActivity(intent);*/
    }
}
