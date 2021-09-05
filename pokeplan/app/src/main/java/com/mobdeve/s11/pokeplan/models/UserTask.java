package com.mobdeve.s11.pokeplan.models;

public class UserTask {

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
    private int notifRequestCode;

    /**
     * Class constructor (for database use)
     */
    public UserTask() {}

    /**
     * Class constructor for creating a task
     * @param taskID the id of the task
     * @param taskName the name of the task
     * @param priority the priority level of the task, should be an integer from 1-5
     * @param category the category of the task, must be one of 8 categories
     * @param startDate the date when the task will start, can be empty
     * @param endDate the date when the task will end, cannot be empty
     * @param description the notes/additional information of the task, can be empty
     * @param notifWhen the number of minutes before the notification
     * @param beforeStartTime if true, the notif is before start time, otherwise before end time
     * @param isNotif if true, notifications are on, otherwise no notifs
     */
    public UserTask(String taskID, String taskName, int priority, String category,
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
        this.notifRequestCode = 0;
    }

    /**
     * @return the id of the task
     */
    public String getTaskID () {
        return this.taskID;
    }

    /**
     * @return the name of the task
     */
    public String getTaskName () {
        return this.taskName;
    }

    /**
     * @return the priority level of the task
     */
    public int getPriority () {
        return this.priority;
    }

    /**
     * @return the category of the task
     */
    public String getCategory () {
        return this.category;
    }

    /**
     * @return the start date of the task
     */
    public CustomDate getStartDate () {
        return this.startDate;
    }

    /**
     * @return the end date of the task
     */
    public CustomDate getEndDate () {
        return this.endDate;
    }

    /**
     * @return the description of the task
     */
    public String getDescription () {
        return this.description;
    }

    /**
     * @return the priority level of the task
     */
    public boolean getIsFinished() {
        return this.isFinished;
    }

    /**
     * @return  true if notification is before start time;
     *          otherwise, notification is before end time
     */
    public boolean getBeforeStartTime () {
        return this.beforeStartTime;
    }

    /**
     * @return the number of minutes until the notification time
     */
    public String getNotifWhen () {
        return this.notifWhen;
    }

    /**
     * @return  true if notifications are on for this task;
     *          otherwise, notifications are off
     */
    public boolean getIsNotif () {
        return this.isNotif;
    }

    /**
     * @param name the name of the task
     */
    public void setTaskName (String name) {
        this.taskName = name;
    }

    /**
     * @param priority the priority level of the task
     */
    public void setPriority (int priority) {
        this.priority = priority;
    }

    /**
     * @param category the category of the task
     */
    public void setCategory (String category) {
        this.category = category;
    }

    /**
     * @param startDate the start date and time of the task
     */
    public void setStartDate (CustomDate startDate) {
        this.startDate = startDate;
    }

    /**
     * @param endDate the end date and time of the task
     */
    public void setEndDate (CustomDate endDate) {
        this.endDate = endDate;
    }

    /**
     * @param description the description of the task
     */
    public void setDescription (String description) {
        this.description = description;
    }

    /**
     * @param finished true if the task is finished; otherwise, task is not finished
     */
    public void setIsFinished(boolean finished) {
        this.isFinished = finished;
    }

    /**
     * @param ID the ID of the task
     */
    public void setTaskID (String ID) {
        this.taskID = ID;
    }

    /**
     * @param isNotif true if notifications are on for this task; otherwise, notifications are off
     */
    public void setIsNotif (boolean isNotif) {
        this.isNotif = isNotif;
    }

    /**
     * @param when true if notification is before start time; otherwise, notification is before end time
     */
    public void setBeforeStartTime (boolean when) {
        this.beforeStartTime = when;
    }

    /**
     * @param when the number of minutes until the notification time
     */
    public void setNotifWhen (String when) {
        this.notifWhen = when;
    }

    public int getNotifRequestCode () {
        return this.notifRequestCode;
    }

    public void setNotifRequestCode (int code) {
        this.notifRequestCode = code;
    }
}
