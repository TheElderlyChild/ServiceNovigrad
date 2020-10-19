package com.example.servicenovigrad.userManagement;

public class Employee extends UserAccount {

    public Employee(User user){
        this.user=user;
    }

    @Override
    public String getRole() {
        return "Employee";
    }
}
