package com.mobdeve.s11.pokeplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

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

    private Dialog errorDialog;

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

        this.etTaskName = findViewById(R.id.et_add_task_name);
        this.etTaskNotes = findViewById(R.id.et_add_task_notes);
        this.etStartTime = findViewById(R.id.et_add_task_start_time);
        this.etStartDate = findViewById(R.id.et_add_task_start_date);
        this.etEndDate = findViewById(R.id.et_add_task_end_date);
        this.etEndTime = findViewById(R.id.et_add_task_end_time);

        this.btnCreate = findViewById(R.id.btn_add_task_create);
        this.btnCreate.setOnClickListener(new View.OnClickListener() {

            private long getDiff (String endDate, String startDate, String endTime, String startTime) {
                long diff = -1;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm a");

                try {
                    Date dateStart = simpleDateFormat.parse(startDate + " " + startTime);
                    Date dateEnd = simpleDateFormat.parse(endDate + " " + endTime);

                    diff = dateEnd.getTime() - dateStart.getTime();
                } catch (Exception e) {
                }

                return diff;
            }

            @Override
            public void onClick(View v) {

                String taskName = etTaskName.getText().toString();
                String taskNotes = etTaskNotes.getText().toString();
                String startTime = etStartTime.getText().toString();
                String endTime = etEndTime.getText().toString();
                String startDate = etStartDate.getText().toString();
                String endDate = etEndDate.getText().toString();

                String error = "";
                Boolean checker = false;

                // checking task name for errors
                if (taskName == null) {
                    error = "Task name is required!\n";
                    checker = true;
                } else if (!(taskName.length() > 0 && taskName.length() <= 25)) {
                    error = "Length of task name should be from 1 to 25 characters.\n";
                    checker = true;
                }

                if (!endDate.equals("")) {

                    if (!startDate.equals("") && !startTime.equals("")) {

                        if (!startTime.equals("")) {
                            String tempStartDate = startDate.substring(0, 6) + "20" + startDate.substring(6, 8);
                            String tempStarTime = startTime.substring(0, 2) + ":" + startTime.substring(3, 8);

                            String tempEndDate = endDate.substring(0, 6) + "20" + endDate.substring(6, 8);
                            String tempEndTime = endTime.substring(0, 2) + ":" + endTime.substring(3, 8);

                            long diff = getDiff(tempEndDate, tempStartDate, tempEndTime, tempStarTime);

                            if (diff < 0) {
                                error = "End date should be later than the Start date.\n";
                                checker = true;
                            } else if (diff == 0) {
                                error = "Your task should not start and end at the same day and time!\n";
                                checker = true;
                            }
                        }
                        else {
                            error = "Start time is requierd if start date is provided.\n";
                            checker = true;
                        }
                    }

                    Calendar c = Calendar.getInstance();
                    c.setTimeZone(TimeZone.getTimeZone("GMT"));

                    int currentYear = c.get(Calendar.YEAR);
                    int currentDay = c.get(Calendar.DAY_OF_MONTH);
                    int currentMonth = c.get(Calendar.MONTH) + 1;
                    int currentHour = c.get(Calendar.HOUR_OF_DAY);
                    int currentMinute = c.get(Calendar.MINUTE);

                    String temp = "AM";
                    if (currentHour >= 12) {
                        temp = "PM";
                    }

                    if (currentHour > 12) {
                        currentHour = currentHour - 12;
                    }

                    String currentDate = new DecimalFormat("00").format(currentDay) + "." +
                            new DecimalFormat("00").format(currentMonth) + "." + currentYear;
                    String currentTime = new DecimalFormat("00").format(currentHour) + ":" +
                            new DecimalFormat("00").format(currentMinute)+ " " + temp;

                    String tempEndDate = endDate.substring(0, 6) + "20" + endDate.substring(6, 8);
                    String tempEndTime = endTime.substring(0, 2) + ":" + endTime.substring(3, 8);
                    long diff = getDiff(tempEndDate, currentDate, tempEndTime, currentTime);

                    if (diff < 0) {
                        error = "Your end date should be later than the curent time and date!\n";
                        checker = true;
                    } else if (diff == 0) {
                        error = "Your task cannot be currently ending!\n";
                        checker = true;
                    }
                } else {
                    error = "End date is required!\n";
                    checker = true;
                }

                if (endTime == null) {
                    error = "End time is required\n";
                    checker = true;
                }

                if (priority == null) {
                    error = "Priority level for this task is required.\n";
                    checker = true;
                }

                if (category == null) {
                    error = "The category of this task is required\n";
                    checker = true;
                }

                if (!checker) {
                    Intent intent = new Intent();
                    intent.putExtra(KEY_TASKNAME, taskName);
                    intent.putExtra(KEY_NOTES, taskNotes);
                    intent.putExtra(KEY_PRIORITY, Integer.valueOf(priority.length()));
                    intent.putExtra(KEY_CATEGORY, category);
                    intent.putExtra(KEY_START_DATE, startDate);
                    intent.putExtra(KEY_START_TIME, startTime);
                    intent.putExtra(KEY_END_DATE, endDate);
                    intent.putExtra(KEY_END_TIME, endTime);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    errorDialog = new Dialog(v.getContext());
                    errorDialog.setContentView(R.layout.dialog_error);

                    int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
                    int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);

                    errorDialog.getWindow().setLayout(width, height);
                    errorDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    TextView tvdialogtitle = (TextView) errorDialog.findViewById(R.id.tv_dialog_error_title);
                    tvdialogtitle.setText("Invalid input!");
                    TextView tvdialogtext = (TextView) errorDialog.findViewById(R.id.tv_dialog_error_text);
                    tvdialogtext.setText(error);

                    Button btndialogerror = (Button) errorDialog.findViewById(R.id.btn_dialog_error);
                    btndialogerror.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            errorDialog.dismiss();
                        }
                    });
                    errorDialog.show();
                }
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