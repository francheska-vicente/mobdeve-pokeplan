package com.mobdeve.s11.pokeplan;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TasklistFragment extends Fragment {

    private ArrayList<Task> ongoingList;
    private RecyclerView rvOngoing;
    private ImageButton ibOngoingToggle;
    private boolean ongoingIsVisible;

    private ArrayList<Task> completedList;
    private RecyclerView rvCompleted;
    private ImageButton ibCompletedToggle;
    private boolean completedIsVisible;

    private TaskAdapter taskAdapterCompleted;
    private TaskAdapter taskAdapterOngoing;
    private FloatingActionButton fabAdd;

    private ActivityResultLauncher addActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

            private int convertHour (int hour, String temp) {
                    if (hour == 12) {
                        if (temp == "AM")
                            hour = 0;
                    } else if (temp == "PM") {
                        hour = hour + 12;
                    }

                    return hour;
            }

            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent = result.getData();

                if (intent != null) {
                    String name =  intent.getStringExtra(AddTaskActivity.KEY_TASKNAME);
                    String category =  intent.getStringExtra(AddTaskActivity.KEY_CATEGORY);
                    int priority = intent.getIntExtra(AddTaskActivity.KEY_PRIORITY, 1);
                    String startDate = intent.getStringExtra(AddTaskActivity.KEY_START_DATE);
                    String endDate = intent.getStringExtra(AddTaskActivity.KEY_END_DATE);
                    String startTime = intent.getStringExtra(AddTaskActivity.KEY_START_TIME);
                    String endTime = intent.getStringExtra(AddTaskActivity.KEY_END_TIME);
                    String notes = intent.getStringExtra(AddTaskActivity.KEY_NOTES);

                    String [] tempStartDate = startDate.split(".", 3);
                    int monthStart = Integer.parseInt(tempStartDate [1]);
                    int dayStart = Integer.parseInt(tempStartDate [0]);
                    int yearStart = Integer.parseInt(tempStartDate [2]);

                    String [] tempEndDate = endDate.split(".", 3);
                    int monthEnd = Integer.parseInt(tempEndDate [1]);
                    int dayEnd = Integer.parseInt(tempEndDate [0]);
                    int yearEnd = Integer.parseInt(tempEndDate [2]);

                    String [] tempStartTime = startTime.split(".", 2);
                    String [] temp = tempStartDate [1].split(" ", 2);

                    int hourStart = Integer.parseInt(tempStartTime [0]);
                    int minuteStart = Integer.parseInt(temp [0]);

                    hourStart = this.convertHour (hourStart, temp [1]);

                    String [] tempEndTime = endTime.split(".", 2);
                    temp = tempEndDate [1].split (" ", 2);

                    int hourEnd = Integer.parseInt(tempEndTime [0]);
                    int minuteEnd = Integer.parseInt(temp [0]);

                    hourEnd = this.convertHour(hourEnd, temp [1]);

                    ongoingList.add(0 , new Task(name, priority, category,
                            new CustomDate(yearStart, monthStart, dayStart, hourStart, minuteStart),
                            new CustomDate(yearEnd, monthEnd, dayEnd, hourEnd, minuteEnd), notes));
                    taskAdapterOngoing.notifyItemChanged(0);
                    taskAdapterOngoing.notifyItemRangeChanged(0, taskAdapterOngoing.getItemCount());
                }
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

        this.ibOngoingToggle = view.findViewById(R.id.ib_tasklist_toggleongoing);
        this.ongoingIsVisible = true;
        this.ibOngoingToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ongoingIsVisible) {
                    ongoingIsVisible = false;
                    ibOngoingToggle.setImageResource(R.drawable.arrow_down);
                    rvOngoing.setVisibility(View.GONE);
                }
                else {
                    ongoingIsVisible = true;
                    ibOngoingToggle.setImageResource(R.drawable.arrow_up);
                    rvOngoing.setVisibility(View.VISIBLE);
                }
            }
        });
        this.ibCompletedToggle = view.findViewById(R.id.ib_tasklist_togglecompleted);
        this.completedIsVisible = true;
        this.ibCompletedToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(completedIsVisible) {
                    completedIsVisible = false;
                    ibCompletedToggle.setImageResource(R.drawable.arrow_down);
                    rvCompleted.setVisibility(View.GONE);
                }
                else {
                    completedIsVisible = true;
                    ibCompletedToggle.setImageResource(R.drawable.arrow_up);
                    rvCompleted.setVisibility(View.VISIBLE);
                }
            }
        });
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