package com.example.tszya2020.animalhelp.object_classes;

import java.util.HashMap;

public class Request
{
    private String username;
    private HashMap<String, String> preferences;

    public Request(String inUsername, HashMap<String, String> inPreferences)
    {
        username = inUsername;
        preferences = inPreferences;
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
