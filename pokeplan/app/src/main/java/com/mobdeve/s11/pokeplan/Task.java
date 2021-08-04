package com.mobdeve.s11.pokeplan;

import java.util.Date;

public class Task {
    private String taskName;
    private int priority;
    private String category;
    private Date startDate;
    private Date endDate;
    private String description;

    public Task (String taskName, int priority, String category, Date startDate, Date endDate, String description) {
        this.taskName = taskName;
        this.priority = priority;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    public String getTaskName () {
        return this.taskName;
    }

    public int getPriority () {
        return this.priority;
    }

    public String getCategory () {
        return this.category;
    }

    public Date getStartDate () {
        return this.startDate;
    }

    public Date getEndDate () {
        return this.endDate;
    }

    public String getDescription () {
        return this.description;
    }
}
