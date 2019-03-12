package com.pig.original.watch;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WatchHeart {
    private float heart;
    private long date;
    public WatchHeart(){}


    public WatchHeart(float heart, long date) {
        super();
        this.heart = heart;
        this.date = date;
    }


    public float getHeart() {
        return heart;
    }


    public void setHeart(float heart) {
        this.heart = heart;
    }


    public long getDate() {
        return date;
    }


    public void setDate(long date) {
        this.date = date;
    }


    public String getFormatedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "HH-mm-ss" );
        return dateFormat.format(new Date(date));
    }
}
