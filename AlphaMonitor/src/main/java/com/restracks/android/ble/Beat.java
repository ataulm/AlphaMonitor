package com.restracks.android.ble;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by mvklingeren on 11/4/2014.
 */
public class Beat {
    @SerializedName("rate")
    private int rate;

    @SerializedName("datetime")
    private Date dateTime;

    //@SerializedName("location")
    //private location;

    public Beat(int rate, Date dateTime){
        this.rate = rate;
        this.dateTime=dateTime;
    }
}
