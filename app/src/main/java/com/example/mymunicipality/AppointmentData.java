package com.example.mymunicipality;

public class AppointmentData {

    private Object sector;
    private String object_string;
    private String via;
    private String data;
    private String ora;
    private String username;

    public AppointmentData(){
        
    }

    public AppointmentData(Object sector,String object_string, String data, String ora, String username){
        this.sector = sector;
        this.object_string = object_string;
        this.data = data;
        this.ora = ora;
        this.username = username;
    }

    public Object getSector() {
        return sector;
    }

    public String getObject() {
        return object_string;
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

    public String getUsername() {
        return username;
    }

}

