package com.example.tszya2020.animalhelp;

import java.sql.Timestamp;

public class User
{
    private String uUsername;
    private String uEmail;
    private String uRank;
    private Status uStatus;

    public User(String name, String inEmail, String inRank)
    {
        uUsername = name;
        uEmail = inEmail;
        uRank = inRank;
        uStatus = new Status(true, false);
    }

    public void setUsername(String inUsername)
    {
        this.uUsername = inUsername;
    }

    public void setEmail(String inEmail)
    {
        this.uEmail = inEmail;
    }

    public void setRank(String inputRank)
    {
        uRank = inputRank;
    }

    public void setStatus(boolean online, boolean chatting, Timestamp timestamp) {
        uStatus.setIsOnline(online);
        uStatus.setIsChatting(chatting);
        uStatus.setTimestamp(timestamp);
    }

    public String getUsername()
    {
        return uUsername;
    }

    public String getEmail()
    {
        return uEmail;
    }

    public String getRank()
    {
        return uRank;
    }
}
