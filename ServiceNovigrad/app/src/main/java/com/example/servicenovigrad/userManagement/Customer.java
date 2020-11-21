package com.example.servicenovigrad.userManagement;

public class Customer extends UserAccount{
    protected Customer(User user){
        this.user=user;
    }

    @Override
    public String getRole() {
        return "Customer";
    }
}
