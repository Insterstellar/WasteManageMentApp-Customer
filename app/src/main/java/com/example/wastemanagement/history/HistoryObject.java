package com.example.wastemanagement.history;

public class HistoryObject {
    private String rideId;
    private String time;
    private String itemType;
    private String kilgram;
    private String mImageUrl;



    public HistoryObject(String rideId, String time,String itemType,String kilgram,String mImageUrl){
        this.rideId = rideId;
        this.time = time;
        this.itemType = itemType;
        this.kilgram = kilgram;
        this.mImageUrl = mImageUrl;
    }

    public String getRideId(){return rideId;}
    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getTime(){return time;}
    public void setTime(String time) {
        this.time = time;
    }
    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getKilgram() {
        return kilgram;
    }

    public void setKilgram(String kilgram) {
        this.kilgram = kilgram;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
