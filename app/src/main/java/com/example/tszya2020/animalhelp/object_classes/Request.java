package com.example.tszya2020.animalhelp.object_classes;

import java.util.HashMap;

public class Request
{
    private HashMap<String, Boolean> uid;
    private String username;
    private HashMap<String, String> preferences;

    public Request()
    {
    }

    public Request(HashMap<String, Boolean> inUid, String inUsername, HashMap<String, String> inPreferences)
    {
        uid = inUid;
        username = inUsername;
        preferences = inPreferences;
    }

    public HashMap<String, Boolean> getUid()
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

    @Override
    public String toString()
    {

        return username + " has requested to chat with you.  Here are the preferences chosen: "
                + preferences.toString();
    }
}