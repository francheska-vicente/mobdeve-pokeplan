package com.mobdeve.s11.pokeplan.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.models.CustomDate;

import org.jetbrains.annotations.NotNull;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    private final ImageView ivtaskicon;
    private final TextView tvtaskname;
    private final TextView tvtaskdeadline;
    private final TextView tvtaskpriority;
    private final ConstraintLayout layout;

    /**
     * Class constructor
     * @param itemView the layout of a specific item
     */
    public TaskViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        this.ivtaskicon = itemView.findViewById(R.id.iv_task_icon);
        this.tvtaskname = itemView.findViewById(R.id.tv_task_taskname);
        this.tvtaskdeadline = itemView.findViewById(R.id.tv_task_deadline);
        this.tvtaskpriority = itemView.findViewById(R.id.tv_task_priority);
        this.layout = itemView.findViewById(R.id.cl_template_task);
    }

    /**
     * Sets the task's icon based on its category
     * @param category the category of the task
     */
    public void setTaskIcon(String category) {
        int pic;
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
        this.ivtaskicon.setImageResource(pic);
    }

    /**
     * Sets the task's name in the view's TextView
     * @param name the name of the task
     */
    public void setTaskName(String name) {
        this.tvtaskname.setText(name);
    }

    /**
     * Sets the task's due date in the view's TextView
     * @param dl the due date of the task
     */
    public void setTaskDeadline(CustomDate dl) {
        this.tvtaskdeadline.setText("Due " + dl.toString());
    }

    /**
     * Sets the task's priority icon
     * @param priority the due date of the task
     */
    public void setTaskPriority(int priority) {
        String priorityIcon = "!";
        switch (priority) {
            case 5:
                priorityIcon = "!!!!!";
                break;
            case 4:
                priorityIcon = "!!!!";
                break;
            case 3:
                priorityIcon = "!!!";
                break;
            case 2:
                priorityIcon = "!!";
                break;
        }
        this.tvtaskpriority.setText(priorityIcon);
    }

    /**
     * @return the ConstraintLayout of the template
     */
    public ConstraintLayout getConstraintLayout() {
        return this.layout;
    }
}
