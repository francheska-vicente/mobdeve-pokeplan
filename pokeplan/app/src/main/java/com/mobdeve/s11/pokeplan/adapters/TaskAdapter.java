package com.mobdeve.s11.pokeplan.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.activities.TaskDetailsActivity;
import com.mobdeve.s11.pokeplan.models.CustomDate;
import com.mobdeve.s11.pokeplan.models.UserTask;
import com.mobdeve.s11.pokeplan.utils.Keys;
import com.mobdeve.s11.pokeplan.viewholders.TaskViewHolder;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private ArrayList<UserTask> tasks;

    public TaskAdapter(ArrayList<UserTask> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @NotNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.template_task, parent, false);

        TaskViewHolder vh = new TaskViewHolder(view);

        vh.getConstraintLayout().setOnClickListener(view1 -> {
            Intent i = new Intent(view1.getContext(), TaskDetailsActivity.class);
            i.putExtra(Keys.KEY_TASKNAME.name(), tasks.get(vh.getBindingAdapterPosition()).getTaskName());
            i.putExtra(Keys.KEY_CATEGORY.name(), tasks.get(vh.getBindingAdapterPosition()).getCategory());
            i.putExtra(Keys.KEY_PRIORITY.name(), tasks.get(vh.getBindingAdapterPosition()).getPriority());
            i.putExtra(Keys.KEY_END_DATE.name(), tasks.get(vh.getBindingAdapterPosition()).getEndDate().toString());
            i.putExtra(Keys.KEY_START_DATE.name(), tasks.get(vh.getBindingAdapterPosition()).getStartDate().toString());
            i.putExtra(Keys.KEY_NOTES.name(), tasks.get(vh.getBindingAdapterPosition()).getDescription());
            i.putExtra(Keys.KEY_ID.name(), tasks.get(vh.getBindingAdapterPosition()).getTaskID());
            i.putExtra(Keys.KEY_NOTIF_WHEN.name(), tasks.get(vh.getBindingAdapterPosition()).getNotifWhen());
            i.putExtra(Keys.KEY_NOTIF_ON.name(), tasks.get(vh.getBindingAdapterPosition()).getIsNotif());
            i.putExtra(Keys.KEY_NOTIF_START_TIME.name(), tasks.get(vh.getBindingAdapterPosition()).getBeforeStartTime());
            i.putExtra(Keys.KEY_IS_COMPLETED.name(), tasks.get(vh.getBindingAdapterPosition()).getIsFinished());

            UserTask task = tasks.get(vh.getBindingAdapterPosition());
            CustomDate endDate = task.getEndDate();
            CustomDate startDate = task.getStartDate();

            DecimalFormat formatter = new DecimalFormat("00");

            String sEndDate = formatter.format(endDate.getDay()) + "." + formatter.format(endDate.getMonth()) + "." +
                    endDate.getYear();
            String sStartDate = formatter.format(startDate.getDay()) + "." + formatter.format(startDate.getMonth()) + "." +
                    startDate.getYear();
            String sEndTime = formatter.format(endDate.getHour()) + ":" + formatter.format(endDate.getMinute());
            String sStartTime = formatter.format(startDate.getHour()) + ":" + formatter.format(startDate.getMinute());

            i.putExtra(Keys.KEY_C_START_DATE.name(), sStartDate);
            i.putExtra(Keys.KEY_C_END_DATE.name(), sEndDate);
            i.putExtra(Keys.KEY_C_START_TIME.name(), sStartTime);
            i.putExtra(Keys.KEY_C_END_TIME.name(), sEndTime);
            i.putExtra(Keys.KEY_NOTIF_CODE.name(), tasks.get(vh.getBindingAdapterPosition()).getNotifRequestCode());

            view1.getContext().startActivity(i);
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
