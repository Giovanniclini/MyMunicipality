package com.example.mymunicipality;

import java.io.Serializable;

public class ReportData implements Serializable {

    private String title;
    private String description;
    private String via;
    private String username;
    private Integer priority;

    public ReportData(){

    }

    public ReportData(String title, String description, String via, Integer priority, String username){
        this.title = title;
        this.description = description;
        this.via = via;
        this.username = username;
        this.priority = priority;
    }

    //NON CAMBIARE NOME AI GETTER

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVia() {
        return via;
    }

    public String getUsername(){return  username;}

    public Integer getPriority(){ return priority; }

}
