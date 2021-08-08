package com.mobdeve.s11.pokeplan;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomDate {
    private static final String[] monthString = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private int day_in_month, month, year, hour, minute;

    public CustomDate () {
        Calendar c = Calendar.getInstance();
        this.year = c.get(Calendar.YEAR);
        this.day_in_month = c.get(Calendar.DAY_OF_MONTH);
        this.month = c.get(Calendar.MONTH);
        this.hour = 0;
        this.minute = 0;
    }

    public CustomDate(int year, int month, int day_in_month, int hour, int minute) {
        this.year = year;
        this.day_in_month = day_in_month;
        this.month = month;
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public String toString() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        int currentYear = c.get(Calendar.YEAR);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        int currentMonth = c.get(Calendar.MONTH);

        long diff = -1;
        try {
            Date dateStart = simpleDateFormat.parse(currentDay + "." + currentMonth + "." + currentYear);
            Date dateEnd = simpleDateFormat.parse(this.day_in_month + "." + this.month + "." + this.year);

            diff = Math.round((dateEnd.getTime() - dateStart.getTime()) / (double) 86400000);

            if (diff == 0) {
                return "Due today @" + new DecimalFormat("00").format(hour) + ":" + new DecimalFormat("00").format(minute);
            } else if (diff >= 6) {
                Locale locale = new Locale("EN", "PHILIPPINES");
                DateFormat formatter = new SimpleDateFormat("EEEE", locale);
                return "Due " + formatter.format(dateEnd) + " @" + new DecimalFormat("00").format(hour) + ":" + new DecimalFormat("00").format(minute);
            }
        } catch (Exception e) {

        }

        return "Due " + monthString[month] + " " + this.day_in_month + ", " + this.year + "@" + new DecimalFormat("00").format(hour) + ":" + new DecimalFormat("00").format(minute);
    }

}

