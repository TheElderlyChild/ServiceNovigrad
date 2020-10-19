package com.example.servicenovigrad.userManagement;

public abstract class UserAccount {
    protected User user;
    public abstract String getRole();

    public String getUsername(){
        return user.getUsername();
    }

    public String getFirstName(){
        return user.getFirstName();
    }

    public String getLastName(){
        return user.getLastName();
    }

    public boolean isAccount(String username, String password){
        return (username.equals(getUsername())) && (user.isPassword(password));
    }
}
