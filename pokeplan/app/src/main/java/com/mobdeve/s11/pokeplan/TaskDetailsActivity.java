package com.mobdeve.s11.pokeplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TaskDetailsActivity extends AppCompatActivity {
    private ImageButton btnback;
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

    private DatabaseHelper databaseHelper;
    private UserDetails user;

    private boolean wasEdited;

    public static final String KEY_TASKNAME = "KEY_TASKNAME";
    public static final String KEY_CATEGORY = "KEY_CATEGORY";
    public static final String KEY_PRIORITY = "KEY_PRIORITY";
    public static final String KEY_C_START_DATE = "KEY_C_START_DATE";
    public static final String KEY_C_END_DATE = "KEY_C_END_DATE";
    public static final String KEY_C_START_TIME = "KEY_C_START_TIME";
    public static final String KEY_C_END_TIME = "KEY_C_END_TIME";

    public static final String KEY_NOTIF_WHEN = "KEY_NOTIF_WHEN";
    public static final String KEY_NOTIF_ON = "KEY_NOTIF_ON";
    public static final String KEY_NOTIF_START_TIME = "KEY_NOTIF_START_TIME";

    public static final String KEY_NOTES = "KEY_NOTES";
    public static final String KEY_ID = "KEY_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        databaseHelper = new DatabaseHelper();
        wasEdited = false;

        btnback = findViewById(R.id.ib_taskdetails_back);
        btnback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });

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

        databaseHelper.getUserDetails(new FirebaseCallbackUser() {
            @Override
            public void onCallbackUser(UserDetails userDetails, Boolean isSuccessful, String message) {
                user = userDetails;
                initComponents();
            }
        });
    }

    private void initComponents () {
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
        Boolean isFinished = intent.getBooleanExtra(Keys.KEY_IS_COMPLETED.name(), false);


//        if (wasEdited) {
//            taskName = this.tvTaskName.getText().toString();
//            category = this.tvCategory.getText().toString();
//            priority = this.tvPriorityIcon.getText().toString().length();
//
//            String [] temp = this.tvNotif.getText().toString().split(" ");
//
//            if (temp [0].equalsIgnoreCase("No")) {
//                notifOn = false;
//                notifStartTime = false;
//                notifWhen = "";
//            } else {
//                notifOn = true;
//                notifWhen = temp [0] + " " + temp [1];
//
//                if (temp [3].equalsIgnoreCase("Start")) {
//                    notifStartTime = true;
//                } else {
//                    notifStartTime = false;
//                }
//            }
//
//        } else {
//            taskName = intent.getStringExtra(Keys.KEY_TASKNAME.name());
//            category = intent.getStringExtra(Keys.KEY_CATEGORY.name());
//            priority = intent.getIntExtra(Keys.KEY_PRIORITY.name(), 1);
//            startDate = intent.getStringExtra(Keys.KEY_START_DATE.name());
//            endDate = intent.getStringExtra(Keys.KEY_DEADLINE.name());
//            notes = intent.getStringExtra(Keys.KEY_NOTES.name());
//            cEndDate = intent.getStringExtra(Keys.KEY_C_END_DATE.name());
//            cStartDate = intent.getStringExtra(Keys.KEY_C_START_DATE.name());
//            cEndTime = intent.getStringExtra(Keys.KEY_C_END_TIME.name());
//            cStartTime = intent.getStringExtra(Keys.KEY_C_START_TIME.name());
//            notifWhen = intent.getStringExtra(Keys.KEY_NOTIF_WHEN.name());
//            notifOn = intent.getBooleanExtra(Keys.KEY_NOTIF_ON.name(), false);
//            notifStartTime = intent.getBooleanExtra(Keys.KEY_NOTIF_START_TIME.name(), false);
//        }

        if (isFinished) {
            this.btnFinishTask.setVisibility(View.GONE);
        }

        setValues (taskName, category, startDate, endDate,
                notes, priority, notifWhen, notifOn, notifStartTime);

        this.btnFinishTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(v, taskID);
            }
        });

        this.ibDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog(v, taskID);
            }
        });

        this.ibEditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editIntent(taskName, category, priority, notes, taskID, cEndDate, cStartDate, cEndTime, cStartTime,
                        notifWhen, notifOn, notifStartTime);
            }
        });
    }

    private void setValues (String taskName, String category, String startDate, String endDate,
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
        
        String notif = "";
        if (notifOn) {
            if (notifStartTime) {
                notif = notifWhen + " before Start";
            } else {
                notif = notifWhen + " before End";
            }
        } else {
            notif = "No set notification for this task.";
        }

        this.tvNotif.setText(notif);
    }

    private void editIntent (String taskName, String category, int priority, String notes, String taskID,
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
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

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

                        setValues(name, category, startDate, endDate,
                                notes, priority, notifWhen, notifOn, notifStartTime);
                        wasEdited = true;
                    } else {
                        initComponents();
                    }
                }
            }
    );

    protected void deleteDialog(View view, String taskID) {
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
            databaseHelper.deleteTask(new FirebaseCallbackTask() {
                @Override
                public void onCallbackTask(ArrayList<Task> list, Boolean isSuccesful, String message) {
                    finish();
                }
            }, taskID);
        });
        confirmDelete.show();
    }

    protected void createDialog (View view, String taskID) {
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
            databaseHelper.moveToCompletedTask(new FirebaseCallbackTask() {
                @Override
                public void onCallbackTask(ArrayList<Task> list, Boolean isSuccesful, String message) {
                    giveCandies();
                }
            }, taskID, user);

        });
        confirmFinish.show();
    }

    protected void giveCandies () {
        int divider = 90, maxR = 5, minR = 3, maxS = 2, minS = 1;
        int priority = this.tvPriorityIcon.getText().toString().trim().length();

        switch (priority) {
            case 2: divider = 85;
                maxR = 10;
                minR = 4;
                maxS = 3;
                minS = 2;
                break;
            case 3: divider = 80;
                maxR = 13;
                minR = 5;
                maxS = 4;
                minS = 3;
                break;
            case 4: divider = 70;
                maxR = 17;
                minR = 6;
                maxS = 5;
                minS = 4;
                break;
            case 5: divider = 60;
                maxR = 20;
                minR = 7;
                maxS = 6;
                minS = 7;
                break;
        }

        Random random = new Random();
        int numberGenerated = random.nextInt(100) + 1;
        String candyType = "rareCandy";

        int numberROfCandies = (int) (Math.random () * (maxR - minR + 1) + minR);
        int numberSOfCandies = (int) (Math.random () * (maxS - minS + 1) + minS);

        int totalNumberOfcandies = 0, numberOfCandies = numberROfCandies;
        if (numberGenerated > divider) {
            candyType = "superCandy";
            numberOfCandies = numberSOfCandies;
            user.addSuperCandy(numberSOfCandies);
            totalNumberOfcandies = user.getSuperCandy();
        } else {
            user.addRareCandy(numberROfCandies);
            totalNumberOfcandies = user.getRareCandy();
        }

        HashMap<String, Object> hash = new HashMap<>();
        hash.put(candyType, totalNumberOfcandies);

        databaseHelper.updateUser(new FirebaseCallbackUser() {
            @Override
            public void onCallbackUser(UserDetails userDetails, Boolean isSuccessful, String message) {
                if (isSuccessful) {

                }
            }
        }, hash);

        createCandyDialog(candyType, numberOfCandies);
    }

    private void createCandyDialog (String candyType, int numberOfCandies) {
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