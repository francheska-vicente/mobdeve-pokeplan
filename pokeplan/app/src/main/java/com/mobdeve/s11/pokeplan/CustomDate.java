package com.mobdeve.s11.pokeplan;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class CustomDate {
    private static final String[] monthString = new String[]{"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private int day_in_month, month, year, hour, minute;

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

    @Override
    public String toString() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        int currentYear = c.get(Calendar.YEAR);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        int currentMonth = c.get(Calendar.MONTH) + 1;

        String date = "";
        long diff = 0;
        try {
            Date dateStart = simpleDateFormat.parse(currentDay + "." + currentMonth + "." + currentYear);
            Date dateEnd = simpleDateFormat.parse(this.day_in_month + "." + this.month + "." + this.year);

            diff = dateEnd.getTime() - dateStart.getTime();
            diff = TimeUnit.MILLISECONDS.toDays(diff);

            if (diff == 0) {
                date = "Today @ ";
            } else if (diff <= 6 && diff > 0) {
                Locale locale = new Locale("EN", "PHILIPPINES");
                DateFormat formatter = new SimpleDateFormat("EEEE", locale);

                date = formatter.format(dateEnd) + " @ ";
            } else {
                date = monthString[month] + " " + this.day_in_month + ", " + this.year + " @";
            }
        } catch (Exception e) {

        }

        int tempHour = this.hour;
        String temp = "AM";
        if (hour > 12) {
            tempHour = hour - 12;
            temp = "PM";
        } else if (hour == 12) {
            temp = "PM";
        }

        date = date + new DecimalFormat("00").format(tempHour) + ":" + new DecimalFormat("00").format(minute) + " " + temp;
        return  date;
    }

}

