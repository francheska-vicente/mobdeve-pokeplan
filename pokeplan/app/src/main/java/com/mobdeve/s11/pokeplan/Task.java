package com.mobdeve.s11.pokeplan;

import java.util.Date;

public class Task {
    private final String[] categories =
            {"School", "Work", "Hobby", "Leisure", "Chores", "Health", "Social", "Others"};

    private String taskName;
    private int priority;
    private String category;
    private CustomDate startDate;
    private CustomDate endDate;
    private String description;

    public Task (String taskName, int priority, String category, CustomDate startDate, CustomDate endDate, String description) {
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

    public CustomDate getStartDate () {
        return this.startDate;
    }

    public CustomDate getEndDate () {
        return this.endDate;
    }


    public String getDescription () {
        return this.description;
    }
}
