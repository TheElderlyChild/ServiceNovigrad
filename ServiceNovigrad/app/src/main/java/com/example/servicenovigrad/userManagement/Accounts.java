package com.example.servicenovigrad.userManagement;

import java.util.ArrayList;
public class Accounts {
    private ArrayList<UserAccount> content;

    public Accounts() {
        content=new ArrayList<UserAccount>();
        addAccount(new Admin(new User("Weks","Team", "admin", "admin001")));
    }

    public void addAccount(UserAccount acc){
        content.add(acc);
    }

    public UserAccount getAccount(String username, String password){
        for (UserAccount acc : content){
            if (acc.isAccount(username, password)){
                return acc;
            }
        }
        return null;
    }

    public int getSize(){return content.size();}

    public UserAccount createAccount(String role, String username, String password, String firstName, String lastName){
        UserAccount acc = null;
        if (role.equals("Employee")){
            acc = new Employee(new User(firstName, lastName, username, password));
        }
        else if (role.equals("Customer")){
            acc = new Customer(new User(firstName, lastName, username, password));
        }

        addAccount(acc);
        return acc;
    }
}
