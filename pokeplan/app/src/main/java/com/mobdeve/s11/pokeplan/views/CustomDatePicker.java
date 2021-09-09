package com.mobdeve.s11.pokeplan.views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CustomDatePicker {
    private int year;
    private int month;
    private int day;

    public void createDatePicker(Context context, EditText etDate, String sDate) {
        DecimalFormat format = new DecimalFormat("00");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.ROOT);
        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            etDate.setText(sdf.format(calendar.getTime()));
        };

        if (sDate != null && !sDate.isEmpty()) {
            String [] temp = sDate.split("\\.");
            day = Integer.parseInt(temp[0]);
            month = Integer.parseInt(temp[1]) - 1;
            etDate.setText(format.format(day) + "." + format.format(month + 1) + "." + temp[2]);
        }

        etDate.setOnClickListener(v ->
                new DatePickerDialog(context, dateSetListener, year, month, day).show());
    }
}
