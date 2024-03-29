package com.example.servicenovigrad.services;

public class FieldTemplate{

    private int id;
    private String name;
    private String type;

    public FieldTemplate(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public FieldTemplate(String name, String type) {
        this.name = name;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString(){return getName();}
}
