package com.example.sajidsalman75.nearby.Model;

/**
 * Created by sajidsalman75 on 12/6/2017.
 */

public class Time {
    private int sec, min, hour;

    public Time(){
        this.sec = 0;
        this.min = 0;
        this.hour = 0;
    }

    public Time(int sec, int min, int hour) {
        this.sec = sec;
        this.min = min;
        this.hour = hour;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void secToHours(int sec1){
        int min1 = sec1 / 60;
        min = min1 % 60;
        hour = min1 - min;
        hour = hour / 60;
    }

    public void hourToSec(int hour1, int min1){
        sec = hour1 * 60 * 60 + min1 * 60;
    }
}
