package com.jeffryRaymond.rememberYou.data.database.models;

import java.io.Serializable;

/**
 * Created by Jeffry Raymond on 2018-05-08.
 */

public class TaskItem implements Serializable {
    private long taskID;
    private String taskTitle;
    private String userName;
    private String taskDescription;
    private String userPinCode;
    private String taskDetailedDescription;
    private long taskCategoryId;
    private boolean taskItemCheckbox;

    public TaskItem(String user_name, String user_pin, long id, String title, String description, String detailedDescription, int categoryId, boolean taskCheckbox) {
        this.userName = user_name;
        this.userPinCode = user_pin;
        this.taskID = id;
        this.taskTitle = title;
        this.taskDescription = description;
        this.taskDetailedDescription = detailedDescription;
        this.taskCategoryId = categoryId;
        this.taskItemCheckbox = taskCheckbox;
    }

    public TaskItem() {
    }

    //Getters and Setters for userName
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName1) {
        userName = userName1;
    }

    //Getters and Setters for userPinCode
    public String getUserPinCode() {
        return userPinCode;
    }

    public void setUserPinCode(String userPin) {
        userPinCode = userPin;
    }

    //Getters and Setters for taskID
    public long getTaskID() {
        return taskID;
    }

    public void setTaskID(long id) {
        this.taskID = id;
    }

    //Getters and Setters for taskTitle
    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String title) {
        this.taskTitle = title;
    }

    //Getter and Setters for taskDescription
    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String description) {
        this.taskDescription = description;
    }

    //Getters and setters for taskCategoryID
    public long getTaskCategoryId() {
        return taskCategoryId;
    }

    public void setTaskCategoryId(long categoryId) {
        this.taskCategoryId = categoryId;
    }
}
