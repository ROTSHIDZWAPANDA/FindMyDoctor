package com.example.findmydoctor;

public class Availability {
    private String dayOfWeek;
    private String startTime;
    private String endTime;

    private String calculatedWeekDay;//...........
    private String calculatedDay;
    private String calculatedMonth;

    // Constructors, getters, and setters for existing fields (dayOfWeek, startTime, endTime)

    // Constructors, getters, and setters for calculated date values
    public String getCalculatedWeekDay() {
        return calculatedWeekDay;
    }

    public void setCalculatedWeekDay(String calculatedWeekDay) {
        this.calculatedWeekDay = calculatedWeekDay;
    }

    public String getCalculatedDay() {
        return calculatedDay;
    }

    public void setCalculatedDay(String calculatedDay) {
        this.calculatedDay = calculatedDay;
    }

    public String getCalculatedMonth() {
        return calculatedMonth;
    }

    public void setCalculatedMonth(String calculatedMonth) {
        this.calculatedMonth = calculatedMonth;
    }

    // Default constructor (required for Firebase)
    public Availability() {
    }

    public Availability(String dayOfWeek, String startTime, String endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getter and setter methods
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


}

