package com.example.servicenovigrad.services;

import java.util.ArrayList;

public class Service {
    private int id;
    private String name;
    private ArrayList<DocumentTemplate> requirements;
    private ArrayList<FieldTemplate> information;

    public Service(int id, String name, ArrayList<DocumentTemplate> requirements,
                   ArrayList<FieldTemplate> information) {
        this.id = id;
        this.name = name;
        this.requirements = requirements;
        this.information = information;
    }

    public Service(String name, ArrayList<DocumentTemplate> requirements,
                   ArrayList<FieldTemplate> information) {
        this.name = name;
        this.requirements = requirements;
        this.information = information;
    }

    public String toString(){return name;}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<DocumentTemplate> getRequirements() {
        return requirements;
    }

    public void setRequirements(ArrayList<DocumentTemplate> requirements) {
        this.requirements = requirements;
    }

    public ArrayList<FieldTemplate> getInformation() {
        return information;
    }

    public void setInformation(ArrayList<FieldTemplate> information) {
        this.information = information;
    }
}
