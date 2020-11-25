package com.example.servicenovigrad.services;

import java.util.ArrayList;

public class ServiceRequest {
    int id;

    public String getBranchName() {
        return branchName;
    }

    public String getCustomerName() {
        return customerName;
    }

    String branchName;
    String customerName;
    Service service;
    ArrayList Requirements;
    ArrayList Information;
    int approved;

    public ServiceRequest(int id, String branchID, String customerID, Service service, ArrayList requirements, ArrayList information, int approved) {
        this.id = id;
        this.branchName = branchID;
        this.customerName = customerID;
        this.service = service;
        Requirements = requirements;
        Information = information;
        this.approved = approved;
    }

    public ServiceRequest(int id, String branchName, String customerName, Service service, int approved) {
        this.id = id;
        this.branchName = branchName;
        this.customerName = customerName;
        this.service = service;
        this.approved = approved;
    }

    public void setRequirements(ArrayList requirements) {
        Requirements = requirements;
    }

    public void setInformation(ArrayList information) {
        Information = information;
    }

    public int getId() {
        return id;
    }


    public Service getService() {
        return service;
    }

    public ArrayList getRequirements() {
        return Requirements;
    }

    public ArrayList getInformation() {
        return Information;
    }
}
