package com.example.servicenovigrad.services;

public class Service {
    private int id;
    private String name;
    private DocumentTemplate[] requirements;
    private FieldTemplate[] information;

    public Service(int id, String name, DocumentTemplate[] requirements, FieldTemplate[] information) {
        this.id = id;
        this.name = name;
        this.requirements = requirements;
        this.information = information;
    }

    public Service(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DocumentTemplate[] getRequirements() {
        return requirements;
    }

    public void setRequirements(DocumentTemplate[] requirements) {
        this.requirements = requirements;
    }

    public FieldTemplate[] getInformation() {
        return information;
    }

    public void setInformation(FieldTemplate[] information) {
        this.information = information;
    }
}
