package com.mobdeve.s11.pokeplan;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class CustomDate {
    private static final String[] monthString = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private int day_in_month, month, year;

    public CustomDate() {

    }

    public CustomDate(int year, int month, int day_in_month) {
        this.year = year;
        this.day_in_month = day_in_month;
        this.month = month;
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
        } catch (Exception e) {

        }

        if (diff >= 6) {
            String [] printWeek = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

            LocalDate localDate = LocalDate.of(year, month, day_in_month);

            java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
            System.out.println(dayOfWeek);

            return dayOfWeek.toString();

        } else {
            return monthString[month] + " " + this.day_in_month + ", " + this.year;
        }
    }

}

