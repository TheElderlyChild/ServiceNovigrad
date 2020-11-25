package com.example.servicenovigrad.userManagement;

import java.util.ArrayList;

public class Workday {
    public static String[] daysInString = new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday",
        "Saturday","Sunday"};

    int day;
    boolean available;

    public Workday(int day, boolean available){
        this.day=day;
        this.available=available;
    }


    public void setDay(int day) {
        this.day = day;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getDay() {
        return day;
    }

    public boolean isAvailable() {
        return available;
    }

    public String toString(){
        return daysInString[day] + (isAvailable() ? " (Open)" : " (Closed)");
    }

    public static ArrayList<Workday> parseWorkdays(String values){
        ArrayList<Workday> alw = new ArrayList<Workday>();
        if (values==null || values.length()!=7){return null;}
        for(int dayInt=0 ; dayInt<=6; dayInt++){
            alw.add(new Workday(dayInt,values.charAt(dayInt)=='1'));
        }
        return alw;
    }

    public static String toCompressedString(ArrayList<Workday> alw){
        String result = "";
        for (Workday workday : alw){
            result = result + (workday.isAvailable()?"1":"0");
        }
        return result;
    }
}
