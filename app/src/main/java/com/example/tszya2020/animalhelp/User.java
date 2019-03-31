package com.example.tszya2020.animalhelp;

import java.io.Serializable;
import java.util.ArrayList;

//https://stackoverflow.com/a/2736612
public class User implements Serializable
{
    //TODO: ADD STATS?  EG. CHAT COUNT, RATING FOR RANKS
    private String uid;
    private String username;
    private String email;
    private ArrayList<String> strengths;
    private boolean onlineStatus;
    private boolean chattingStatus;

    public User()
    {

    }

    public User(String inUid, String inName, String inEmail, ArrayList<String> inStrengths, boolean online, boolean chatting)
    {
        uid = inUid;
        username = inName;
        email = inEmail;
        strengths = inStrengths;
        onlineStatus = online;
        chattingStatus = chatting;
    }

    //TODO: IS ALL THIS NECCESARY IF THERE'S USERSHAREDPREF
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

    public void setStrengths(ArrayList<String> inStrengths)
    {
        this.strengths = inStrengths;
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

    public ArrayList<String> getStrengths()
    {
        return strengths;
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
