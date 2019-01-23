package com.example.tszya2020.animalhelp;

import java.sql.Timestamp;

public class Message {

    private String mSenderName;
    private String mSenderId;
    private String mMessage;
    private Timestamp mTimestamp;

    public Message(String senderId, String senderName, String message)
    {
        this.mSenderId = senderId;
        this.mSenderName = senderName;
        this.mMessage = message;
        this.mTimestamp = new Timestamp(System.currentTimeMillis());
    }

    public void setSenderId(String senderId)
    {
        this.mSenderId = senderId;
    }

    public void setSenderName(String senderName)
    {
        this.mSenderName = senderName;
    }

    public void setMessage(String message)
    {
        this.mMessage = message;
    }

    public String getMessage()
    {
        return mMessage;
    }

    public String getSenderId()
    {
        return mSenderId;
    }

    public String getSenderName()
    {
        return mSenderName;
    }

    public Timestamp getTimestamp()
    {
        return mTimestamp;
    }
}
