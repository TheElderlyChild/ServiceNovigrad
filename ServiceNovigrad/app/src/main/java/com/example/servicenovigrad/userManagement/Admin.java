package com.example.servicenovigrad.userManagement;

public class Admin extends UserAccount{
    protected Admin(User user){
        this.user=user;
    }

    @Override
    public String getRole() {
        return "Admin";
    }

}
