package com.mobdeve.s11.pokeplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

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

    private TextView tvTitle;

    private EditText etTaskName;
    private EditText etTaskNotes;
    private EditText etStartDate;
    private EditText etEndDate;
    private EditText etStartTime;
    private EditText etEndTime;
    private Button btnCreate;

    private ArrayList<View> btnPriority;
    private ArrayList<View> btnCategory;

    private CheckBox cbNotif;
    private Spinner spinNotifTime;
    private Spinner spinNotifWhen;

    private Dialog errorDialog;

    private String currentUserUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        this.initComponents();
    }

    private void initComponents () {
        this.currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.initCalendar ();
        this.intent ();
    }

    private int convertHour (int hour, String temp) {
        if (hour == 12) {
            if (temp.equalsIgnoreCase("AM"))
                hour = 0;
        } else if (temp.equalsIgnoreCase("PM")) {
            hour = hour + 12;
        }

        return hour;
    }

    public void addToDatabase (String name, int priority, String category, String startDate,
                               String endDate, String startTime, String endTime, String notes) {
        int monthEnd = Integer.parseInt(endDate.substring(3, 5));
        int dayEnd = Integer.parseInt(endDate.substring(0, 2));
        int yearEnd = Integer.parseInt(endDate.substring(6, 8));

        int hourEnd = Integer.parseInt(endTime.substring(0, 2));
        int minuteEnd = Integer.parseInt(endTime.substring(3, 5));

        hourEnd = this.convertHour(hourEnd, endTime.substring(6, 8));

        Task taskCreated;
        if (startDate.equals("")) {
            taskCreated = new Task(currentUserUid, name, priority, category,
                    new CustomDate(true),
                    new CustomDate(yearEnd, monthEnd, dayEnd, hourEnd, minuteEnd), notes);
        } else {
            int monthStart = Integer.parseInt(startDate.substring(3, 5));
            int dayStart = Integer.parseInt(startDate.substring(0, 2));
            int yearStart = Integer.parseInt(startDate.substring(6, 8));

            int hourStart = Integer.parseInt(startTime.substring(0, 2));
            int minuteStart = Integer.parseInt(startTime.substring(3, 5));

            hourStart = this.convertHour (hourStart, startTime.substring(6, 8));

            taskCreated = new Task(currentUserUid, name, priority, category,
                    new CustomDate(yearStart, monthStart, dayStart, hourStart, minuteStart),
                    new CustomDate(yearEnd, monthEnd, dayEnd, hourEnd, minuteEnd), notes);
        }

        UserSingleton.getUser().addOngoingTask(taskCreated);
    }

    public void setValues (Intent intent) {
        this.etTaskName.setText(intent.getStringExtra(TaskDetailsActivity.KEY_TASKNAME));
        this.etTaskNotes.setText(intent.getStringExtra(TaskDetailsActivity.KEY_NOTES));

        int priority = intent.getIntExtra(TaskDetailsActivity.KEY_PRIORITY, 1) - 1;
        Drawable priorityDrawable = btnPriority.get(priority).getBackground();
        priorityDrawable = DrawableCompat.wrap(priorityDrawable);
        DrawableCompat.setTint(priorityDrawable, getResources().getColor(R.color.pink_button));
        btnPriority.get(priority).setBackground(priorityDrawable);

        this.priority = (priority + 1) + "";
        String category = intent.getStringExtra(TaskDetailsActivity.KEY_CATEGORY);
        for (int i = 0; i < btnCategory.size(); i++) {
            Button temp = (Button) btnCategory.get(i);
            if(temp.getText().toString().equalsIgnoreCase(category)) {
                Drawable categoryDrawable = temp.getBackground();
                categoryDrawable = DrawableCompat.wrap(categoryDrawable);
                DrawableCompat.setTint(categoryDrawable, getResources().getColor(R.color.pink_button));
                temp.setBackground(categoryDrawable);
                this.category = temp.getText().toString();
            }
        }
    }

    public void editDatabase (String name, int priority, String category, String startDate,
                              String endDate, String startTime, String endTime, String notes, String taskID) {

        CustomDate cEndDate = new CustomDate (endDate, endTime);
        CustomDate cStartDate = new CustomDate(startDate, startTime);

        UserSingleton.getUser().editTask(name, priority, category, cStartDate, cEndDate, notes, taskID);

        Intent intent = new Intent();

        intent.putExtra(AddTaskActivity.KEY_TASKNAME, name);
        intent.putExtra(AddTaskActivity.KEY_NOTES, notes);
        intent.putExtra(AddTaskActivity.KEY_END_DATE, cEndDate.toString());
        intent.putExtra(AddTaskActivity.KEY_START_DATE, cStartDate.toString());
        intent.putExtra(AddTaskActivity.KEY_CATEGORY, category);
        intent.putExtra(AddTaskActivity.KEY_PRIORITY, this.priority);

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void intent () {
        this.initPriority ();
        this.initCategory ();
        this.initNotifications ();

        this.etTaskName = findViewById(R.id.et_add_task_name);
        this.etTaskNotes = findViewById(R.id.et_add_task_notes);
        this.etStartTime = findViewById(R.id.et_add_task_start_time);
        this.etStartDate = findViewById(R.id.et_add_task_start_date);
        this.etEndDate = findViewById(R.id.et_add_task_end_date);
        this.etEndTime = findViewById(R.id.et_add_task_end_time);
        tvTitle = findViewById(R.id.tv_add_task_start_title);
        this.btnCreate = (Button) findViewById(R.id.btn_add_task_create);

        Intent intent = getIntent();
        String checker = intent.getStringExtra(TaskDetailsActivity.KEY_ID);
        if (intent != null && checker != null) {
            setValues (intent);

            tvTitle.setText("EDIT TASK");
            btnCreate.setText("EDIT TASK");
        }

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
                boolean checker = false;

                // checking task name for errors
                if (taskName.isEmpty()) {
                    etTaskName.setError("Task name is required!");
                    etTaskName.requestFocus();
                    return;
                } else if (taskName.length() >= 25) {
                    etTaskName.setError("Length of task name should be from 1 to 25 characters.");
                    etTaskName.requestFocus();
                    return;
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
                                etEndDate.setError("End date should be later than the Start date.");
                                etEndDate.requestFocus();
                                return;
                            } else if (diff == 0) {
                                error = "Your task should not start and end at the same day and time!";
                                checker = true;
                            }
                        }
                        else {
                            etStartTime.setError("Start time is required if start date is provided.");
                            etStartTime.requestFocus();
                            return;
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
                        etEndDate.setError("Your end date should be later than the current time and date!");
                        etEndDate.requestFocus();
                        return;
                    } else if (diff == 0) {
                        error = "Your task cannot be currently ending!\n";
                        checker = true;
                    }
                } else {
                    etEndDate.setError("End date is required.");
                    etEndDate.requestFocus();
                    return;
                }

                if (endTime == null) {
                    etEndTime.setError("End time is required");
                    etEndTime.requestFocus();
                    return;
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
                    Intent intent = getIntent();
                    String taskID = intent.getStringExtra(TaskDetailsActivity.KEY_ID);
                    if (taskID != null) {
                        editDatabase (taskName, Integer.valueOf(priority.length()), category, startDate, endDate, startTime, endTime, taskNotes,
                                taskID);
                    } else {
                        addToDatabase (taskName, Integer.valueOf(priority.length()), category, startDate, endDate, startTime, endTime, taskNotes);
                        finish();
                    }
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

        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        DecimalFormat format = new DecimalFormat("00");

        Intent intent = getIntent();
        String sStartDate = intent.getStringExtra(TaskDetailsActivity.KEY_C_START_DATE);
        if (sStartDate != null && !sStartDate.isEmpty()) {
            String [] temp = sStartDate.split("\\.");
            day = Integer.parseInt(temp[0]);
            month = Integer.parseInt(temp[1]) - 1;

            if (temp[2].length() == 4) {
                temp[2] = temp[2].substring(2,4);
            }

            startDate.setText(format.format(day) + "." + format.format(month + 1) + "." + temp[2]);
        }

        int finalSYear = year;
        int finalSDay = day;
        int finalSMonth = month;
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTaskActivity.this, dateStart, finalSYear, finalSMonth,
                        finalSDay).show();
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

        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        year = Calendar.getInstance().get(Calendar.YEAR);

        String sEndDate = intent.getStringExtra(TaskDetailsActivity.KEY_C_END_DATE);
        if (sStartDate != null && !sEndDate.isEmpty()) {
            String [] temp = sEndDate.split("\\.");
            day = Integer.parseInt(temp[0]);
            month = Integer.parseInt(temp[1]) - 1;

            if (temp[2].length() == 4) {
                temp[2] = temp[2].substring(2,4);
            }

            endDate.setText(format.format(day) + "." + format.format(month + 1) + "." + temp[2]);
        }

        int finalYear = year;
        int finalMonth = month;
        int finalDay = day;
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTaskActivity.this, dateEnd, finalYear, finalMonth,
                        finalDay).show();
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

        int hour = 0;
        int minute = 0;

        String sTime = intent.getStringExtra(TaskDetailsActivity.KEY_C_START_TIME);
        if (sTime != null && !sTime.isEmpty()) {
            String [] temp = sTime.split(":");
            hour = Integer.parseInt(temp[0]);
            minute = Integer.parseInt(temp[1]);

            int tempHour = hour;
            String tempM = "AM";
            if (hour > 12) {
                tempHour = hour - 12;
                tempM = "PM";
            } else if (hour == 0) {
                tempHour = 12;
            } else if (hour == 12) {
                tempM = "PM";
            }

            startTime.setText(format.format(hour) + ":" + format.format(minute) + " " + tempM);
        }

        int finalSHour = hour;
        int finalSMinute = minute;
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddTaskActivity.this, timeStart, finalSHour, finalSMinute, false).show();
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

        hour = 0;
        minute = 0;

        String eTime = intent.getStringExtra(TaskDetailsActivity.KEY_C_END_TIME);
        if (sTime != null && !sTime.isEmpty()) {
            String [] temp = eTime.split(":");
            hour = Integer.parseInt(temp[0]);
            minute = Integer.parseInt(temp[1]);

            int tempHour = hour;
            String tempM = "AM";
            if (hour > 12) {
                tempHour = hour - 12;
                tempM = "PM";
            } else if (hour == 0) {
                tempHour = 12;
            } else if (hour == 12) {
                tempM = "PM";
            }

            endTime.setText(format.format(hour) + ":" + format.format(minute) + " " + tempM);
        }

        int finalEHour = hour;
        int finalEMinute = minute;

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddTaskActivity.this, timeEnd, finalEHour, finalEMinute, false).show();
            }
        });
    }

    private void initNotifications () {
        spinNotifTime = findViewById(R.id.spin_add_task_notiftime);
        ArrayAdapter<CharSequence> adapterTime = ArrayAdapter.createFromResource(this,
                R.array.notifs_time, R.layout.spinner_item);
        adapterTime.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinNotifTime.setAdapter(adapterTime);

        spinNotifWhen = findViewById(R.id.spin_add_task_notifwhen);
        ArrayAdapter<CharSequence> adapterWhen = ArrayAdapter.createFromResource(this,
                R.array.notifs_when, R.layout.spinner_item);
        adapterWhen.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinNotifWhen.setAdapter(adapterWhen);

        spinNotifTime.setEnabled(false);
        spinNotifWhen.setEnabled(false);

        cbNotif = findViewById(R.id.cb_add_task_notifs);
        cbNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cbNotif.isChecked()) {
                    spinNotifTime.setEnabled(false);
                    spinNotifWhen.setEnabled(false);
                }
                else {
                    spinNotifTime.setEnabled(true);
                    spinNotifWhen.setEnabled(true);
                }
            }
        });

    }
}