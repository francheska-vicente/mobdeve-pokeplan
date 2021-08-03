package com.mobdeve.s11.pokeplan;

public class Timer {
    private int hours;
    private int mins;
    private int secs;

    public Timer() {
        this.hours = 0;
        this.mins = 10;
        this.secs = 0;
    }

    public Timer(int hours, int mins, int secs) {
        this.hours = hours;
        this.mins = mins;
        this.secs = secs;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMins(int mins) {
        this.mins = mins;
    }

    public void setSecs(int secs) {
        this.secs = secs;
    }

    public int getHours() {
        return hours;
    }

    public int getMins() {
        return mins;
    }

    public int getSecs() {
        return secs;
    }
}
