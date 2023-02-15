package com.example.csguide;

public class CSItem {

    protected String name;
    protected String description;

    public CSItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CSItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CSItem{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
