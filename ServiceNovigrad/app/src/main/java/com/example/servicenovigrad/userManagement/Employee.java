package com.example.servicenovigrad.userManagement;

import com.example.servicenovigrad.services.Service;

import java.time.LocalTime;
import java.util.ArrayList;

public class Employee extends UserAccount {
    public ArrayList<Workday> getWorkdays() {
        return workdays;
    }

    public void setWorkdays(ArrayList<Workday> workdays) {
        this.workdays = workdays;
    }

    public static class Offering{
        private Service service;
        private boolean isProvided;

        public Offering(Service service, boolean isProvided){
            this.service=service;
            this.isProvided=isProvided;
        }

        public Service getService(){return service;}
        public boolean getIsProvided(){return isProvided;}
        public void setIsProvided(boolean newIsProvided){this.isProvided=newIsProvided;}
        public String toString(){
            return service.toString() + (isProvided ? " (Offered)" : " (Not Offered)");}
    }

    private LocalTime startTime;
    private LocalTime endTime;
    private ArrayList<Workday> workdays;


    public Employee(User user){
        this.user=user;
    }

    @Override
    public String getRole() {
        return "Employee";
    }

    public LocalTime getStartTime(){return startTime;}
    public LocalTime getEndTime(){return endTime;}
    public void setStartTime(LocalTime newTime){startTime=newTime;}
    public void setEndTime(LocalTime newTime){endTime=newTime;}
}
