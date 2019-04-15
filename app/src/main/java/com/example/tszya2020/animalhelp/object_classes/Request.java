package com.example.tszya2020.animalhelp.object_classes;

import java.util.HashMap;
import java.util.Map;

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

    public String getUidString()
    {
        String neededUid = null;
        for(String key : uid.keySet())
        {
            neededUid = key;
        }
        return neededUid;
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
        StringBuilder desc = new StringBuilder();
        for(Map.Entry<String, String> data : preferences.entrySet())
        {
            desc.append(data.getKey());
            desc.append(":");
            desc.append(data.getValue());
            desc.append("\n");
        }

        return username + " has requested to chat with you.  Here are the preferences chosen: \n"
                + desc.toString();
    }
}