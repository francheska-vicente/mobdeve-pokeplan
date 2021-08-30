package com.mobdeve.s11.pokeplan;

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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

public class TaskDetailsActivity extends AppCompatActivity {
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
    private CustomDialog confirmFinish;
    private CustomDialog confirmDelete;
    private CustomDialog candyDialog;

    private String taskID;

    private DatabaseHelper databaseHelper;
    private UserDetails user;
    private Task task;

    private boolean wasEdited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Intent intent = getIntent();
        taskID = intent.getStringExtra(Keys.KEY_ID.name());

        initInfo();

        ImageButton btnback = findViewById(R.id.ib_taskdetails_back);
        btnback.setOnClickListener(view -> onBackPressed());
    }

    private void initInfo() {
        databaseHelper = new DatabaseHelper();
        databaseHelper.getUserDetails((userDetails, isSuccessful, message) -> {
            user = userDetails;
            initComponents();
        });
    }

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

        if (wasEdited)
            updateViewComponents(taskID);
        else
            setViewComponents();
        setButtonListeners();
    }

    private void setButtonListeners() {
        this.btnFinishTask.setOnClickListener(v -> createConfirmFinishDialog(v, taskID));
        this.ibDeleteTask.setOnClickListener(v -> createDeleteTaskDialog(v, taskID));
    }

    private void setViewComponents () {
        Intent intent = getIntent();

        String taskName = intent.getStringExtra(Keys.KEY_TASKNAME.name());
        String category = intent.getStringExtra(Keys.KEY_CATEGORY.name());
        int priority = intent.getIntExtra(Keys.KEY_PRIORITY.name(), 1);
        String startDate = intent.getStringExtra(Keys.KEY_START_DATE.name());
        String endDate = intent.getStringExtra(Keys.KEY_DEADLINE.name());
        String notes = intent.getStringExtra(Keys.KEY_NOTES.name());
        String taskID = intent.getStringExtra(Keys.KEY_ID.name());
        String cEndDate = intent.getStringExtra(Keys.KEY_C_END_DATE.name());
        String cStartDate = intent.getStringExtra(Keys.KEY_C_START_DATE.name());
        String cEndTime = intent.getStringExtra(Keys.KEY_C_END_TIME.name());
        String cStartTime = intent.getStringExtra(Keys.KEY_C_START_TIME.name());
        String notifWhen = intent.getStringExtra(Keys.KEY_NOTIF_WHEN.name());
        Boolean notifOn = intent.getBooleanExtra(Keys.KEY_NOTIF_ON.name(), false);
        Boolean notifStartTime = intent.getBooleanExtra(Keys.KEY_NOTIF_START_TIME.name(), false);

        setViewComponentValues(taskName, category, startDate, endDate,
                notes, priority, notifWhen, notifOn, notifStartTime);

        this.ibEditTask.setOnClickListener(v -> editTaskDetails(taskName, category, priority, notes, taskID, cEndDate, cStartDate, cEndTime, cStartTime,
                notifWhen, notifOn, notifStartTime));
    }

    private void updateViewComponents (String taskID) {
        databaseHelper.getTasks((list, isSuccesful, message) -> {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTaskID().equalsIgnoreCase(taskID)) {
                    task = list.get(i);
                    break;
                }
            }

            String taskName = task.getTaskName();
            String category = task.getCategory();
            int priority = task.getPriority();
            Boolean notifOn = task.getIsNotif();
            Boolean notifStartTime = task.getBeforeStartTime();
            String notifWhen = task.getNotifWhen();
            String notes = task.getDescription();
            CustomDate startDate = task.getStartDate();
            CustomDate endDate = task.getEndDate();

            DecimalFormat formatter = new DecimalFormat("00");

            String sEndDate = formatter.format(endDate.getDay()) + "." + formatter.format(endDate.getMonth()) + "." +
                    endDate.getYear();
            String sStartDate = formatter.format(startDate.getDay()) + "." + formatter.format(startDate.getMonth()) + "." +
                    startDate.getYear();
            String sEndTime = formatter.format(endDate.getHour()) + ":" + formatter.format(endDate.getMinute());
            String sStartTime = formatter.format(startDate.getHour()) + ":" + formatter.format(startDate.getMinute());

            ibEditTask.setOnClickListener(v -> editTaskDetails(taskName, category, priority, notes, taskID, sEndDate, sStartDate, sEndTime, sStartTime,
                    notifWhen, notifOn, notifStartTime));
        });
    }

    private void setViewComponentValues (String taskName, String category, String startDate, String endDate,
                            String notes, int priority, String notifWhen, Boolean notifOn, Boolean notifStartTime) {
        this.tvTaskName.setText(taskName);
        this.tvCategory.setText(category);

        this.tvStartTime.setText("Starts " + startDate);
        this.tvEndTime.setText("Ends " + endDate);

        if (notes == null) {
            this.tvNotesLabel.setVisibility(View.GONE);
            this.tvNotesDesc.setVisibility(View.GONE);
        } else {
            this.tvNotesLabel.setVisibility(View.VISIBLE);
            this.tvNotesDesc.setVisibility(View.VISIBLE);
            this.tvNotesDesc.setText(notes);
        }

        this.setCategoryIcon(category);
        this.setPriorityIcon(priority);

        String notif = "No set notification for this task.";
        if (notifOn) {
            if (notifStartTime) {
                notif = notifWhen + " before Start";
            }
            else {
                notif = notifWhen + " before End";
            }
        }

        this.tvNotif.setText(notif);
    }

    private void editTaskDetails (String taskName, String category, int priority, String notes, String taskID,
                             String endDate, String startDate, String endTime, String startTime, String notifWhen,
                             Boolean notifOn, Boolean notifStartTime) {
        Intent intent = new Intent(TaskDetailsActivity.this, AddTaskActivity.class);

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

        addActivityResultLauncher.launch(intent);
    }


    private ActivityResultLauncher addActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();

                    String name = intent.getStringExtra(Keys.KEY_TASKNAME.name());
                    String notes = intent.getStringExtra(Keys.KEY_NOTES.name());
                    String endDate = intent.getStringExtra(Keys.KEY_END_DATE.name());
                    String startDate = intent.getStringExtra(Keys.KEY_START_DATE.name());
                    int priority = intent.getIntExtra(Keys.KEY_PRIORITY.name(), 1);
                    String category = intent.getStringExtra(Keys.KEY_CATEGORY.name());
                    String notifWhen = intent.getStringExtra(Keys.KEY_NOTIF_WHEN.name());
                    Boolean notifOn = intent.getBooleanExtra(Keys.KEY_NOTIF_ON.name(), false);
                    Boolean notifStartTime = intent.getBooleanExtra(Keys.KEY_NOTIF_START_TIME.name(), false);

                    setViewComponentValues(name, category, startDate, endDate,
                            notes, priority, notifWhen, notifOn, notifStartTime);
                    wasEdited = true;
                }
                else {
                    initComponents();
                }
            }
    );

    private void giveCandies () {
        /*
            These five arrays represent the values needed to generate the candies
            for each priority level

            [0] - divider
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

        int priority = this.tvPriorityIcon.getText().toString().trim().length();

        int[] candyrates;
        switch (priority) {
            case 1: candyrates = one; break;
            case 2: candyrates = two; break;
            case 3: candyrates = three; break;
            case 4: candyrates = four; break;
            default: candyrates = five; break;
        }

        HashMap<String, Object> hash = new HashMap<>();
        int numberOfCandies;

        int numberGenerated = new Random().nextInt(100) + 1;
        if (numberGenerated > candyrates[0]) {
            numberOfCandies = (int) (Math.random () *
                    (candyrates[3] - candyrates[4] + 1) + candyrates[4]);
            user.addSuperCandy(numberOfCandies);

            hash.put("superCandy", user.getSuperCandy());
            createGiveCandyDialog("superCandy", numberOfCandies);
        }
        else {
            numberOfCandies = (int) (Math.random () *
                    (candyrates[1] - candyrates[2] + 1) + candyrates[2]);
            user.addRareCandy(numberOfCandies);
            hash.put("rareCandy", user.getRareCandy());
            createGiveCandyDialog("rareCandy", numberOfCandies);
        }

        databaseHelper.updateUser((userDetails, isSuccessful, message) -> {}, hash);
    }

    private void createDeleteTaskDialog(View view, String taskID) {
        confirmDelete = new CustomDialog(view.getContext());
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
            databaseHelper.deleteTask((list, isSuccesful, message) -> finish(), taskID);
        });
        confirmDelete.show();
    }

    private void createConfirmFinishDialog(View view, String taskID) {
        confirmFinish = new CustomDialog(view.getContext());
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

    private void createGiveCandyDialog (String candyType, int numberOfCandies) {
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

    private void setCategoryIcon (String category) {
        int pic;
        if (category != null) {
            switch (category) {
                case "School": pic = R.drawable.task_categ_school; break;
                case "Work": pic = R.drawable.task_categ_work; break;
                case "Hobby": pic = R.drawable.task_categ_hobby; break;
                case "Leisure": pic = R.drawable.task_categ_leisure; break;
                case "Chores": pic = R.drawable.task_categ_chores; break;
                case "Health": pic = R.drawable.task_categ_health; break;
                case "Social": pic = R.drawable.task_categ_social; break;
                default: pic = R.drawable.task_categ_others; break;
            }
            this.ivCategory.setImageResource(pic);
        }
    }

    private void setPriorityIcon (int priority) {
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

    @Override
    public void onRestart() {
        super.onRestart();
        initComponents();
    }
}