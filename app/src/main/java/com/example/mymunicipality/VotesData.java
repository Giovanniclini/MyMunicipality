package com.example.mymunicipality;


import java.io.Serializable;

public class VotesData implements Serializable {

    private String title;
    private String username;
    private  long votesCount;

    public VotesData(String title, String username, long votesCount){

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

    public long getVotesCount() {
        return votesCount;
    }
}
