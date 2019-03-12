package com.pig.original.watch;

public class WatchOxygen {
    private float bloodOxygen;
    private long date;


    public WatchOxygen(float bloodoxygen, long date) {
        super();
        this.bloodOxygen = bloodoxygen;
        this.date = date;
    }


    public float getBloodoxygen() {
        return bloodOxygen;
    }


    public void setBloodoxygen(float bloodoxygen) {
        this.bloodOxygen = bloodoxygen;
    }


    public long getDate() {
        return date;
    }


    public void setDate(long date) {
        this.date = date;
    }


}
