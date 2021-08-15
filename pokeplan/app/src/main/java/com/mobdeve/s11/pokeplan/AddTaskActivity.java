package com.mobdeve.s11.pokeplan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    public static final String KEY_TASKNAME = "KEY_TASKNAME";
    public static final String KEY_CATEGORY = "KEY_CATEGORY";
    public static final String KEY_PRIORITY = "KEY_PRIORITY";
    public static final String KEY_START_DATE = "KEY_START_DATE";
    public static final String KEY_START_TIME = "KEY_START_TIME";
    public static final String KEY_END_DATE = "KEY_END_DATE";
    public static final String KEY_END_TIME = "KEY_END_TIME";
    public static final String KEY_NOTES = "KEY_NOTES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        this.initComponents();
    }

    private void initComponents () {
        this.initDeadline ();
        this.initTimeDue ();
    }


    private void initDeadline () {
        EditText edittext= (EditText) findViewById(R.id.et_add_task_start_date);
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                String myFormat = "dd.MM.yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ROOT);

                edittext.setText(sdf.format(calendar.getTime()));
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTaskActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void initTimeDue () {

    }
}