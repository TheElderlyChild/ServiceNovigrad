package com.example.servicenovigrad.services;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ServiceRequest {
    public static class Document{
        private int docId;
        private Bitmap value;

        public Document(int docId, Bitmap value) {
            this.docId = docId;
            this.value = value;
        }

        public int getDocId() {
            return docId;
        }

        public Bitmap getValue() {
            return value;
        }

        public void setValue(Bitmap value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Document Of Document ID " + docId;
        }
    }

    public static class Field{
        private int fieldId;
        private String value;

        public Field(int fieldId, String value) {
            this.fieldId = fieldId;
            this.value = value;
        }

        public int getFieldId() {
            return fieldId;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Field{" +
                    "fieldId=" + fieldId +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

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
    ArrayList<Document> Requirements;
    ArrayList<Field> Information;
    int approved;


    public ServiceRequest(int id, String branchID, String customerID, Service service,
                          ArrayList<Document> requirements, ArrayList<Field> information, int approved) {
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

    public void setRequirements(ArrayList<Document> requirements) {
        Requirements = requirements;
    }

    public void setInformation(ArrayList<Field> information) {
        Information = information;
    }

    public int getId() {
        return id;
    }


    public Service getService() {
        return service;
    }

    public ArrayList<Document> getRequirements() {
        return Requirements;
    }

    public ArrayList<Field> getInformation() {
        return Information;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public String toString(){
        String result="Request " + String.valueOf(getId()) + " by " + getCustomerName() + " to "+
                getBranchName() + " for " + getService().getName();
        if(approved==1){result+=" (approved)";}
        if(approved==-1){result+=" (rejected)";}
        return result;
    }
}
