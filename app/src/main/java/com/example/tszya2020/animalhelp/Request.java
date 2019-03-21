package com.example.tszya2020.animalhelp;

import java.util.ArrayList;

public class Request
{
    private String uid;
    private String username;
    private ArrayList<String> preferences;

    public Request(String inUid, String inUsername, ArrayList<String> inPreferences)
    {
        uid = inUid;
        username = inUsername;
        preferences = inPreferences;
    }

    public String getUid()
    {
        return uid;
    }

    public String getUsername()
    {
        return username;
    }

    public ArrayList<String> getPreferences()
    {
        return preferences;
    }
}
