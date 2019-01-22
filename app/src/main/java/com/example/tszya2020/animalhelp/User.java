package com.example.tszya2020.animalhelp;

import java.sql.Timestamp;

public class User
{
    private String username;
    private String email;
    private String rank;
    //private Status uStatus;

    public User()
    {

    }
    public User(String name, String inEmail, String inRank)
    {
        username = name;
        email = inEmail;
        rank = inRank;
        //uStatus = new Status(true, false);
    }

    public void setUsername(String inUsername)
    {
        this.username = inUsername;
    }

    public void setEmail(String inEmail)
    {
        this.email = inEmail;
    }

    public void setRank(String inputRank)
    {
        this.rank = inputRank;
    }

    /*
    public void setStatus(boolean online, boolean chatting, Timestamp timestamp) {
        uStatus.setIsOnline(online);
        uStatus.setIsChatting(chatting);
        uStatus.setTimestamp(timestamp);
    }*/

    public String getUsername()
    {
        return username;
    }

    public String getEmail()
    {
        return email;
    }

    public String getRank()
    {
        return rank;
    }
}
