package com.example.servicenovigrad.userManagement;

import java.time.LocalTime;

public class Employee extends UserAccount {

    private LocalTime startTime;
    private LocalTime endTime;


    public Employee(User user){
        this.user=user;
    }

    @Override
    public String getRole() {
        return "Employee";
    }

    public LocalTime getStartTime(){return startTime;}
    public LocalTime getEndTime(){return endTime;}
    public void setStartTime(LocalTime newTime){}
    public void setEndTime(LocalTime newTime){}
}
