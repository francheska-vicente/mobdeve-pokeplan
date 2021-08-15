package com.mobdeve.s11.pokeplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    private String category;
    private String priority;

    private EditText etTaskName;
    private EditText etTaskNotes;
    private EditText etStartDate;
    private EditText etEndDate;
    private EditText etStartTime;
    private EditText etEndTime;
    private Button btnCreate;

    private ArrayList<View> btnPriority;
    private ArrayList<View> btnCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        this.initComponents();
    }

    private void initComponents () {
        this.initCalendar ();

        this.intent ();
    }

    private void intent () {
        this.initPriority ();
        this.initCategory ();

        this.btnCreate = findViewById(R.id.btn_add_task_create);
        this.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initPriority () {
        this.btnPriority = new ArrayList<>();
        ConstraintLayout clPriority = findViewById(R.id.cl_add_task_priority);
        for(int i = 0; i < clPriority.getChildCount(); i++){
            View v = clPriority.getChildAt(i);
            if(v instanceof Button){
                btnPriority.add((Button) v);
                btnPriority.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button b = (Button) v;
                        priority = b.getText().toString();

                        ConstraintLayout clPriority = findViewById(R.id.cl_add_task_priority);
                        for(int i = 0; i < clPriority.getChildCount(); i++) {
                            View view = clPriority.getChildAt(i);
                            if(view instanceof Button && !view.equals(v)) {
                                Drawable buttonDrawable = view.getBackground();
                                DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.lighter_gray) );
                                view.setBackground(buttonDrawable);
                            } else if (view.equals(v)) {
                                Drawable buttonDrawable = v.getBackground();
                                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                                DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.pink_button));
                                v.setBackground(buttonDrawable);
                            }
                        }
                    }
                });
            }
        }
    }

    private void initCategory () {
        this.btnCategory = new ArrayList<>();
        ConstraintLayout clCategory = findViewById(R.id.cl_add_task_category);
        for(int i = 0; i < clCategory.getChildCount(); i++){
            View v = clCategory.getChildAt(i);
            if(v instanceof Button){
                btnCategory.add((Button) v);
                btnCategory.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button b = (Button) v;
                        category = b.getText().toString();

                        ConstraintLayout clPriority = findViewById(R.id.cl_add_task_category);
                        for(int i = 0; i < clPriority.getChildCount(); i++) {
                            View view = clPriority.getChildAt(i);
                            if(view instanceof Button && !view.equals(v)) {
                                Drawable buttonDrawable = view.getBackground();
                                DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.lighter_gray) );
                                view.setBackground(buttonDrawable);
                            } else if (view.equals(v)) {
                                Drawable buttonDrawable = v.getBackground();
                                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                                DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.pink_button));
                                v.setBackground(buttonDrawable);
                            }
                        }
                    }
                });
            }
        }
    }

    private void initCalendar () {
        EditText startDate = (EditText) findViewById(R.id.et_add_task_start_date);
        Calendar calendarStart = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, month);
                calendarStart.set(Calendar.DAY_OF_MONTH, day);

                String myFormat = "dd.MM.yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ROOT);

                startDate.setText(sdf.format(calendarStart.getTime()));
            }
        };

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTaskActivity.this, dateStart, calendarStart
                        .get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        EditText endDate = (EditText) findViewById(R.id.et_add_task_end_date);
        Calendar calendarEnd = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                calendarEnd.set(Calendar.YEAR, year);
                calendarEnd.set(Calendar.MONTH, month);
                calendarEnd.set(Calendar.DAY_OF_MONTH, day);

                String myFormat = "dd.MM.yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ROOT);

                endDate.setText(sdf.format(calendarEnd.getTime()));
            }

        };

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTaskActivity.this, dateEnd, calendarEnd
                        .get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                        calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        EditText startTime = (EditText) findViewById(R.id.et_add_task_start_time);
        TimePickerDialog.OnTimeSetListener timeStart = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String date = hourOfDay + "." + minute;
                SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");
                try {
                    Date tempDate = sdf.parse(date);
                    sdf = new SimpleDateFormat("hh.mm aa");
                    startTime.setText(sdf.format(tempDate));

                } catch (ParseException e) {
                }
            }
        };

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddTaskActivity.this, timeStart, 0, 0, false).show();
            }
        });

        EditText endTime = (EditText) findViewById(R.id.et_add_task_end_time);
        TimePickerDialog.OnTimeSetListener timeEnd = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String date = hourOfDay + "." + minute;
                SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");
                try {
                    Date tempDate = sdf.parse(date);
                    sdf = new SimpleDateFormat("hh.mm aa");
                    endTime.setText(sdf.format(tempDate));

                } catch (ParseException e) {
                }
            }
        };

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddTaskActivity.this, timeEnd, 0, 0, false).show();
            }
        });
    }
}