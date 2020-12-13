package com.example.mymunicipality;

public class AppointmentData {

    private String sector;
    private String object;
    private String data;
    private String ora;
    private String username;

    public AppointmentData(){
        
    }

    public AppointmentData(String sector,String object, String data, String ora, String username){
        this.sector = sector;
        this.object = object;
        this.data = data;
        this.ora = ora;
        this.username = username;
    }

    public String getSector() {
        return sector;
    }

    public String getObject() {
        return object;
    }

    public String getData() {
        return data;
    }

    public String getOra() {
        return ora;
    }

    public String getUsername() {
        return username;
    }

}

