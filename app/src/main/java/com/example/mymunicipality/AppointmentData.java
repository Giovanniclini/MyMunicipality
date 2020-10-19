package com.example.mymunicipality;

public class AppointmentData {

    private String sector;
    private String notes;
    private String via;
    private String data;
    private String ora;

    public AppointmentData(){
        
    }

    public AppointmentData(String sector, String notes, String via, String data, String ora){
        this.sector = sector;
        this.notes = notes;
        this.via = via;
        this.data = data;
        this.ora = ora;
    }

    public String getSector() {
        return sector;
    }

    public String getNotes() {
        return notes;
    }

    public String getVia() {
        return via;
    }

    public String getData() {
        return data;
    }

    public String getOra() {
        return ora;
    }
}
