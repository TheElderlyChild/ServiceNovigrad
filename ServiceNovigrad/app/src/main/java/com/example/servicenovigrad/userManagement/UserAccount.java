package com.example.servicenovigrad.userManagement;

public abstract class UserAccount {
    protected User user;

    public static UserAccount createAccount(String role, User user){
        switch(role){
            case "Employee":
                return new Employee(user);
            case "Customer":
                return new Customer(user);
            default:
                return null;
        }
    }

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

    public String getPassword(){
        return user.getPassword();
    }

    public String toString(){return user.getUsername() +
            "("+user.getFirstName()+" "+user.getLastName()+")";}

    public boolean isAccount(String username, String password){
        return (username.equals(getUsername()) && user.isPassword(password));
    }
}
