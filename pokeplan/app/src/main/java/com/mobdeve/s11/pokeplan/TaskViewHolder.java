package com.mobdeve.s11.pokeplan;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    private ImageView ivtaskicon;
    private TextView tvtaskname;
    private TextView tvtaskdeadline;
    private ConstraintLayout layout;

    public TaskViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        this.ivtaskicon = itemView.findViewById(R.id.iv_task_icon);
        this.tvtaskname = itemView.findViewById(R.id.tv_task_taskname);
        this.tvtaskdeadline = itemView.findViewById(R.id.tv_task_deadline);
    }

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

    public void setTaskName(String name) {
        this.tvtaskname.setText(name);
    }

    /* TODO: fix string */
    public void setTaskDeadline(Date dl) {
        this.tvtaskdeadline.setText(dl.toString());
    }

    public ConstraintLayout getConstraintLayout() {
        return this.layout;
    }
}
