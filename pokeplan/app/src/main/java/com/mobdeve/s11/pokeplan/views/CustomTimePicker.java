package com.mobdeve.s11.pokeplan.views;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomTimePicker {
    private int hour = 0;
    private int minute = 0;
    private String period = "AM";

    public void createTimePicker(Context context, EditText etTime, String sTime) {
        DecimalFormat format = new DecimalFormat("00");

        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            String date = hourOfDay + ":" + minute;
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            try {
                Date tempDate = sdf.parse(date);
                sdf = new SimpleDateFormat("hh:mm aa");
                etTime.setText(sdf.format(tempDate));
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        };

        if (sTime != null && !sTime.isEmpty()) {
            String [] temp = sTime.split(":");
            hour = Integer.parseInt(temp[0]);
            minute = Integer.parseInt(temp[1]);

            int tempHour = convertHour();

            etTime.setText(format.format(tempHour) + ":" + format.format(minute) + " " + period);
        }

        etTime.setOnClickListener(v ->
                new TimePickerDialog(context, timeSetListener, hour, minute, false).show());
    }

    private int convertHour() {
        if (hour > 12) {
            period = "PM";
            return hour - 12;
        }
        else if (hour == 0) {
            return 12;
        }
        else if (hour == 12) {
            period = "PM";
        }
        return hour;
    }
}
