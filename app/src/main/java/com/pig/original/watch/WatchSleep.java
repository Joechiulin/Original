package com.pig.original.watch;

public class WatchSleep {
    private float sleep;
    private float deepSleep;
    private long date;

    public WatchSleep(){}
    public WatchSleep(float sleep, float deepSleep, long date) {
        this.sleep = sleep;
        this.deepSleep = deepSleep;
        this.date = date;
    }

    public float getSleep() {
        return sleep;
    }

    public void setSleep(float sleep) {
        this.sleep = sleep;
    }

    public float getDeepSleep() {
        return deepSleep;
    }

    public void setDeepSleep(float deepSleep) {
        this.deepSleep = deepSleep;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
