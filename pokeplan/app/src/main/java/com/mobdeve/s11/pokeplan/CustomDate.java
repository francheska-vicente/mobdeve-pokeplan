package com.mobdeve.s11.pokeplan;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class CustomDate {
    private static final String[] monthString = new String[]{"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private final int day_in_month, month, year, hour, minute;

    public CustomDate () {
        Calendar c = Calendar.getInstance();
        this.year = c.get(Calendar.YEAR);
        this.day_in_month = c.get(Calendar.DAY_OF_MONTH);
        this.month = c.get(Calendar.MONTH) + 1;
        this.hour = c.get(Calendar.HOUR_OF_DAY);
        this.minute = c.get(Calendar.MINUTE);
    }

    public CustomDate(int year, int month, int day_in_month, int hour, int minute) {
        if (year <= 1000)
            this.year = 2000 + year;
        else
            this.year = year;

        this.day_in_month = day_in_month;
        this.month = month;
        this.hour = hour;
        this.minute = minute;

    }

    public int getDay () {
        return this.day_in_month;
    }

    public int getMonth () {
        return this.month;
    }

    public int getYear () {
        return this.year;
    }

    public int getHour () {
        return this.hour;
    }

    public int getMinute () {
        return this.minute;
    }

    @Override
    public String toString() {
        String tempTime = new DecimalFormat("00").format(hour) + ":" + new DecimalFormat("00").format(minute);
        String tempDate = this.day_in_month + "." + this.month + "." + this.year;
        Log.d("Hello pare inside custom date", tempDate);
        return printData(tempDate, tempTime);
    }

    public String storeData () {
        String tempTime = new DecimalFormat("00").format(this.hour) + ":" + new DecimalFormat("00").format(this.minute);
        String tempDate = new DecimalFormat("00").format(this.day_in_month) + "." + new DecimalFormat("00").format(this.month) + "." + this.year;
        return tempDate + " " + tempTime;
    }

    public static String printData (String dateToConvert, String time) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        int currentYear = c.get(Calendar.YEAR);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        int currentMonth = c.get(Calendar.MONTH) + 1;

        String [] tempDate = dateToConvert.split("\\.");
        String [] tempTime = time.split(":");
        int month = Integer.valueOf(tempDate[1]);
        int day_in_month = Integer.valueOf(tempDate[0]);
        int year = Integer.valueOf(tempDate[2]);
        int hour = Integer.valueOf(tempTime[0]);
        int minute = Integer.valueOf(tempTime[1]);

        String date = "";
        long diff = 0;
        try {
            Date dateStart = simpleDateFormat.parse(currentDay + "." + currentMonth + "." + currentYear);
            Date dateEnd = simpleDateFormat.parse(dateToConvert);

            diff = dateEnd.getTime() - dateStart.getTime();
            diff = TimeUnit.MILLISECONDS.toDays(diff);

            if (diff == 0) {
                date = "Today @ ";
            } else if (diff <= 6 && diff > 0) {
                Locale locale = new Locale("EN", "PHILIPPINES");
                DateFormat formatter = new SimpleDateFormat("EEEE", locale);

                date = formatter.format(dateEnd) + " @ ";
            } else {
                date = monthString[month] + " " + day_in_month + ", " + year + " @";
            }
        } catch (Exception e) {

        }

        int tempHour = hour;
        String temp = "AM";
        if (hour > 12) {
            tempHour = hour - 12;
            temp = "PM";
        } else if (hour == 12) {
            temp = "PM";
        } else if (hour == 0) {
            tempHour = 12;
        }

        date = date + new DecimalFormat("00").format(tempHour) + ":" + new DecimalFormat("00").format(minute) + " " + temp;
        return  date;
    }
}

