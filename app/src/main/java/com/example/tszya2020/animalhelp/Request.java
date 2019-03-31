package com.example.tszya2020.animalhelp;

import java.util.HashMap;

public class Request
{
    private String uid;
    private String username;
    private HashMap<String, String> preferences;

    public Request(String inUid, String inUsername, HashMap<String, String> inPreferences)
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

    public HashMap<String, String> getPreferences()
    {
        return preferences;
    }
}
