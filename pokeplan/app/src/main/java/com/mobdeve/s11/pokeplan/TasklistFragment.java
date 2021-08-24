 package com.mobdeve.s11.pokeplan;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

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
    private Spinner spinFilter;

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
        this.ongoingList = UserSingleton.getUser().getOngoingTasks();
        this.rvOngoing = view.findViewById(R.id.rv_tasklist_ongoing);
        this.rvOngoing.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        this.completedList = UserSingleton.getUser().getCompletedTasks();
        this.rvCompleted = view.findViewById(R.id.rv_tasklist_completed);
        this.rvCompleted.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        initLists(this.ongoingList, this.completedList);

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

        initFilter (view);
    }

    private void initFilter (View view) {
        this.spinFilter = view.findViewById(R.id.spinner_tasklist_filter);
        ArrayAdapter<CharSequence> adapterFilter = ArrayAdapter.createFromResource(getContext(),
                R.array.tasklist_filter, R.layout.spinner_item);
        adapterFilter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinFilter.setAdapter(adapterFilter);

        spinFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterTask(spinFilter.getSelectedItem().toString());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    private void initFabAdd (View view) {
        this.fabAdd = view.findViewById(R.id.fab_tasklist_add_task);
        this.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initLists (ArrayList<Task> ongoingList, ArrayList<Task> completedList) {
        this.taskAdapterOngoing = new TaskAdapter(ongoingList);
        this.rvOngoing.setAdapter(this.taskAdapterOngoing);
        this.taskAdapterCompleted = new TaskAdapter(completedList);
        this.rvCompleted.setAdapter(this.taskAdapterCompleted);

        this.taskAdapterCompleted.notifyDataSetChanged();
        this.taskAdapterOngoing.notifyDataSetChanged();
    }

    private void filterTask (String category) {
        if (category.equalsIgnoreCase("ALL")) {
            initLists(this.ongoingList, this.completedList);
        } else {
            ArrayList<Task> tempCompleted = new ArrayList<>();
            ArrayList<Task> tempOngoing = new ArrayList<>();

            for (int i = 0; i < this.completedList.size(); i++) {
                if (this.completedList.get(i).getCategory().equalsIgnoreCase(category)) {
                    tempCompleted.add(this.completedList.get(i));
                }
            }

            for (int i = 0; i < this.ongoingList.size(); i++) {
                if (this.ongoingList.get(i).getCategory().equalsIgnoreCase(category)) {
                    tempOngoing.add(this.ongoingList.get(i));
                }
            }

            initLists(tempOngoing, tempCompleted);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        initComponents(getView());
    }
}