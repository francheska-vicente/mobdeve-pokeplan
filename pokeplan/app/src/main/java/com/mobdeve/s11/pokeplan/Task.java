package com.mobdeve.s11.pokeplan;

public class Task {

    private String taskName;
    private int priority;
    private String category;
    private CustomDate startDate;
    private CustomDate endDate;
    private String description;
    private boolean isFinished;
    private String taskID;
    private String notifWhen;
    private boolean beforeStartTime;
    private boolean isNotif;

    public Task (String taskID, String taskName, int priority, String category,
                 CustomDate startDate, CustomDate endDate, String description,
                 String notifWhen, boolean beforeStartTime, boolean isNotif) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.priority = priority;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.isFinished = false;
        this.notifWhen = notifWhen;
        this.beforeStartTime = beforeStartTime;
        this.isNotif = isNotif;
    }

    public Task () {

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

    public String getTaskID () {
        return this.taskID;
    }

    public boolean getIsFinished() {
        return this.isFinished;
    }

    public boolean getBeforeStartTime () {
        return this.beforeStartTime;
    }

    public String getNotifWhen () {
        return this.notifWhen;
    }

    public boolean getIsNotif () {
        return this.isNotif;
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

    public void setIsFinished(boolean finished) {
        this.isFinished = finished;
    }

    public void setTaskID (String ID) {
        this.taskID = ID;
    }

    public void setIsNotif (boolean isNotif) {
        this.isNotif = isNotif;
    }

    public void setBeforeStartTime (boolean when) {
        this.beforeStartTime = when;
    }

    public void setNotifWhen (String when) {
        this.notifWhen = when;
    }
}
