package com.example.mymunicipality;

public class ReportData {
    private String title;
    private String description;
    private int priority;

    public ReportData(){
        //Serve un Costruttore vuoto
    }

    public ReportData(String title, String description, int priority){
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

    public int getPriority() {
        return priority;
    }
}
