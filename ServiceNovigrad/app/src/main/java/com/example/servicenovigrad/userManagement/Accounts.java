package com.example.servicenovigrad.userManagement;

import java.util.ArrayList;
public class Accounts {
    private ArrayList<UserAccount> content;

    public Accounts() {
        content=new ArrayList<UserAccount>();
    }

    public void addAccount(UserAccount account){
        content.add(account);
    }

    public UserAccount getAccount(String username, String password){
        for (UserAccount acc : content){
            if (acc.isAccount(username, password)){
                return acc;
            }
        }
        return null;
    }

    public UserAccount createAccount(String role, String username, String password, String firstName, String lastName){
        UserAccount acc = null;
        if (role=="Admin"){
            acc = new Admin(new User(firstName, lastName, username, password));
        }
        else if (role=="Employee"){
            acc = new Employee(new User(firstName, lastName, username, password));
        }
        else if (role=="Customer"){
            acc = new Customer(new User(firstName, lastName, username, password));
        }

        addAccount(acc);
        return acc;
    }
}
