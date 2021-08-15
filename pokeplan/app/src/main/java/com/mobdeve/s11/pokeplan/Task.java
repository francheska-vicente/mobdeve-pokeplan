package com.mobdeve.s11.pokeplan;

public class Task {
    private final String[] categories =
            {"School", "Work", "Hobby", "Leisure", "Chores", "Health", "Social", "Others"};

    private String taskName;
    private int priority;
    private String category;
    private CustomDate startDate;
    private CustomDate endDate;
    private String description;
    private boolean isFinished;

    public Task (String taskName, int priority, String category, CustomDate startDate, CustomDate endDate, String description) {
        this.taskName = taskName;
        this.priority = priority;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.isFinished = false;
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

    public boolean isFinished() {
        return this.isFinished;
    }

    public void setTaskName (String name) {
        this.taskName = name;
    }

    public void setPriority (int priority) {
        this.priority = priority;
    }

    public void setCategory (String category) {
        this.category = category;
    }

    public void setStartDate (CustomDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate (CustomDate endDate) {
        this.endDate = endDate;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public void setFinished(boolean finished) {
        this.isFinished = finished;
    }
}
