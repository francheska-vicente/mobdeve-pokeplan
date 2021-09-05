package com.mobdeve.s11.pokeplan.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.data.DatabaseHelper;
import com.mobdeve.s11.pokeplan.models.UserDetails;
import com.mobdeve.s11.pokeplan.utils.Keys;
import com.mobdeve.s11.pokeplan.views.CustomDialog;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

/**
 * Shows the details of a specific task. This Activity also handles the modification and deletion of the task,
 * and marking an ongoing task to be completed.
 */
public class TaskDetailsActivity extends AppCompatActivity {
    /* Components of the layout */
    private TextView tvTaskName;
    private TextView tvCategory;
    private ImageView ivCategory;
    private TextView tvPriorityIcon;
    private TextView tvPriorityName;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvNotesDesc;
    private TextView tvNotesLabel;
    private TextView tvNotif;

    private Button btnFinishTask;
    private ImageButton ibDeleteTask;
    private ImageButton ibEditTask;

    /* Dialogs that would show the message to the user */
    private CustomDialog confirmFinish;
    private CustomDialog confirmDelete;
    private CustomDialog candyDialog;

    private Intent intent;

    private DatabaseHelper databaseHelper; // allows access to the database
    private UserDetails user;

    /* Information to be shown in the textviews of this activity */
    private String taskName;
    private String category;
    private int priority;
    private String notes;
    private String taskID;
    private String endDate;
    private String startDate;
    private String endTime;
    private String startTime;
    private String notifWhen;
    private Boolean notifOn;
    private Boolean notifStartTime;
    private Boolean isFinished;

