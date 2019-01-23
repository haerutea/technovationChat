package com.example.tszya2020.animalhelp;

public class User
{
    private String uid;
    private String username;
    private String email;
    private String rank;
    private boolean onlineStatus;
    private boolean chattingStatus;
    private Chat chatlog;

    public User()
    {

    }

    public User(String inputUid, String name, String inEmail, String inRank, boolean online, boolean chatting)
    {
        uid = inputUid;
        username = name;
        email = inEmail;
        rank = inRank;
        onlineStatus = online;
        chattingStatus = chatting;
        chatlog = new Chat();
    }

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

    public void setRank(String inputRank)
    {
        this.rank = inputRank;
    }

    public void setOnline(boolean inputOnline)
    {
        this.onlineStatus = inputOnline;
    }

    public void setChatting(boolean inputChatting)
    {
        this.chattingStatus = inputChatting;
    }

    public void setChatlog(Chat inputChatlog)
    {
        this.chatlog = inputChatlog;
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

    public String getRank()
    {
        return rank;
    }

    public boolean getOnline()
    {
        return onlineStatus;
    }

    public boolean getChatting()
    {
        return chattingStatus;
    }

    public Chat getChatlog()
    {
        return chatlog;
    }
}
