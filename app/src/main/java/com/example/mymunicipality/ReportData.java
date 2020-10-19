package com.example.mymunicipality;

import java.io.Serializable;

public class ReportData implements Serializable {

    private String title;
    private String description;
    private String via;
    private Integer priority;

    public ReportData(){

    }

    public ReportData(String title, String description, String via, Integer priority){
        this.title = title;
        this.description = description;
        this.via = via;
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

    public Integer getPriority(){ return priority; }

}
