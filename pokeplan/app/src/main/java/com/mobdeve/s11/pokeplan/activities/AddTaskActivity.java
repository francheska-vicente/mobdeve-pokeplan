package com.mobdeve.s11.pokeplan.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.data.DatabaseHelper;
import com.mobdeve.s11.pokeplan.data.FirebaseCallbackTask;
import com.mobdeve.s11.pokeplan.models.CustomDate;
import com.mobdeve.s11.pokeplan.models.UserTask;
import com.mobdeve.s11.pokeplan.services.ReminderBroadcast;
import com.mobdeve.s11.pokeplan.utils.Keys;
import com.mobdeve.s11.pokeplan.views.CustomDatePicker;
import com.mobdeve.s11.pokeplan.views.CustomDialog;
import com.mobdeve.s11.pokeplan.views.CustomTimePicker;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * This activity is used to create a task and to edit an existing task. When editing a task,
 * the fields are pre-filled with information that is saved in the database.
 */
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

    private CustomDialog errorDialog;

    private String currentUserUid;
    private int notifCode;

    private DatabaseHelper databaseHelper; // allows access to the database
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        createNotificationChannel();

        this.sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        notifCode = -1;
        databaseHelper = new DatabaseHelper(true);
        checkerNotif = true;
        this.initComponents();
    }

    /**
     * Initializes the layout's components.
     */
    private void initComponents () {
        this.currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // user id of the current user
        this.initCalendar (); // initializes the date picker for the end date and start date of the task
        this.setComponents();

        ibBack = findViewById(R.id.ib_add_task_back);
        this.setButtonListeners();
    }

    /**
     * Initializes the action of the back button.
     */
    private void setButtonListeners() {
        ibBack.setOnClickListener(view -> onBackPressed());

        this.cbNotif.setOnClickListener(v -> checkerNotif = !checkerNotif);
    }

    /**
     * Converts the given 12H format hour to its 24H format version
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

        UserTask taskCreated;
        if (startDate.equals("")) {
            taskCreated = new UserTask(currentUserUid, name, priority, category,
                    new CustomDate(true),
                    new CustomDate(yearEnd, monthEnd, dayEnd, hourEnd, minuteEnd), notes, notif, val, checkerNotif);
        }
        else {
            int monthStart = dateTimeInputToInt(startDate, 3, 5);
            int dayStart = dateTimeInputToInt(startDate, 0, 2);
            int yearStart = dateTimeInputToInt(startDate, 6, 10);

            int hourStart = this.convertHour(dateTimeInputToInt(startTime, 0, 2), startTime.substring(6, 8));
            int minuteStart = dateTimeInputToInt(startTime, 3, 5);

            taskCreated = new UserTask(currentUserUid, name, priority, category,
                    new CustomDate(yearStart, monthStart, dayStart, hourStart, minuteStart),
                    new CustomDate(yearEnd, monthEnd, dayEnd, hourEnd, minuteEnd), notes, notif, val, checkerNotif);
        }

        databaseHelper.addOngoingTask((list, isSuccessful, message) -> {
            if (isSuccessful)
                Toast.makeText(AddTaskActivity.this, "Task was added successfully.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(AddTaskActivity.this, "Task was not added to your list of tasks.", Toast.LENGTH_SHORT).show();

            finish();
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
     * Sets the values of the fields based on the latest information of the task
     * @param intent is the intent that holds the previous information of the task from the details shown in the TaskDetailsActivity
     */
    public void setValues (Intent intent) {
        tvTitle.setText("EDIT TASK"); // changes the textview to show that the task is only being edited
        btnCreate.setText("EDIT TASK");

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
        boolean notifOn = intent.getBooleanExtra(Keys.KEY_NOTIF_ON.name(), false);
        this.checkerNotif = notifOn;
        this.cbNotif.setChecked(notifOn);

        /* if the user opted for notification, then the spinner for the specific time of the notification
           and when the notification should be is set to its current values.
        * */
        if (notifOn) {
            String notifTime = intent.getStringExtra(Keys.KEY_NOTIF_WHEN.name());

            boolean notifWhen = intent.getBooleanExtra(Keys.KEY_NOTIF_START_TIME.name(), false);
            String temp = "Before End Time";
            if (notifWhen) {
                temp = "Before Start Time";
            }

            notifCode = intent.getIntExtra(Keys.KEY_NOTIF_CODE.name(), -1);

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

        databaseHelper.editTask((list, isSuccessful, message) -> {
            if (isSuccessful) {
                Toast.makeText(AddTaskActivity.this, "Task was successfully edited!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddTaskActivity.this, "Task was not edited successfully!", Toast.LENGTH_SHORT).show();
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
     * Initializes the layout components
     */
    private void setComponents () {
        this.initPriority (); // initializes the arraylist of buttons that represents the priority level of the task
        this.initCategory (); // initializes the arraylist of buttons that represents the category  of the task
        this.initNotifications (); // initializes the spinners and checkbox that handles the notification

        this.etTaskName = findViewById(R.id.et_add_task_name);
        this.etTaskNotes = findViewById(R.id.et_add_task_notes);
        this.etStartTime = findViewById(R.id.et_add_task_start_time);
        this.etStartDate = findViewById(R.id.et_add_task_start_date);
        this.etEndDate = findViewById(R.id.et_add_task_end_date);
        this.etEndTime = findViewById(R.id.et_add_task_end_time);
        this.tvTitle = findViewById(R.id.tv_add_task_start_title);
        this.btnCreate = findViewById(R.id.btn_add_task_create);
        this.cbNotif = findViewById(R.id.cb_add_task_notifs);
        this.spinNotifTime = findViewById(R.id.spin_add_task_notiftime);
        this.spinNotifWhen = findViewById(R.id.spin_add_task_notifwhen);

        this.checkValuesIfValid();
    }

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
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return diff;
    }

    /**
     * Sets the components of the layout to their respective attributes. It also sets the
     * button listener for the btnCreate, which would validate the fields.
     */
    private void checkValuesIfValid (){
        Intent intent = getIntent(); // gets the intent if it is for notiication
        /*
           If there is no intent, it is a new task that is being created.
           Otherwise, it is only editing an existing task.
        */
        String checker = intent.getStringExtra(Keys.KEY_ID.name());
        if (checker != null)
            setValues (intent); // set the values of the fields to the current information from the intent

        this.btnCreate.setOnClickListener(v -> {
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
            if(!when.isEmpty() && when.equalsIgnoreCase("Before Start Time")) {
                val = true;
            }

            // if the checkbox for the notification is selected, the two spinners have a value.
            if (checkerNotif) {
                if (notif.isEmpty()) {
                    ((TextView) spinNotifTime.getSelectedView()).setError("Notification information is required if check box is clicked.");
                    spinNotifTime.getSelectedView().requestFocus();
                    return;
                }
                else if (when.isEmpty()) {
                    ((TextView) spinNotifWhen.getSelectedView()).setError("Notification information is required if check box is clicked.");
                    spinNotifWhen.getSelectedView().requestFocus();
                    return;
                }
            }

            String error = "";
            boolean checker1 = false;

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
                if (endTime.isEmpty()) {
                    error = "End time is required";
                    checker1 = true;
                } else {
                    if (!startDate.equals("")) {
                        if (!startTime.equals("")) {
                            String tempStarTime = startTime.substring(0, 2) + ":" + startTime.substring(3, 8);

                            String tempEndTime = endTime.substring(0, 2) + ":" + endTime.substring(3, 8);

                            // checks if the startDate is earlier than the endDate
                            long diff = getDiff(endDate, startDate, tempEndTime, tempStarTime);

                            if (diff < 0) {
                                error = "End date should be later than the Start date.";
                                checker1 = true;
                            } else if (diff == 0) {
                                error = "Your task should not start and end at the same day and time!";
                                checker1 = true;
                            }
                        }
                        else {
                            error = "Start time is required if start date is provided.";
                            checker1 = true;
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

                    String tempEndTime = endTime.substring(0, 2) + ":" + endTime.substring(3, 8);
                    long diff = getDiff(endDate, currentDate, tempEndTime, currentTime);

                    if (diff < 0) {
                        error = "Your end date should be later than the current time and date!";
                        checker1 = true;
                    } else if (diff == 0) {
                        error = "Your task cannot be currently ending!\n";
                        checker1 = true;
                    }
                }
            } else {
                error = "End date is required.";
                checker1 = true;
            }

            if (endTime.isEmpty()) {
                error = "End time is required";
                checker1 = true;
            }

            // checks if there is no priority button clicked
            if (priority == null) {
                error = "Priority level for this task is required.\n";
                checker1 = true;
            }

            // checks if there is no category button clicked
            if (category == null) {
                error = "The category of this task is required\n";
                checker1 = true;
            }

            if (checkerNotif) {
                if (val) {
                    if (startDate.isEmpty()) {
                        error = "You cannot set the notification to be determined by the start time if start date is empty.";
                        checker1 = true;
                    }
                }

                boolean notifs = sp.getBoolean(Keys.KEY_NOTIFS.name(), true);

                if (!notifs) {
                    error = "You cannot add notifications. If you wish to add notifications, head to settings to allow notifications.";
                    checker1 = true;
                }
            }

            // if there are no errors found, the information is added/edited to the database
            if (!checker1) {
                // if there is an intent from the TaskDetailsActivity, the information is only to be edited from the database
                Intent intent1 = getIntent();
                String taskID = intent1.getStringExtra(Keys.KEY_ID.name());
                if (taskID != null) {
                    editDatabase (taskName, priority.length(), category, startDate, endDate, startTime, endTime, taskNotes,
                            taskID, notif, val);

                    if(checkerNotif) {
                        if (val) {
                            setTimer(new CustomDate(startDate, startTime), notif, true, taskID);
                        } else {
                            deleteTimer();
                            setTimer(new CustomDate(endDate, endTime), notif, false, taskID);
                        }
                    } else {
                        deleteTimer();
                    }

                } else {
                    addToDatabase (taskName, priority.length(), category, startDate, endDate, startTime, endTime, taskNotes, notif, val);
                    if(checkerNotif) {
                        if (val) {
                            setTimer(new CustomDate(startDate, startTime), notif, true, taskID);
                        } else {
                            setTimer(new CustomDate(endDate, endTime), notif, false, taskID);
                        }
                    }
                }
            } else { // if there are errors found, the error dialog should be shown with an error message
                setErrorDialog (error);
            }
        });
    }

    /**
     * Creates the dialog that shows the error for the user's input
     * @param error is the error message
     */
    private void setErrorDialog (String error) {
        errorDialog = new CustomDialog(this);
        errorDialog.setDialogType(CustomDialog.ERROR);
        errorDialog.setErrorComponents("Invalid input!", error);
        errorDialog.show();
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
                btnPriority.add(v);
                btnPriority.get(i).setOnClickListener(v1 -> {
                    Button b = (Button) v1;
                    priority = b.getText().toString();

                    for(int i1 = 0; i1 < clPriority.getChildCount(); i1++) {
                        View view = clPriority.getChildAt(i1);
                        if(view instanceof Button && !view.equals(v1)) {
                            Drawable buttonDrawable = view.getBackground();
                            DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.lighter_gray) );
                            view.setBackground(buttonDrawable);
                        } else if (view.equals(v1)) {
                            Drawable buttonDrawable = v1.getBackground();
                            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                            DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.pink_button));
                            v1.setBackground(buttonDrawable);
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
                btnCategory.add(v);
                btnCategory.get(i).setOnClickListener(v1 -> {
                    Button b = (Button) v1;
                    category = b.getText().toString();

                    for(int i1 = 0; i1 < clCategory.getChildCount(); i1++) {
                        View view = clCategory.getChildAt(i1);
                        if(view instanceof Button && !view.equals(v1)) {
                            Drawable buttonDrawable = view.getBackground();
                            DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.lighter_gray));
                            view.setBackground(buttonDrawable);
                        } else if (view.equals(v1)) {
                            Drawable buttonDrawable = v1.getBackground();
                            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                            DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.pink_button));
                            v1.setBackground(buttonDrawable);
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
        Intent intent = getIntent();
        String sStartDate = intent.getStringExtra(Keys.KEY_C_START_DATE.name());
        String sEndDate = intent.getStringExtra(Keys.KEY_C_END_DATE.name());
        String sStartTime = intent.getStringExtra(Keys.KEY_C_START_TIME.name());
        String sEndTime = intent.getStringExtra(Keys.KEY_C_END_TIME.name());

        EditText startDate = findViewById(R.id.et_add_task_start_date);
        EditText endDate = findViewById(R.id.et_add_task_end_date);

        new CustomDatePicker().createDatePicker(this, startDate, sStartDate);
        new CustomDatePicker().createDatePicker(this, endDate, sEndDate);

        EditText startTime = findViewById(R.id.et_add_task_start_time);
        EditText endTime = findViewById(R.id.et_add_task_end_time);

        new CustomTimePicker().createTimePicker(this, startTime, sStartTime);
        new CustomTimePicker().createTimePicker(this, endTime, sEndTime);
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
        cbNotif.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cbNotif.isChecked()) {
                spinNotifTime.setEnabled(true);
                spinNotifWhen.setEnabled(true);
            }
            else {
            spinNotifTime.setEnabled(false);
            spinNotifWhen.setEnabled(false);
         }
    });
    }

    /**
     * Calculates given the data and the interval to be calculated
     * @param date is the basis date
     * @param notif the time to be subtracted from the basis date
     * @return the time in millis of the calculated time and date
     */
    private long calculateTime (CustomDate date, String notif) {
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

        return c.getTimeInMillis();
    }

    /**
     * Creates the notification based on the information provided.
     * @param date is the day (and specific time) wherein the notification would be created
     * @param notif determines how many minutes/hours/days the notification would be from the date provided
     * @param checker is used for the creation of the notification message
     */
    private void setTimer (CustomDate date, String notif, boolean checker, String taskID) {
        long timeInMillis = calculateTime(date, notif);

        String message = "Don't forget! This ";
        if(checker) {
            message = message + "starts in " + notif;
        } else {
            message = message + "ends in " + notif;
        }

        int requestCode = (int) System.currentTimeMillis();

        if (notifCode != -1) {
            requestCode = notifCode;
        } else {
            databaseHelper.createNotif(new FirebaseCallbackTask() {
                @Override
                public void onCallbackTask(ArrayList<UserTask> list, Boolean isSuccesful, String message) {

                }
            }, requestCode, taskID);

            notifCode = requestCode;
        }

        // creates the alarm manager and the intent to be sent to the broadcast receiver
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderBroadcast.class);
        intent.putExtra("TASKNAME", etTaskName.getText().toString().trim());
        intent.putExtra("NOTIF_MESSAGE", message);
        intent.putExtra("NOTIF_CODE", requestCode);
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);

        // sets the notification time and date
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    /**
     * Creates the notification channel
     */
    private void createNotificationChannel() {
        // checks if the sdk used by the phone is greater than or equal 26 as a channel needs to be created if it is sdk 26 or higher
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "PokePlanReminderChannel";
            String description = "Channel for PokePlan Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("pokeplanNotify", name, importance);
            channel.setDescription(description);

            channel.enableLights(true);
            channel.enableVibration(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Deletes the notification created from the broadcast receiver.
     */
    private void deleteTimer () {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

         Intent intent = new Intent(getApplicationContext(), ReminderBroadcast.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notifCode, intent, 0);

        alarmManager.cancel(pendingIntent);
    }
}