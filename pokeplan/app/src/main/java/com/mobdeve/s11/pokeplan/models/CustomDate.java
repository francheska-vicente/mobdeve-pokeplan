package com.mobdeve.s11.pokeplan.models;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * This class handles the storing and printing the dates based on the format of the application.
 */
public class CustomDate {
    private static final String[] monthString = new String[]{"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private int day, month, year, hour, minute;

    /**
     * A constructor with no parameters for Firebase.
     */
    public CustomDate () {}

    /**
     * A constructor used to create a CustomDate object with today as the date.
     * @param checker
     */
    public CustomDate (boolean checker) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        this.year = c.get(Calendar.YEAR);
        this.day = c.get(Calendar.DAY_OF_MONTH);
        this.month = c.get(Calendar.MONTH) + 1;
        this.hour = c.get(Calendar.HOUR_OF_DAY);
        this.minute = c.get(Calendar.MINUTE);
    }

    /**
     * A constructor used to create a CustomDate object for the specific date.
     * @param year is the year of the date to be created
     * @param month is the month of the date to be created
     * @param day_in_month is the day of the date to be created
     * @param hour is the hour of the date to be created
     * @param minute is the minute of the date to be created
     */
    public CustomDate(int year, int month, int day_in_month, int hour, int minute) {
        this.year = year;
        this.day = day_in_month;
        this.month = month;
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * A constructor used to create a CustomDate object for the specific date in String
     * @param date is a String in the format dd.MM.yyyy that represents the date to be created
     * @param time is a String in the format HH:mm aa that represents the time of the date to be created in 12H foramt
     */
    public CustomDate(String date, String time) {
        String [] tempDate = date.split("\\.");
        String [] tempTime = time.split(":");
        this.month = Integer.parseInt(tempDate[1]);
        this.day = Integer.parseInt(tempDate[0]);
        this.year = Integer.parseInt(tempDate[2]);

        this.hour = Integer.parseInt(tempTime[0]);
        tempTime = tempTime[1].split(" ");
        this.minute = Integer.parseInt(tempTime[0]);

        if (tempTime[1].trim().equalsIgnoreCase("pm") && this.hour != 12) {
            this.hour = hour + 12;
        }
        else if (tempTime[1].trim().equalsIgnoreCase("am") && this.hour == 12) {
            this.hour = 0;
        }

    }

    public int getDay () {
        return this.day;
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
    /**
     * Reformats the CustomDate object to a string
     */
    public String toString() {
        String tempTime = new DecimalFormat("00").format(hour) + ":" + new DecimalFormat("00").format(minute);
        String tempDate = this.day + "." + this.month + "." + this.year;

        return printData(tempDate, tempTime);
    }

    public static String printData (String dateToConvert, String time) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

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
            String temp = new DecimalFormat("00").format(day_in_month) + "." + new DecimalFormat("00").format(month) + "." + year;
            Date dateEnd = simpleDateFormat.parse(temp);

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
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        int tempHour = hour;
        String temp = "AM";
        if (hour > 12) {
            tempHour = hour - 12;
            temp = "PM";
        }
        else if (hour == 12) {
            temp = "PM";
        }
        else if (hour == 0) {
            tempHour = 12;
        }

        date = date + new DecimalFormat("00").format(tempHour) + ":" + new DecimalFormat("00").format(minute) + " " + temp;
        return  date;
    }

    public String printSpecificDate () {
        return monthString[month] + " " + day + ", " + year;
    }

}

