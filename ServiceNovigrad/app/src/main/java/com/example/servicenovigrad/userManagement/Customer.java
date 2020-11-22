package com.example.servicenovigrad.userManagement;

public class Customer extends UserAccount{
    public Customer(User user){
        this.user=user;
    }

    @Override
    public String getRole() {
        return "Customer";
    }
}
