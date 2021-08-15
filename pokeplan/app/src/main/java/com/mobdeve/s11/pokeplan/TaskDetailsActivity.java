package com.mobdeve.s11.pokeplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskDetailsActivity extends AppCompatActivity {
    private ImageButton btnregisterback;
    private TextView tvTaskName;
    private TextView tvCategory;
    private ImageView ivCategory;
    private TextView tvPriorityIcon;
    private TextView tvPriorityName;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvNotesDesc;
    private TextView tvNotesLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        this.tvTaskName = findViewById(R.id.tv_taskdetails_name);
        this.tvCategory = findViewById(R.id.tv_taskdetails_category);
        this.ivCategory = findViewById(R.id.iv_taskdetails_category);
        this.tvPriorityIcon = findViewById(R.id.tv_taskdetails_priority_symbol);
        this.tvStartTime = findViewById(R.id.tv_taskdetails_time_start);
        this.tvEndTime = findViewById(R.id.tv_taskdetails_time_end);
        this.tvNotesDesc = findViewById(R.id.tv_taskdetails_notes);
        this.tvNotesLabel = findViewById(R.id.tv_taskdetails_label_notes);
        this.tvPriorityName = findViewById(R.id.tv_taskdetails_priorty);

        Intent intent = getIntent();

        String taskName = intent.getStringExtra(TaskAdapter.KEY_TASKNAME);
        String category = intent.getStringExtra(TaskAdapter.KEY_CATEGORY);
        int priority = intent.getIntExtra(TaskAdapter.KEY_PRIORITY, 1);
        String startDate = intent.getStringExtra(TaskAdapter.KEY_START_DATE);
        String endDate = intent.getStringExtra(TaskAdapter.KEY_DEADLINE);
        String notes = intent.getStringExtra(TaskAdapter.KEY_NOTES);

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
    }

    private void initBackBtn() {
        btnregisterback = findViewById(R.id.ib_register_back);
        btnregisterback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MainActivity.class);
                view.getContext().startActivity(i);
            }
        });
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