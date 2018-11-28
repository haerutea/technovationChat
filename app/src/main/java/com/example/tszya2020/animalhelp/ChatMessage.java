package com.example.tszya2020.animalhelp;

public class ChatMessage {

    private String mName;
    private String mId;
    private String mMessage;

    public ChatMessage()
    {

    }
    public ChatMessage(String id, String name, String message)
    {
        mId = id;
        mName = name;
        mMessage = message;
    }

    public void setId(String id)
    {
        mId = id;
    }

    public void setName(String name)
    {
        mName = name;
    }

    public void setMessage(String message)
    {
        mMessage = message;
    }

    public String getId()
    {
        return mId;
    }
    public String getName()
    {
        return mName;
    }

    public String getMessage()
    {
        return mMessage;
    }
}
