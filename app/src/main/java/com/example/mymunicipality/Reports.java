package com.example.mymunicipality;

public class Reports {
    public  String title;
    public  String description;
    public String via;
    public String username;
    public Integer priority;

    public Reports(String title, String description, String via, Integer priority, String username) {

        this.title = title;
        this.description = description;
        this.via = via;
        this.priority = priority;
        this.username = username;
    }
}
