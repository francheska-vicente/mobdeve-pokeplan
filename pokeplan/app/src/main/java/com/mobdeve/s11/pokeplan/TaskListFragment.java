package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TaskListFragment extends Fragment {
    private ArrayList<Task> ongoingList;
    private RecyclerView rvOngoing;
    private ImageButton ibOngoingToggle;
    private boolean ongoingIsVisible = true;
    private TextView tvOngoingLabel;

    private ArrayList<Task> completedList;
    private RecyclerView rvCompleted;
    private ImageButton ibCompletedToggle;

    // sets the ongoing list toggle
    private boolean completedIsVisible = true;
    private TextView tvCompletedLabel;

    private TaskAdapter taskAdapterCompleted;
    private TaskAdapter taskAdapterOngoing;
    private FloatingActionButton fabAdd;

    private Spinner spinFilter;
    private ImageButton ibFilter;
    private TextView tvFilterLabel;
    private boolean filterIsVisible = false;

    private DatabaseHelper databaseHelper;

    public TaskListFragment() {
    }

    public static TaskListFragment newInstance() {
        return new TaskListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasklist, container, false);

        initInfo (view);
        return view;
    }

    /**
     * Retrieves the user's task list from the database
     * @param view the View of the fragment
     */
    private void initInfo(View view) {
        databaseHelper = new DatabaseHelper();
        databaseHelper.getTasks((list, isSuccesful, message) -> {
            classifyTasks(list);
            initComponents(view);
        });
    }

    /**
     * Classifies the user's tasks into Ongoing and Completed tasks
     * @param list the list of all the user's tasks
     */
    private void classifyTasks(ArrayList<Task> list) {
        ongoingList = new ArrayList<>();
        completedList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getIsFinished())
                completedList.add(list.get(i));
            else
                ongoingList.add(list.get(i));
        }
    }

    /**
     * Initializes the layout's components.
     * @param view the view of the fragment
     */
    private void initComponents (View view) {
        this.rvOngoing = view.findViewById(R.id.rv_tasklist_ongoing);
        this.rvOngoing.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        this.rvCompleted = view.findViewById(R.id.rv_tasklist_completed);
        this.rvCompleted.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        this.setTaskLists(this.ongoingList, this.completedList);

        this.tvOngoingLabel = view.findViewById(R.id.tv_tasklist_label_ongoing);
        this.tvCompletedLabel = view.findViewById(R.id.tv_tasklist_label_completed);
        this.ibOngoingToggle = view.findViewById(R.id.ib_tasklist_toggleongoing);
        this.ibCompletedToggle = view.findViewById(R.id.ib_tasklist_togglecompleted);
        this.initToggleComponents();

        this.spinFilter = view.findViewById(R.id.spinner_tasklist_filter);
        this.ibFilter = view.findViewById(R.id.ib_filter);
        this.tvFilterLabel = view.findViewById(R.id.tv_tasklist_filter_label);
        this.initFilterComponents();

        this.fabAdd = view.findViewById(R.id.fab_tasklist_add_task);
        this.setFabListener();
    }

    /**
     * Initializes components used for the show/hide task list feature
     */
    private void initToggleComponents() {
        // sets the toggle for ongoing list
        View.OnClickListener ongoingListener = v -> {
            if(ongoingIsVisible) {
                ibOngoingToggle.setImageResource(R.drawable.arrow_down);
                rvOngoing.setVisibility(View.GONE);
            }
            else {
                ibOngoingToggle.setImageResource(R.drawable.arrow_up);
                rvOngoing.setVisibility(View.VISIBLE);
            }
            ongoingIsVisible = !ongoingIsVisible;
        };
        this.ibOngoingToggle.setOnClickListener(ongoingListener);
        this.tvOngoingLabel.setOnClickListener(ongoingListener);

        // sets the toggle for completed list
        View.OnClickListener completedListener = v -> {
            if(completedIsVisible) {
                ibCompletedToggle.setImageResource(R.drawable.arrow_down);
                rvCompleted.setVisibility(View.GONE);
            }
            else {
                ibCompletedToggle.setImageResource(R.drawable.arrow_up);
                rvCompleted.setVisibility(View.VISIBLE);
            }
            completedIsVisible = !completedIsVisible;
        };
        this.ibCompletedToggle.setOnClickListener(completedListener);
        this.tvCompletedLabel.setOnClickListener(completedListener);
    }

    /**
     * Initializes components used for the filter feature
     */
    private void initFilterComponents() {
        // sets the toggle for showing filter spinner
        View.OnClickListener filterListener = v -> {
            if (filterIsVisible) {
                spinFilter.setVisibility(View.INVISIBLE);
                tvFilterLabel.setVisibility(View.VISIBLE);
            }
            else {
                spinFilter.setVisibility(View.VISIBLE);
                tvFilterLabel.setVisibility(View.INVISIBLE);
            }
            filterIsVisible = !filterIsVisible;
        };
        this.ibFilter.setOnClickListener(filterListener);
        this.tvFilterLabel.setOnClickListener(filterListener);

        // initializes filter spinner
        ArrayAdapter<CharSequence> adapterFilter = ArrayAdapter.createFromResource(getContext(),
                R.array.tasklist_filter, R.layout.spinner_item);
        adapterFilter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinFilter.setAdapter(adapterFilter);
        spinFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterTask(spinFilter.getSelectedItem().toString());
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    /**
     * Sets listener for the FloatingActionButton
     */
    private void setFabListener() {
        this.fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTaskActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Sets the adapter for the RecyclerViews
     * @param ongoingList the list of the user's ongoing tasks
     * @param completedList the list of the user's completed tasks
     */
    private void setTaskLists (ArrayList<Task> ongoingList, ArrayList<Task> completedList) {
        this.taskAdapterOngoing = new TaskAdapter(ongoingList);
        this.rvOngoing.setAdapter(this.taskAdapterOngoing);
        this.taskAdapterOngoing.notifyDataSetChanged();

        this.taskAdapterCompleted = new TaskAdapter(completedList);
        this.rvCompleted.setAdapter(this.taskAdapterCompleted);
        this.taskAdapterCompleted.notifyDataSetChanged();
    }

    /**
     * Filters the user's tasks based on their category
     * @param category the chosen category to display
     */
    private void filterTask (String category) {
        if (category.equalsIgnoreCase("ALL")) {
            setTaskLists(this.ongoingList, this.completedList);
        }
        else {
            ArrayList<Task> filteredCompleted = new ArrayList<>();
            ArrayList<Task> filteredOngoing = new ArrayList<>();

            for (int i = 0; i < this.completedList.size(); i++)
                if (this.completedList.get(i).getCategory().equalsIgnoreCase(category))
                    filteredCompleted.add(this.completedList.get(i));

            for (int i = 0; i < this.ongoingList.size(); i++)
                if (this.ongoingList.get(i).getCategory().equalsIgnoreCase(category))
                    filteredOngoing.add(this.ongoingList.get(i));

            setTaskLists(filteredOngoing, filteredCompleted);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.initInfo(getView());
    }
}