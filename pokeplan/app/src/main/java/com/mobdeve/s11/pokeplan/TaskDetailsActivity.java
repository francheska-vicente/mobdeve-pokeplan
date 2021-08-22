package com.mobdeve.s11.pokeplan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;

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

    private Button btnFinishTask;
    private ImageButton ibDeleteTask;
    private Dialog confirmFinish;
    private Dialog confirmDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

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

        Intent intent = getIntent();

        String taskName = intent.getStringExtra(TaskAdapter.KEY_TASKNAME);
        String category = intent.getStringExtra(TaskAdapter.KEY_CATEGORY);
        int priority = intent.getIntExtra(TaskAdapter.KEY_PRIORITY, 1);
        String startDate = intent.getStringExtra(TaskAdapter.KEY_START_DATE);
        String endDate = intent.getStringExtra(TaskAdapter.KEY_DEADLINE);
        String notes = intent.getStringExtra(TaskAdapter.KEY_NOTES);
        String taskID = intent.getStringExtra(TaskAdapter.KEY_ID);

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
    }

    protected void deleteDialog(View v, String taskID) {
        confirmDelete = new Dialog(v.getContext());

        confirmDelete.setContentView(R.layout.dialog_confirm);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);

        confirmDelete.getWindow().setLayout(width, height);
        confirmDelete.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    protected void createDialog (View v, String taskID) {
        confirmFinish = new Dialog(v.getContext());
        confirmFinish.setContentView(R.layout.dialog_confirm);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);

        confirmFinish.getWindow().setLayout(width, height);
        confirmFinish.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvdialogtitle = (TextView) confirmFinish.findViewById(R.id.tv_dialog_title);
        tvdialogtitle.setText(R.string.task_details_confirm_finish_title);
        TextView tvdialogtext = (TextView) confirmFinish.findViewById(R.id.tv_dialog_text);
        tvdialogtext.setText(R.string.task_details_confirm_finish_text);
        ImageView ivdialogicon = (ImageView) confirmFinish.findViewById(R.id.iv_dialog_icon);
        ivdialogicon.setImageResource(R.drawable.warning);

        Button btndialogcancel = (Button) confirmFinish.findViewById(R.id.btn_dialog_cancel);
        btndialogcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmFinish.dismiss();
            }
        });

        Button btndialogconfirm = (Button) confirmFinish.findViewById(R.id.btn_dialog_confirm);
        btndialogconfirm.setText(R.string.task_details_confirm_finish_button);
        btndialogconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmFinish.dismiss();
                UserSingleton.getUser().moveToCompletedTask(taskID);
                hatchEgg();
            }
        });
        confirmFinish.show();
    }

    protected void hatchEgg () {
        finish();
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
}