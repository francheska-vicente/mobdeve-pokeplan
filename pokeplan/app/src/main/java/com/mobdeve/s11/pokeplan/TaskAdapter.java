package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private ArrayList<Task> tasks;

    public static final String KEY_TASKNAME = "KEY_TASKNAME";
    public static final String KEY_DEADLINE = "KEY_DEADLINE";
    public static final String KEY_CATEGORY = "KEY_CATEGORY";
    public static final String KEY_PRIORITY = "KEY_PRIORITY";
    public static final String KEY_START_DATE = "KEY_START_DATE";
    public static final String KEY_NOTES = "KEY_NOTES";


    public TaskAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @NotNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.template_task, parent, false);

        TaskViewHolder vh = new TaskViewHolder(view);

        vh.getConstraintLayout().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), TaskDetailsActivity.class);
                i.putExtra(KEY_TASKNAME, tasks.get(vh.getBindingAdapterPosition()).getTaskName());
                i.putExtra(KEY_CATEGORY, tasks.get(vh.getBindingAdapterPosition()).getCategory());
                i.putExtra(KEY_PRIORITY, tasks.get(vh.getBindingAdapterPosition()).getPriority());
                i.putExtra(KEY_DEADLINE, tasks.get(vh.getBindingAdapterPosition()).getEndDate().toString());
                i.putExtra(KEY_START_DATE, tasks.get(vh.getBindingAdapterPosition()).getStartDate().toString());
                i.putExtra(KEY_NOTES, tasks.get(vh.getBindingAdapterPosition()).getDescription());
                view.getContext().startActivity(i);
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TaskViewHolder holder, int position) {
        holder.setTaskIcon(this.tasks.get(position).getCategory());
        holder.setTaskName(this.tasks.get(position).getTaskName());
        holder.setTaskDeadline(this.tasks.get(position).getEndDate());
    }

    @Override
    public int getItemCount() {
        return this.tasks.size();
    }
}
