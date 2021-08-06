package com.mobdeve.s11.pokeplan;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TasklistActivity extends AppCompatActivity {

    private ArrayList<Task> ongoingList;
    private RecyclerView rvOngoing;

    private ArrayList<Task> completedList;
    private RecyclerView rvCompleted;

    private TaskAdapter taskAdapterCompleted;
    private TaskAdapter taskAdapterOngoing;
    private FloatingActionButton fabAdd;

    private ActivityResultLauncher addActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent = result.getData();

                /* TODO: add intent */
            }
        }
    );
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasklist);

        initComponents();
        initFabAdd();
    }

    private void initComponents () {
        TaskDataHelper helper = new TaskDataHelper();

        this.ongoingList = helper.getOngoingList();

        this.rvOngoing = findViewById(R.id.rv_tasklist_ongoing);
        this.rvOngoing.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        this.taskAdapterOngoing = new TaskAdapter(this.ongoingList);
        this.rvOngoing.setAdapter(this.taskAdapterOngoing);

        this.completedList = helper.getCompletedList();

        this.rvCompleted = findViewById(R.id.rv_tasklist_completed);
        this.rvCompleted.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        this.taskAdapterCompleted = new TaskAdapter(this.completedList);
        this.rvCompleted.setAdapter(this.taskAdapterCompleted);
    }

    private void initFabAdd () {
        this.fabAdd = findViewById(R.id.fab_tasklist_add_task);
        this.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TasklistActivity.this, AddTaskActivity.class);

                addActivityResultLauncher.launch(intent);
            }
        });

    }
}