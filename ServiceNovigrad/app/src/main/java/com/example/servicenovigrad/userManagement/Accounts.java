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
        return null;
    }
}
