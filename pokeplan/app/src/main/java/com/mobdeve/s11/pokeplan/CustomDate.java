package com.mobdeve.s11.pokeplan;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CustomDate {
    private static final String[] monthString = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private int day_in_month, month, year, hour, minute;

    public CustomDate () {
        Calendar c = Calendar.getInstance();
        this.year = c.get(Calendar.YEAR);
        this.day_in_month = c.get(Calendar.DAY_OF_MONTH);
        this.month = c.get(Calendar.MONTH);
        this.hour = c.get(Calendar.HOUR_OF_DAY);
        this.minute = c.get(Calendar.MINUTE);
    }

    public CustomDate(int year, int month, int day_in_month, int hour, int minute) {
        this.year = year;
        this.day_in_month = day_in_month;
        this.month = month - 1;
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
        int currentMonth = c.get(Calendar.MONTH);

        long diff = -1;
        try {
            Date dateStart = simpleDateFormat.parse(currentDay + "." + currentMonth + "." + currentYear);
            Date dateEnd = simpleDateFormat.parse(this.day_in_month + "." + (this.month + 1) + "." + this.year);

            diff = Math.round(((dateEnd.getTime() - dateStart.getTime()) / (1000 * 60 * 60 * 24)) % 365);

            if (diff == 0) {
                return "Today @ " + new DecimalFormat("00").format(hour) + ":" + new DecimalFormat("00").format(minute);
            } else if (diff <= 6) {
                Locale locale = new Locale("EN", "PHILIPPINES");
                DateFormat formatter = new SimpleDateFormat("EEEE", locale);
                return formatter.format(dateEnd) + " @ diff: " + diff + new DecimalFormat("00").format(hour) + ":" + new DecimalFormat("00").format(minute);
            }
        } catch (Exception e) {

        }

        return monthString[month] + " " + this.day_in_month + ", " + this.year + " @" + new DecimalFormat("00").format(hour) + ":" + new DecimalFormat("00").format(minute);
    }

}

