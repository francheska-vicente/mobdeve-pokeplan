package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private ArrayList<Task> tasks;

    public static final String KEY_TASKNAME = "KEY_TASKNAME";
    public static final String KEY_DEADLINE = "KEY_DEADLINE";
    public static final String KEY_CATEGORY = "KEY_CATEGORY";
    public static final String KEY_PRIORITY = "KEY_PRIORITY";
    public static final String KEY_START_DATE = "KEY_START_DATE";
    public static final String KEY_NOTES = "KEY_NOTES";
    public static final String KEY_ID = "KEY_ID";

    public static final String KEY_C_START_DATE = "KEY_C_START_DATE";
    public static final String KEY_C_END_DATE = "KEY_C_END_DATE";
    public static final String KEY_C_START_TIME = "KEY_C_START_TIME";
    public static final String KEY_C_END_TIME = "KEY_C_END_TIME";

    public static final String KEY_NOTIF_WHEN = "KEY_NOTIF_WHEN";
    public static final String KEY_NOTIF_ON = "KEY_NOTIF_ON";
    public static final String KEY_NOTIF_START_TIME = "KEY_NOTIF_START_TIME";
    public static final String KEY_IS_COMPLETED = "KEY_IS_COMPLETED";

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
                i.putExtra(KEY_ID, tasks.get(vh.getBindingAdapterPosition()).getTaskID());
                i.putExtra(KEY_NOTIF_WHEN, tasks.get(vh.getBindingAdapterPosition()).getNotifWhen());
                i.putExtra(KEY_NOTIF_ON, tasks.get(vh.getBindingAdapterPosition()).getIsNotif());
                i.putExtra(KEY_NOTIF_START_TIME, tasks.get(vh.getBindingAdapterPosition()).getBeforeStartTime());
                i.putExtra(KEY_IS_COMPLETED, tasks.get(vh.getBindingAdapterPosition()).getIsFinished());

                Task task = tasks.get(vh.getBindingAdapterPosition());
                CustomDate endDate = task.getEndDate();
                CustomDate startDate = task.getStartDate();

                DecimalFormat formatter = new DecimalFormat("00");

                String sEndDate = formatter.format(endDate.getDay()) + "." + formatter.format(endDate.getMonth()) + "." +
                        endDate.getYear();
                String sStartDate = formatter.format(startDate.getDay()) + "." + formatter.format(startDate.getMonth()) + "." +
                        startDate.getYear();
                String sEndTime = formatter.format(endDate.getHour()) + ":" + formatter.format(endDate.getMinute());
                String sStartTime = formatter.format(startDate.getHour()) + ":" + formatter.format(startDate.getMinute());

                i.putExtra(KEY_C_START_DATE, sStartDate);
                i.putExtra(KEY_C_END_DATE, sEndDate);
                i.putExtra(KEY_C_START_TIME, sStartTime);
                i.putExtra(KEY_C_END_TIME, sEndTime);

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
        holder.setTaskPriority(this.tasks.get(position).getPriority());
    }

    @Override
    public int getItemCount() {
        return this.tasks.size();
    }
}
