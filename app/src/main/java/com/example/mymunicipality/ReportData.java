package com.example.mymunicipality;

import java.io.Serializable;

public class ReportData implements Serializable {

    private String title;
    private String description;
    private String priority;
    //private Integer priority;

    public ReportData(){
        //Serve un Costruttore vuoto
    }

    public ReportData(String title, String description, String priority){
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    //NON CAMBIARE NOME AI GETTER

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }
}
