package com.mobdeve.s11.pokeplan;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TasklistFragment extends Fragment {

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

                String name =  intent.getStringExtra(AddTaskActivity.KEY_TASKNAME);
                String category =  intent.getStringExtra(AddTaskActivity.KEY_CATEGORY);
                int priority = intent.getIntExtra(AddTaskActivity.KEY_PRIORITY, 1);
                String startDate = intent.getStringExtra(AddTaskActivity.KEY_START_DATE);
                String endDate = intent.getStringExtra(AddTaskActivity.KEY_END_DATE);
                String startTime = intent.getStringExtra(AddTaskActivity.KEY_START_TIME);
                String endTime = intent.getStringExtra(AddTaskActivity.KEY_END_TIME);
                String notes = intent.getStringExtra(AddTaskActivity.KEY_NOTES);

                // ongoingList.add(0 , new Task(name, priority, category, notes));
                taskAdapterOngoing.notifyItemChanged(0);
                taskAdapterOngoing.notifyItemRangeChanged(0, taskAdapterOngoing.getItemCount());


            }
        }
    );

    public TasklistFragment() {
    }

    public static TasklistFragment newInstance() {
        TasklistFragment fragment = new TasklistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasklist, container, false);
        initFabAdd(view);
        initComponents(view);
        return view;
    }

    private void initComponents (View view) {
        TaskDataHelper helper = new TaskDataHelper();

        this.ongoingList = helper.getOngoingList();

        this.rvOngoing = view.findViewById(R.id.rv_tasklist_ongoing);
        this.rvOngoing.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        this.taskAdapterOngoing = new TaskAdapter(this.ongoingList);
        this.rvOngoing.setAdapter(this.taskAdapterOngoing);

        this.completedList = helper.getCompletedList();

        this.rvCompleted = view.findViewById(R.id.rv_tasklist_completed);
        this.rvCompleted.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        this.taskAdapterCompleted = new TaskAdapter(this.completedList);
        this.rvCompleted.setAdapter(this.taskAdapterCompleted);
    }

    private void initFabAdd (View view) {
        this.fabAdd = view.findViewById(R.id.fab_tasklist_add_task);
        this.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);

                addActivityResultLauncher.launch(intent);
            }
        });

    }
}