package com.example.mymunicipality;

import java.io.Serializable;

public class ReportData implements Serializable {

    private String title;
    private String description;
    private String via;

    public ReportData(){
        //Serve un Costruttore vuoto
    }

    public ReportData(String title, String description, String via){
        this.title = title;
        this.description = description;
        this.via = via;
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

}
