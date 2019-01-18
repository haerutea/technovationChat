package com.example.tszya2020.animalhelp;

import java.sql.Timestamp;

public class Status{
    private boolean isOnline;
    private boolean isChatting;
    private Timestamp timestamp;

    public Status(boolean onlineStatus, boolean chattingStatus){
        this.isOnline = onlineStatus;
        this.isChatting = chattingStatus;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public void setIsOnline(boolean online) {
        this.isOnline = online;
    }

    public void setIsChatting(boolean chatting) {
        isChatting = chatting;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public boolean getIsOnline() {
        return isOnline;
    }

    public boolean getIsChatting() {
        return isChatting;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}