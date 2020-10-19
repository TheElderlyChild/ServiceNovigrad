package com.example.servicenovigrad.userManagement;

/**
 * Represents a user of the account
 */
public class User {
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    public User(String firstName, String lastName, String username, String password) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.username=username;
        this.password=password;
    }

    public String getUsername(){
        return username;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public boolean isPassword(String value){
        return value==password;
    }
}