    private String fullStartDateString;
    private String fullEndDateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        initInfo();
    }

    /**
     * Initializes the components of the layout once the information has been successfully retrieved from the database.
     * */
    private void initInfo() {
        databaseHelper = new DatabaseHelper(true);
        databaseHelper.getUserDetails((userDetails, isSuccessful, message) -> {
            user = userDetails;
            initComponents();
        });
    }

    /**
     * Initializes the components of the layout and calls the functions that would set their values
     */
    private void initComponents() {
        this.tvTaskName = findViewById(R.id.tv_taskdetails_name);
        this.tvCategory = findViewById(R.id.tv_taskdetails_category);
        this.ivCategory = findViewById(R.id.iv_taskdetails_category);
        this.tvPriorityIcon = findViewById(R.id.tv_taskdetails_priority_symbol);
        this.tvStartTime = findViewById(R.id.tv_taskdetails_time_start);
        this.tvEndTime = findViewById(R.id.tv_taskdetails_time_end);
        this.tvNotesDesc = findViewById(R.id.tv_taskdetails_notes);
        this.tvNotesLabel = findViewById(R.id.tv_taskdetails_label_notes);
        this.tvPriorityName = findViewById(R.id.tv_taskdetails_priorty);
        this.btnFinishTask = findViewById(R.id.btn_task_finish);
        this.ibDeleteTask = findViewById(R.id.ib_taskdetails_delete);
        this.ibEditTask = findViewById(R.id.ib_taskdetails_edit);
        this.tvNotif = findViewById(R.id.tv_taskdetails_notif);

        this.intent = getIntent();
        this.getExtrasFromIntent();

        this.setViewComponentValues();
        this.setButtonListeners();
    }

    /**
     * Sets the onclick listeners of the buttons of the layout
     */
    private void setButtonListeners() {
        ImageButton btnback = findViewById(R.id.ib_taskdetails_back);
        btnback.setOnClickListener(view -> onBackPressed());

        this.ibDeleteTask.setOnClickListener(v -> createDeleteTaskDialog());
        this.ibEditTask.setOnClickListener(v -> editTaskDetails());
        this.btnFinishTask.setOnClickListener(v -> createConfirmFinishDialog());
        if (isFinished)
            this.btnFinishTask.setVisibility(View.GONE);
    }

    /**
     * Sets the values of the components of the layout
     */
    private void setViewComponentValues() {
        this.tvTaskName.setText(taskName);
        this.tvCategory.setText(category);
        this.tvStartTime.setText("Starts " + fullStartDateString);
        this.tvEndTime.setText("Ends " + fullEndDateString);

        this.setPriorityIcon();
        this.ivCategory.setImageResource(categoryToIcon());
        this.tvNotif.setText(formatNotification());

        if (notes == null) {
            this.tvNotesLabel.setVisibility(View.GONE);
            this.tvNotesDesc.setVisibility(View.GONE);
        }
        else {
            this.tvNotesLabel.setVisibility(View.VISIBLE);
            this.tvNotesDesc.setVisibility(View.VISIBLE);
            this.tvNotesDesc.setText(notes);
        }
    }

    /**
     * Sets the icon that represents the priority level of the task
     */
    private void setPriorityIcon() {
        String priorityIcon = "!";
        String priorityName = "Low Priority";
        switch (priority) {
            case 5:
                priorityIcon = "!!!!!";
                priorityName = "Highest Priority";
                break;
            case 4:
                priorityIcon = "!!!!";
                priorityName = "High Priority";
                break;
            case 3:
                priorityIcon = "!!!";
                priorityName = "Mid Priority";
                break;
            case 2:
                priorityIcon = "!!";
                priorityName = "Low Priority";
                break;
        }
        this.tvPriorityIcon.setText(priorityIcon);
        this.tvPriorityName.setText(priorityName);
    }

    /**
     * Returns the integer that represents the icon assigned to a category
     * @return an integer value of the drawable assigned to a category
     */
    private int categoryToIcon() {
        switch (category) {
            case "School": return R.drawable.task_categ_school;
            case "Work": return R.drawable.task_categ_work;
            case "Hobby": return R.drawable.task_categ_hobby;
            case "Leisure": return R.drawable.task_categ_leisure;
            case "Chores": return R.drawable.task_categ_chores;
            case "Health": return R.drawable.task_categ_health;
            case "Social": return R.drawable.task_categ_social;
            default: return R.drawable.task_categ_others;
        }
    }

    /**
     * Formats the value of the notification to be shown in the textview
     * @return a String formatted to show when the notification is
     */
    private String formatNotification() {
        if (notifOn) {
            if (notifStartTime) {
                return notifWhen + " before Start";
            }
            else {
                return notifWhen + " before End";
            }
        }
        return "No set notification for this task.";
    }
    
    private void editTaskDetails() {
        intent = new Intent(TaskDetailsActivity.this, AddTaskActivity.class);

        putExtrasInIntent();
        addActivityResultLauncher.launch(intent);
    }


    private void getExtrasFromIntent() {
        taskName = intent.getStringExtra(Keys.KEY_TASKNAME.name());
        category = intent.getStringExtra(Keys.KEY_CATEGORY.name());
        priority = intent.getIntExtra(Keys.KEY_PRIORITY.name(), 1);
        notes = intent.getStringExtra(Keys.KEY_NOTES.name());
        taskID = intent.getStringExtra(Keys.KEY_ID.name());
        fullEndDateString = intent.getStringExtra(Keys.KEY_END_DATE.name());
        fullStartDateString = intent.getStringExtra(Keys.KEY_START_DATE.name());
        endDate = intent.getStringExtra(Keys.KEY_C_END_DATE.name());
        startDate = intent.getStringExtra(Keys.KEY_C_START_DATE.name());
        endTime = intent.getStringExtra(Keys.KEY_C_END_TIME.name());
        startTime = intent.getStringExtra(Keys.KEY_C_START_TIME.name());
        notifWhen = intent.getStringExtra(Keys.KEY_NOTIF_WHEN.name());
        notifOn = intent.getBooleanExtra(Keys.KEY_NOTIF_ON.name(), false);
        notifStartTime = intent.getBooleanExtra(Keys.KEY_NOTIF_START_TIME.name(), false);
        isFinished = intent.getBooleanExtra(Keys.KEY_IS_COMPLETED.name(), false);
    }

    /**
     * Sets the intent values needed to edit the task. These values are used to pre-fill the information
     * fields based on the current values of the task details.
     */
    private void putExtrasInIntent() {
        intent.putExtra(Keys.KEY_TASKNAME.name(), taskName);
        intent.putExtra(Keys.KEY_CATEGORY.name(), category);
        intent.putExtra(Keys.KEY_PRIORITY.name(), priority);
        intent.putExtra(Keys.KEY_NOTES.name(), notes);
        intent.putExtra(Keys.KEY_ID.name(), taskID);
        intent.putExtra(Keys.KEY_C_END_DATE.name(), endDate);
        intent.putExtra(Keys.KEY_C_START_DATE.name(), startDate);
        intent.putExtra(Keys.KEY_C_START_TIME.name(), startTime);
        intent.putExtra(Keys.KEY_C_END_TIME.name(), endTime);
        intent.putExtra(Keys.KEY_NOTIF_WHEN.name(), notifWhen);
        intent.putExtra(Keys.KEY_NOTIF_ON.name(), notifOn);
        intent.putExtra(Keys.KEY_NOTIF_START_TIME.name(), notifStartTime);
    }

    /**
     * Converts time in 12HR format into 24HR format
     * @param time the time in 12 hour format
     * @return the time in 24 hour format
     */
    private String convertTo24Hour (String time) {
        String [] temp = time.split(":");
        String meridian = temp[1].split(" ")[1];

        int hour = Integer.parseInt(temp [0]);
        int minute = Integer.parseInt(temp[1].split(" ")[0]);

        if (meridian.equalsIgnoreCase("PM") && hour != 12) {
            hour = hour + 12;
        } else if (meridian.equalsIgnoreCase("AM") && hour == 12) {
            hour = 0;
        }

        DecimalFormat format = new DecimalFormat("00");

        return format.format(hour) + ":" + format.format(minute);
    }

    private final ActivityResultLauncher addActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    intent = result.getData();

                    taskName = intent.getStringExtra(Keys.KEY_TASKNAME.name());
                    notes = intent.getStringExtra(Keys.KEY_NOTES.name());
                    fullEndDateString = intent.getStringExtra(Keys.KEY_END_DATE.name());
                    fullStartDateString = intent.getStringExtra(Keys.KEY_START_DATE.name());
                    priority = intent.getIntExtra(Keys.KEY_PRIORITY.name(), 1);
                    category = intent.getStringExtra(Keys.KEY_CATEGORY.name());
                    notifWhen = intent.getStringExtra(Keys.KEY_NOTIF_WHEN.name());
                    endDate = intent.getStringExtra(Keys.KEY_C_END_DATE.name());
                    startDate = intent.getStringExtra(Keys.KEY_C_START_DATE.name());
                    endTime = intent.getStringExtra(Keys.KEY_C_END_TIME.name());
                    startTime = intent.getStringExtra(Keys.KEY_C_START_TIME.name());

                    startTime = convertTo24Hour(startTime);
                    endTime = convertTo24Hour(endTime);

                    notifOn = intent.getBooleanExtra(Keys.KEY_NOTIF_ON.name(), false);
                    notifStartTime = intent.getBooleanExtra(Keys.KEY_NOTIF_START_TIME.name(), false);

                    setViewComponentValues();
                }
                else {
                    initComponents();
                }
            }
    );

    /**
     * Generates an amount of rare candies or super candies after finishing a task
     */
    private void giveCandies() {
        /*
            These five arrays represent the values needed to generate the candies
            for each priority level

            [0] - divider (affects chances of getting rare or super candy)
            [1] - max value for rare candy
            [2] - min value for rare candy
            [3] - max value for super candy
            [4] - min value for super candy
        */

        int[] one = {90, 5, 3, 2, 1};
        int[] two = {85, 10, 4, 3, 2};
        int[] three = {80, 13, 5, 4, 3};
        int[] four = {70, 17, 6, 5, 4};
        int[] five = {60, 20, 7, 6, 7};

        // fix candy rates based on priority
        int priority = this.tvPriorityIcon.getText().toString().trim().length();
        int[] candyrates;
        switch (priority) {
            case 1: candyrates = one; break;
            case 2: candyrates = two; break;
            case 3: candyrates = three; break;
            case 4: candyrates = four; break;
            default: candyrates = five; break;
        }

        // generate candies and update user candy amount
        HashMap<String, Object> hash = new HashMap<>();
        int numberOfCandies;
        int numberGenerated = new Random().nextInt(100) + 1;

        // candy generated was super candy
        if (numberGenerated > candyrates[0]) {
            numberOfCandies = (int) (Math.random () *
                    (candyrates[3] - candyrates[4] + 1) + candyrates[4]);
            user.addSuperCandy(numberOfCandies);

            hash.put("superCandy", user.getSuperCandy());
            createGiveCandyDialog("superCandy", numberOfCandies);
        }

        // candy generated was rare candy
        else {
            numberOfCandies = (int) (Math.random () *
                    (candyrates[1] - candyrates[2] + 1) + candyrates[2]);
            user.addRareCandy(numberOfCandies);
            hash.put("rareCandy", user.getRareCandy());
            createGiveCandyDialog("rareCandy", numberOfCandies);
        }
        databaseHelper.updateUser((userDetails, isSuccessful, message) -> {}, hash);
    }

    /**
     * Creates a confirm dialog for when the user wants to delete a task
     */
    private void createDeleteTaskDialog() {
        confirmDelete = new CustomDialog(this);
        confirmDelete.setDialogType(CustomDialog.CONFIRM);

        confirmDelete.setConfirmComponents(
                getString(R.string.task_details_delete_task_title),
                getString(R.string.task_details_delete_task_text),
                R.drawable.warning,
                getString(R.string.task_details_delete_task_button)
        );

        Button btndialogconfirm = confirmDelete.findViewById(R.id.btn_dialog_confirm);
        btndialogconfirm.setOnClickListener(v -> {
            confirmDelete.dismiss();
            databaseHelper.deleteTask((list, isSuccesful, message) -> {
                finish();
                if (isSuccesful) {
                    Toast.makeText(TaskDetailsActivity.this, "Task was successfully deleted.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(TaskDetailsActivity.this, "Task was not deleted.", Toast.LENGTH_SHORT).show();
                }
            }, taskID);
        });
        confirmDelete.show();
    }

    /**
     * Creates a confirm dialog for when the user wants to finish a task
     */
    private void createConfirmFinishDialog() {
        confirmFinish = new CustomDialog(this);
        confirmFinish.setDialogType(CustomDialog.CONFIRM);

        confirmFinish.setConfirmComponents(
                getString(R.string.task_details_confirm_finish_title),
                getString(R.string.task_details_confirm_finish_text),
                R.drawable.medal,
                getString(R.string.task_details_confirm_finish_button)
        );

        Button btndialogconfirm = confirmFinish.findViewById(R.id.btn_dialog_confirm);
        btndialogconfirm.setOnClickListener(v -> {
            confirmFinish.dismiss();

            databaseHelper.moveToCompletedTask((list, isSuccesful, message) -> giveCandies(), taskID, user);
        });
        confirmFinish.show();
    }

    /**
     * Creates an OK dialog for when the user receives candy
     */
    private void createGiveCandyDialog(String candyType, int numberOfCandies) {
        candyDialog = new CustomDialog(TaskDetailsActivity.this);
        candyDialog.setDialogType(CustomDialog.OK);

        String body = "You got " + numberOfCandies;
        int icon;
        if (candyType.equalsIgnoreCase("rareCandy")) {
            icon = R.drawable.rarecandy;
            body = body + " Rare Candies";
        }
        else {
            icon = R.drawable.supercandy;
            body = body + " Super Candies";
        }
        body = body + " for completing this task!";

        candyDialog.setOKComponents(
                getString(R.string.task_details_candy_title),
                body,
                icon);

        Button btndialogok = candyDialog.findViewById(R.id.btn_dialog_ok);
        btndialogok.setOnClickListener(v -> {
            candyDialog.dismiss();
            finish();
        });
        candyDialog.show();

        AudioManager audioManager =
                (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume
                (AudioManager.STREAM_MUSIC, 5,0);
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.finishtask);
        mediaPlayer.start();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        initComponents();
    }
}