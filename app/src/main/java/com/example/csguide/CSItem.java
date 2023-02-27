package com.example.csguide;

public class CSItem {

    private String name;
    private String description;
    private String url;

    public CSItem(String name, String description) {
        this.name = name;
        this.description = description;
        this.url = null;
    }

    public CSItem() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
