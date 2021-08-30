package com.mobdeve.s11.pokeplan;

import java.util.concurrent.TimeUnit;

public class Timer {
    private int hours;
    private int mins;
    private int secs;

    /**
     * Class constructor with default value of 10 minutes
     */
    public Timer() {
        this.hours = 0;
        this.mins = 10;
        this.secs = 0;
    }

    /**
     * Class constructor with custom values
     * @param hours the hours of the timer, should be a value from 0-99
     * @param mins the minutes of the timer, should be a value from 0-60
     * @param secs the seconds of the timer, should be a value from 0-60
     */
    public Timer(int hours, int mins, int secs) {
        this.hours = hours;
        this.mins = mins;
        this.secs = secs;
    }


    /**
     * @return hours stored in the timer
     */
    public int getHours() {
        return hours;
    }

    /**
     * @return minutes stored in the timer
     */
    public int getMins() {
        return mins;
    }

    /**
     * @return seconds stored in the timer
     */
    public int getSecs() {
        return secs;
    }

    /**
     * @param hours number of hours to set the timer with
     */
    public void setHours(int hours) {
        this.hours = hours;
    }

    /**
     * @param mins number of minutes to set the timer with
     */
    public void setMins(int mins) {
        this.mins = mins;
    }

    /**
     * @param secs number of seconds to set the timer with
     */
    public void setSecs(int secs) {
        this.secs = secs;
    }

    /**
     * Converts the time stored in the timer to milliseconds
     * @return the time stored in the timer in milliseconds
     */
    public long convertToMilliseconds() {
        return TimeUnit.MILLISECONDS.convert(hours, TimeUnit.HOURS) +
                TimeUnit.MILLISECONDS.convert(mins, TimeUnit.MINUTES) +
                TimeUnit.MILLISECONDS.convert(secs, TimeUnit.SECONDS);
    }
}
