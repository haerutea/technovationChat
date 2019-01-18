package com.example.tszya2020.animalhelp;

import java.sql.Timestamp;

public class ChatMessage {

    private String mSenderName;
    private String mSenderId;
    private String mReceiverName;
    private String mReceiverId;
    private String mMessage;
    private Timestamp mTimestamp;

    public ChatMessage(String id, String name, String message)
    {
        mSenderId = id;
        mSenderName = name;
        mMessage = message;
        mTimestamp = new Timestamp(System.currentTimeMillis());
    }

    public void setSenderId(String senderId)
    {
        mSenderId = senderId;
    }

    public void setSenderName(String senderName)
    {
        mSenderName = senderName;
    }

    public void setmReceiverId(String receiverId)
    {
        mReceiverId = receiverId;
    }

    public void setmReceiverName(String receiverName) {
        this.mReceiverName = receiverName;
    }

    public void setMessage(String message)
    {
        mMessage = message;
    }

    public String getSenderId()
    {
        return mSenderId;
    }
    public String getSenderName()
    {
        return mSenderName;
    }

    public String getReceiverId()
    {
        return mReceiverId;
    }
    public String getReceiverName()
    {
        return mReceiverName;
    }

    public String getMessage()
    {
        return mMessage;
    }

    public Timestamp getTimestamp()
    {
        return mTimestamp;
    }
}
