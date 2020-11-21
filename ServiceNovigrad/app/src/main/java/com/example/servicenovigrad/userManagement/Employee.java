package com.example.servicenovigrad.userManagement;

public class Employee extends UserAccount {

    protected Employee(User user){
        this.user=user;
    }

    @Override
    public String getRole() {
        return "Employee";
    }
}
