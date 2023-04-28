package com.example.myapplication;

import java.util.Date;
//cmd+n and cmd+a
public class EventsStore {
    public int id,notificationID;
    public String title,remindMe,description;
    public int allDay;
    public String startDate,endDate,actualStartDate;
    public String startTime,endTime,context;
    public EventsStore(int id,String title, String remindMe, String description, int allDay, String startDate, String endDate,String actualStartDate, String startTime, String endTime,String context) {
        this.id = id;
        this.title = title;
        this.remindMe = remindMe;
        this.description = description;
        this.allDay = allDay;
        this.startDate = startDate;
        this.actualStartDate = actualStartDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.context = context;
    }
    public EventsStore(int id,String title, String remindMe, String description, int allDay, String startDate, String endDate,String actualStartDate, String startTime, String endTime,String context,int notificationID) {
        this.id = id;
        this.title = title;
        this.remindMe = remindMe;
        this.description = description;
        this.allDay = allDay;
        this.startDate = startDate;
        this.actualStartDate = actualStartDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.context = context;
        this.notificationID = notificationID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemindMe() {
        return remindMe;
    }

    public void setRemindMe(String remindMe) {
        this.remindMe = remindMe;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int isAllDay() {
        return allDay;
    }

    public void setAllDay(int allDay) {
        this.allDay = allDay;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getActualStartDate() {
        return actualStartDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setHour(String hour) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setMin(String min) {
        this.endTime = endTime;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    @Override
    public String toString() {
        return "EventsStore{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", remindMe='" + remindMe + '\'' +
                ", description='" + description + '\'' +
                ", allDay=" + allDay +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", actualStartDate='" + actualStartDate + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}
