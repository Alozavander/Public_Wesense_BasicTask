package com.example.wesensetasklibrary.dataBeans;


import java.util.Date;

//当前Bean主要直接对接应用的直接使用
public class Bean_Task {
    private Integer taskId;               //任务ID
    private String taskName;              //任务名称
    private Date postTime;                //发布日期
    private Date deadLine;                //截止日期
    private Integer userId;               //任务发布者ID
    private String userName;              //任务发布者的名字
    private Float coin;                   //激励金
    private String describe_task;         //任务描述
    private Integer totalNum;             //该任务的执行总人数
    private Integer taskStatus;           //该任务的执行状态

    public Bean_Task() {
        super();
    }

    public Bean_Task(Integer taskId, String taskName, Date postTime, Date deadLine, Integer userId, String userName, Float coin, String describe_task, Integer totalNum, Integer taskStatus) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.postTime = postTime;
        this.deadLine = deadLine;
        this.userId = userId;
        this.userName = userName;
        this.coin = coin;
        this.describe_task = describe_task;
        this.totalNum = totalNum;
        this.taskStatus = taskStatus;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public Date getPostTime() {
        return postTime;
    }


    public Date getDeadLine() {
        return deadLine;
    }


    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Float getCoin() {
        return coin;
    }

    public String getDescribe_task() {
        return describe_task;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCoin(Float coin) {
        this.coin = coin;
    }

    public void setDescribe_task(String describe_task) {
        this.describe_task = describe_task;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "Bean_Task{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", postTime=" + postTime +
                ", deadLine=" + deadLine +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", coin=" + coin +
                ", describe_task='" + describe_task + '\'' +
                ", totalNum=" + totalNum +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
