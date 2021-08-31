package com.mobdeve.s11.pokeplan;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/*
 * This activity is used to create a task and to edit an existing task. When editing a task,
 * the fields are pre-filled with information that is saved in the database.
 * */
public class AddTaskActivity extends AppCompatActivity {
    private ImageButton ibBack;

    private String category;
    private String priority;
    private boolean checkerNotif; // used to check if the user opted for notifications

    private TextView tvTitle;

    private EditText etTaskName;
    private EditText etTaskNotes;
    private EditText etStartDate;
    private EditText etEndDate;
    private EditText etStartTime;
    private EditText etEndTime;
    private Button btnCreate;

    private ArrayList<View> btnPriority; // represents the different buttons of priority levels of a task
    private ArrayList<View> btnCategory; // represents the different buttons of the categories of a task

    private CheckBox cbNotif;
    private Spinner spinNotifTime;
    private Spinner spinNotifWhen;

    private Dialog errorDialog;

    private String currentUserUid;

    private DatabaseHelper databaseHelper; // allows access to the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        createNotificationChannel();
        databaseHelper = new DatabaseHelper();
        checkerNotif = true;
        this.initComponents();
    }

    /**
     * Initializes the layout's components.
     */
    private void initComponents () {
        this.currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // user id of the current user
        this.initCalendar (); // initializes the date picker for the end date and start date of the task
        this.declareComponents ();

        ibBack = findViewById(R.id.ib_add_task_back);
        this.setButtonListeners();
    }

    /**
     * Initializes the action of the back button.
     */
    private void setButtonListeners() {
        ibBack.setOnClickListener(view -> onBackPressed());
    }

    /**
     * converts the given 12H format hour to its 24H format version
     * @param hour is the hour to be converted
     * @param temp is the meridian of the hour given (AM or PM)
     * @return the 24H format of the given hour
     */
    private int convertHour (int hour, String temp) {
        if (hour == 12 && temp.equalsIgnoreCase("AM"))
            hour = 0;
        else if (temp.equalsIgnoreCase("PM"))
            hour += 12;

        return hour;
    }

    /**
     * Parses a part of the given date/time (in string format) to an integer
     * @param input is the string to be parsed to int
     * @param start is the start index of the part that would be parsed to an integer
     * @param end   is the end - 1 index of the part that would be parsed to an integer
     */
    private int dateTimeInputToInt(String input, int start, int end) {
        return Integer.parseInt(input.substring(start, end));
    }

    /**
     * Adds the information of the task created to the database.
     * @param name is the task name.
     * @param priority is the priority level of the task (ranges from 1 as the lowest to 5 as the highest)
     * @param category is the category of the task
     * @param startDate is the string representation of the start date of the task (dd.MM.YYYY)
     * @param endDate is the string representation of the end date of the task (dd.MM.YYYY)
     * @param startTime is the string representation of the start time of the task (hh:mm) in 24H format
     * @param endTime is the string representation of the end time of the task (hh:mm) in 24H format
     * @param notif is the string representation of how many minutes/hours/days before a date should the notification be
     * @param val if true, the notification is before the start time; if false, the notificaation is before the end time
     */
    public void addToDatabase (String name, int priority, String category, String startDate,
                               String endDate, String startTime, String endTime, String notes,
                               String notif, boolean val) {
        int monthEnd = dateTimeInputToInt(endDate, 3, 5);
        int dayEnd = dateTimeInputToInt(endDate, 0, 2);
        int yearEnd = dateTimeInputToInt(endDate, 6, 10);

        int hourEnd = this.convertHour(dateTimeInputToInt(endTime, 0, 2), endTime.substring(6, 8));
        int minuteEnd = dateTimeInputToInt(endTime, 3, 5);

        Task taskCreated;
        if (startDate.equals("")) {
            taskCreated = new Task(currentUserUid, name, priority, category,
                    new CustomDate(true),
                    new CustomDate(yearEnd, monthEnd, dayEnd, hourEnd, minuteEnd), notes, notif, val, checkerNotif);
        }
        else {
            int monthStart = dateTimeInputToInt(startDate, 3, 5);
            int dayStart = dateTimeInputToInt(startDate, 0, 2);
            int yearStart = dateTimeInputToInt(startDate, 6, 10);

            int hourStart = this.convertHour(dateTimeInputToInt(startTime, 0, 2), startTime.substring(6, 8));
            int minuteStart = dateTimeInputToInt(startTime, 3, 5);

            taskCreated = new Task(currentUserUid, name, priority, category,
                    new CustomDate(yearStart, monthStart, dayStart, hourStart, minuteStart),
                    new CustomDate(yearEnd, monthEnd, dayEnd, hourEnd, minuteEnd), notes, notif, val, checkerNotif);
        }

        databaseHelper.addOngoingTask(new FirebaseCallbackTask() {
            @Override
            public void onCallbackTask(ArrayList<Task> list, Boolean isSuccesful, String message) {
                finish();
            }
        }, taskCreated);
    }


    /**
     * Converts a priority number to its representing icon
     * @param num is the priority number assigned to a task
     * @return the string represention of a priority number
     */
    public String setPriority (int num) {
        switch (num) {
            case 1: return "!";
            case 2: return "!!";
            case 3: return "!!!";
            case 4: return "!!!!";
            default: return "!!!!!";
        }
    }

    /**
     * sets the values of the fields based on the latest information of the task
     * @param intent is the intent that holds the previous information of the task from the details shown in the TaskDetailsActivity
     */
    public void setValues (Intent intent) {
        this.etTaskName.setText(intent.getStringExtra(Keys.KEY_TASKNAME.name()));
        this.etTaskNotes.setText(intent.getStringExtra(Keys.KEY_NOTES.name()));

        /* Since the priority is represented by an arraylist of buttons, this next lines of code finds the
           correct button that is assigned to a specific priority level. As the priority level ranges from 1 to 5,
           it can easily be found as the index of a specific level is just priority_level - 1.
        * */
        int priority = intent.getIntExtra(Keys.KEY_PRIORITY.name(), 1) - 1;
        Drawable priorityDrawable = btnPriority.get(priority).getBackground();
        priorityDrawable = DrawableCompat.wrap(priorityDrawable);
        DrawableCompat.setTint(priorityDrawable, getResources().getColor(R.color.pink_button));
        btnPriority.get(priority).setBackground(priorityDrawable);

        this.priority = this.setPriority(priority + 1);
        String category = intent.getStringExtra(Keys.KEY_CATEGORY.name());

        /* Finds the correct button that is assigned to the current category of the task to highlight in the form.
        * */
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

        // checks the checkbox if the user current opted for notification; unchecks if the user has no set notification for the task
        Boolean notifOn = intent.getBooleanExtra(Keys.KEY_NOTIF_ON.name(), false);
        this.checkerNotif = notifOn;
        this.cbNotif.setChecked(notifOn);

        /* if the user opted for notification, then the spinner for the specific time of the notification
           and when the notification should be is set to its current values.
        * */
        if (notifOn) {
            String notifTime = intent.getStringExtra(Keys.KEY_NOTIF_WHEN.name());

            Boolean notifWhen = intent.getBooleanExtra(Keys.KEY_NOTIF_START_TIME.name(), false);
            String temp = "Before End Time";
            if (notifWhen) {
                temp = "Before Start Time";
            }

            spinNotifWhen.setSelection(((ArrayAdapter<String>)spinNotifWhen.getAdapter()).getPosition(temp));
            spinNotifTime.setSelection(((ArrayAdapter<String>)spinNotifTime.getAdapter()).getPosition(notifTime));
        }
    }

    /**
     * Edits the information in the database based on the new information given by the user.
     * @param name is the task name
     * @param priority is the priority level of the task (ranges from 1 as the lowest to 5 as the highest)
     * @param category is the category of the task
     * @param startDate is the string representation of the start date of the task (dd.MM.YYYY)
     * @param endDate is the string representation of the end date of the task (dd.MM.YYYY)
     * @param startTime is the string representation of the start time of the task (hh:mm) in 24H format
     * @param endTime is the string representation of the end time of the task (hh:mm) in 24H format
     * @param notes is the description added to the task
     * @param taskID is the id of the task assigned in the database
     * @param notif is the string representation of how many minutes/hours/days before a date should the notification be
     * @param val if true, the notification is before the start time; if false, the notificaation is before the end time
     */
    public void editDatabase (String name, int priority, String category, String startDate,
                              String endDate, String startTime, String endTime, String notes,
                              String taskID, String notif, boolean val) {

        CustomDate cEndDate = new CustomDate (endDate, endTime);
        CustomDate cStartDate = new CustomDate(startDate, startTime);

        databaseHelper.editTask(new FirebaseCallbackTask() {
            @Override
            public void onCallbackTask(ArrayList<Task> list, Boolean isSuccesful, String message) {
            }
        }, name, priority, category, cStartDate, cEndDate, notes, taskID, notif, val, checkerNotif);

        Intent intent = new Intent();

        intent.putExtra(Keys.KEY_TASKNAME.name(), name);
        intent.putExtra(Keys.KEY_NOTES.name(), notes);
        intent.putExtra(Keys.KEY_END_DATE.name(), cEndDate.toString());
        intent.putExtra(Keys.KEY_START_DATE.name(), cStartDate.toString());
        intent.putExtra(Keys.KEY_C_END_DATE.name(), endDate);
        intent.putExtra(Keys.KEY_C_START_DATE.name(), startDate);
        intent.putExtra(Keys.KEY_C_START_TIME.name(), startTime);
        intent.putExtra(Keys.KEY_C_END_TIME.name(), endTime);
        intent.putExtra(Keys.KEY_CATEGORY.name(), category);
        intent.putExtra(Keys.KEY_PRIORITY.name(), priority);
        intent.putExtra(Keys.KEY_NOTIF_START_TIME.name(), val);
        intent.putExtra(Keys.KEY_NOTIF_ON.name(), checkerNotif);
        intent.putExtra(Keys.KEY_NOTIF_WHEN.name(), notif);

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * This sets the components of the layout to their respective attributes. It also sets the
     * button listener for the btnCreate, which would validate the fields.
     */
    private void declareComponents (){
        this.initPriority (); // initializes the arraylist of buttons that represents the priority level of the task
        this.initCategory (); // initializes the arraylist of buttons that represents the category  of the task
        this.initNotifications (); // initializes the spinners and checkbox that handles the notification

        this.etTaskName = findViewById(R.id.et_add_task_name);
        this.etTaskNotes = findViewById(R.id.et_add_task_notes);
        this.etStartTime = findViewById(R.id.et_add_task_start_time);
        this.etStartDate = findViewById(R.id.et_add_task_start_date);
        this.etEndDate = findViewById(R.id.et_add_task_end_date);
        this.etEndTime = findViewById(R.id.et_add_task_end_time);
        tvTitle = findViewById(R.id.tv_add_task_start_title);
        this.btnCreate = (Button) findViewById(R.id.btn_add_task_create);
        this.cbNotif = findViewById(R.id.cb_add_task_notifs);
        this.spinNotifTime = findViewById(R.id.spin_add_task_notiftime);
        this.spinNotifWhen = findViewById(R.id.spin_add_task_notifwhen);

        Intent intent = getIntent(); // gets the intent if it is for notiication
        /* If there is no intent, then we can conclude that it is a new task that is being created.
           If there is an intent from the task details, we can conclude that it is only editing an existing task.
        * */
        String checker = intent.getStringExtra(Keys.KEY_ID.name());
        if (intent != null && checker != null) {
            setValues (intent); // set the values of the fields to the current information from the intent

            tvTitle.setText("EDIT TASK"); // changes the textview to show that the task is only being edited
            btnCreate.setText("EDIT TASK");
        }

        this.cbNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkerNotif = !checkerNotif;
            }
        });

        this.btnCreate.setOnClickListener(new View.OnClickListener() {

            /**
             * Gets the difference in milliseconds between two dates.
             * @param endDate is the date that is subtracted from
             * @param startDate is the date that is subtracted to
             * @param endTime is the time that is connected with the endDate
             * @param startTime is the time that is connected with the startDate
             * @return if the endDate and endTime is earlier than the startDate, it returns a negative value.
             *         if the startDate and startTime is earlier than the endDate, it returns a positive value.
             *         if the startDate and startTime is the same with the endDate and endTime, it returns 0.
             */
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
                String notif = spinNotifTime.getSelectedItem().toString();
                String when = spinNotifWhen.getSelectedItem().toString();

                boolean val = false;
                // sets the val to true if the chosen notification is before the start time
                if(when != null && !when.isEmpty() && when.equalsIgnoreCase("Before Start Time")) {
                    val = true;
                }

                // if the checkbox for the notification is selected, the two spinners must have a value.
                if (checkerNotif) {
                    if (notif.isEmpty()) {
                        ((TextView) spinNotifTime.getSelectedView()).setError("Notification information is required if check box is clicked.");
                        ((TextView) spinNotifTime.getSelectedView()).requestFocus();
                        return;
                    } else if (when.isEmpty()) {
                        ((TextView) spinNotifWhen.getSelectedView()).setError("Notification information is required if check box is clicked.");
                        ((TextView) spinNotifWhen.getSelectedView()).requestFocus();
                        return;
                    }
                }

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

                // checks if the end date is empty
                if (!endDate.equals("")) {
                    if (!startDate.equals("")) {
                        if (!startTime.equals("")) {
                            String tempStartDate = startDate;
                            String tempStarTime = startTime.substring(0, 2) + ":" + startTime.substring(3, 8);

                            String tempEndDate = endDate;
                            String tempEndTime = endTime.substring(0, 2) + ":" + endTime.substring(3, 8);

                            // checks if the startDate is earlier than the endDate
                            long diff = getDiff(tempEndDate, tempStartDate, tempEndTime, tempStarTime);
                            Log.d("hello pare", Long.toString(diff));
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

                    // checks if the endDate is later than the current date
                    Calendar c = Calendar.getInstance();
                    c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

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

                    String tempEndDate = endDate;
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

                if (endTime == null || endTime.isEmpty()) {
                    etEndTime.setError("End time is required");
                    etEndTime.requestFocus();
                    return;
                }

                // checks if there is no priority button clicked
                if (priority == null) {
                    error = "Priority level for this task is required.\n";
                    checker = true;
                }

                // checks if there is no category button clicked
                if (category == null) {
                    error = "The category of this task is required\n";
                    checker = true;
                }

                // if there are no errors found, the information is added/edited to the database
                if (!checker) {
                    // if there is an intent from the TaskDetailsActivity, the information is only to be edited from the database
                    Intent intent = getIntent();
                    String taskID = intent.getStringExtra(Keys.KEY_ID.name());
                    if (taskID != null) {
                        editDatabase (taskName, priority.length(), category, startDate, endDate, startTime, endTime, taskNotes,
                                taskID, notif, val);

                        if(checkerNotif) {
                            if (val) {
                                if (startDate.isEmpty()) {
                                    deleteTimer();
                                    setTimer(new CustomDate(true), notif, true);
                                } else {
                                    setTimer(new CustomDate(startDate, startTime), notif, true);
                                }
                            } else {
                                deleteTimer();
                                setTimer(new CustomDate(endDate, endTime), notif, false);
                            }
                        } else {
                            deleteTimer();
                        }

                    } else {
                        addToDatabase (taskName, priority.length(), category, startDate, endDate, startTime, endTime, taskNotes, notif, val);
                        if(checkerNotif) {
                            if (val) {
                                if (startDate.isEmpty()) {
                                    setTimer(new CustomDate(true), notif, true);
                                } else {
                                    setTimer(new CustomDate(startDate, startTime), notif, true);
                                }
                            } else {
                                setTimer(new CustomDate(endDate, endTime), notif, false);
                            }
                        }
                    }
                } else { // if there are errors found, the error dialog should be shown with an error message
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

    /**
     * Initializes the buttons for the priority and their onclick listeners.
     * Because of how the onclick listeners were created, there is only one in the arraylist of priority buttons
     * that can be clicked at a time.
     */
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

    /**
     * Initializes the buttons for the category and their onclick listeners.
     * Because of how the onclick listeners were created, there is only one in the arraylist of category buttons
     * that can be clicked at a time.
     */
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

    /**
     * Creates the timepicker dialog and datepicker dialog for the dates and times.
     * Also set the values the Activity is to be used for editing.
     */
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

                String myFormat = "dd.MM.yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ROOT);

                startDate.setText(sdf.format(calendarStart.getTime()));
            }
        };

        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        DecimalFormat format = new DecimalFormat("00");

        Intent intent = getIntent();
        String sStartDate = intent.getStringExtra(Keys.KEY_C_START_DATE.name());
        if (sStartDate != null && !sStartDate.isEmpty()) {
            String [] temp = sStartDate.split("\\.");
            day = Integer.parseInt(temp[0]);
            month = Integer.parseInt(temp[1]) - 1;

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

                String myFormat = "dd.MM.yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ROOT);

                endDate.setText(sdf.format(calendarEnd.getTime()));
            }

        };

        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        year = Calendar.getInstance().get(Calendar.YEAR);

        String sEndDate = intent.getStringExtra(Keys.KEY_C_END_DATE.name());
        if (sStartDate != null && !sEndDate.isEmpty()) {
            String [] temp = sEndDate.split("\\.");
            day = Integer.parseInt(temp[0]);
            month = Integer.parseInt(temp[1]) - 1;

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
                String date = hourOfDay + ":" + minute;
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                try {
                    Date tempDate = sdf.parse(date);
                    sdf = new SimpleDateFormat("hh:mm aa");
                    startTime.setText(sdf.format(tempDate));

                } catch (ParseException e) {
                }
            }
        };

        int hour = 0;
        int minute = 0;

        String sTime = intent.getStringExtra(Keys.KEY_C_START_TIME.name());
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

            startTime.setText(format.format(tempHour) + ":" + format.format(minute) + " " + tempM);
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
                String date = hourOfDay + ":" + minute;
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                try {
                    Date tempDate = sdf.parse(date);
                    sdf = new SimpleDateFormat("hh:mm aa");
                    endTime.setText(sdf.format(tempDate));

                } catch (ParseException e) {
                }
            }
        };

        hour = 0;
        minute = 0;

        String eTime = intent.getStringExtra(Keys.KEY_C_END_TIME.name());
        if (eTime != null && !eTime.isEmpty()) {
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

            endTime.setText(format.format(tempHour) + ":" + format.format(minute) + " " + tempM);
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

    /**
     * Initializes the spinners and checkbox needed for the notification.
     */
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

        cbNotif = findViewById(R.id.cb_add_task_notifs);
        cbNotif.setChecked(true);
        cbNotif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbNotif.isChecked()) {
                    spinNotifTime.setEnabled(true);
                    spinNotifWhen.setEnabled(true);
                }
                else {
                spinNotifTime.setEnabled(false);
                spinNotifWhen.setEnabled(false);
             }
        }});
    }

    /**
     * Creates the notification based on the information provided.
     * @param date is the day (and specific time) wherein the notification would be created
     * @param notif determines how many minutes/hours/days the notification would be from the date provided
     * @param checker is used for the creation of the notification message
     */
    private void setTimer (CustomDate date, String notif, boolean checker) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, date.getMonth() - 1);
        c.set(Calendar.DAY_OF_MONTH, date.getDay());
        c.set(Calendar.YEAR, date.getYear());
        c.set(Calendar.HOUR_OF_DAY, date.getHour());
        c.set(Calendar.MINUTE, date.getMinute());
        c.set(Calendar.SECOND, 0);

        // sets the time-offset from the date and time
        switch(notif) {
            case "10 Minutes": c.add(Calendar.MINUTE, -10);
                break;
            case "30 Minutes": c.add(Calendar.MINUTE, -30);
                break;
            case "1 Hour": c.add(Calendar.HOUR_OF_DAY, -1);
                break;
            case "2 Hours": c.add(Calendar.HOUR_OF_DAY, -2);
                break;
            case "4 Hours": c.add(Calendar.HOUR_OF_DAY, -4);
                break;
            case "8 Hours": c.add(Calendar.HOUR_OF_DAY, -8);
                break;
            case "12 Hours": c.add(Calendar.HOUR_OF_DAY, -12);
                break;
            case "1 Day": c.add(Calendar.DAY_OF_MONTH, -1);
                break;
        }

        String message = "Don't forget! This ";
        if(checker) {
            message = message + " starts in " + notif;
        } else {
            message = message + " ends in " + notif;
        }


        // creates the alarm manager and the intent to be sent to the broadcast receiver
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderBroadcast.class);
        intent.putExtra("TASKNAME", etTaskName.getText().toString().trim());
        intent.putExtra("NOTIF_MESSAGE", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        // sets the notification time and date
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void createNotificationChannel() {
        // checks if the sdk used by the phone is greater than or equal 26 as a channel needs to be created if it is sdk 26 or higher
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "PokePlanReminderChannel";
            String description = "Channel for PokePlan Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("pokeplanNotify", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Deletes the notification created from the broadcast receiver.
     */
    private void deleteTimer () {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderBroadcast.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
    }
}