package com.example.tszya2020.animalhelp;

import java.io.Serializable;

//https://stackoverflow.com/a/2736612
public class User implements Serializable
{
    //TODO: ADD STATS?  EG. CHAT COUNT, RATING FOR RANKS
    private String uid;
    private String username;
    private String email;
    private String rank;
    private boolean onlineStatus;
    private boolean chattingStatus;

    public User()
    {

    }

    public User(String inUid, String inName, String inEmail, String inRank, boolean online, boolean chatting)
    {
        uid = inUid;
        username = inName;
        email = inEmail;
        rank = inRank;
        onlineStatus = online;
        chattingStatus = chatting;
    }

    public void setUid(String inUid)
    {
        this.uid = inUid;
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

    public void setOnline(boolean inputOnline)
    {
        this.onlineStatus = inputOnline;
    }

    public void setChatting(boolean inputChatting)
    {
        this.chattingStatus = inputChatting;
    }

    public String getUid()
    {
        return uid;
    }
    
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

    public boolean getOnline()
    {
        return onlineStatus;
    }

    public boolean getChatting()
    {
        return chattingStatus;
    }
}
