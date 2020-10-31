package com.example.mymunicipality;


import java.io.Serializable;

public class VotesData implements Serializable {

    private String title;
    private String username;
    private  Integer votesCount;

    public VotesData(String title, String username, Integer votesCount){

        this.title = title;
        this.username = username;
        this.votesCount = votesCount;
    }

    public void VotesData(){

    }


    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public Integer getVotesCount() {
        return votesCount;
    }
}
