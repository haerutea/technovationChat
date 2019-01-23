package com.example.tszya2020.animalhelp;

import java.util.ArrayList;

public class Chat
{
    private String mUserOneName;
    private String mUserOneId;
    private String mUserTwoName;
    private String mUserTwoId;
    private ArrayList<Message> messages;

    public Chat()
    {
    }

    public Chat(String userOneName, String userOneId, String userTwoName, String userTwoId)
    {
        this.mUserOneName = userOneName;
        this.mUserOneId = userOneId;
        this.mUserTwoName = userTwoName;
        this.mUserTwoId = userTwoId;
        this.messages = new ArrayList<>();
    }

    public void addMessage(Message inputMessage)
    {
        messages.add(inputMessage);
    }

    public void setUserOneId(String inputUserOneId)
    {
        this.mUserOneId = inputUserOneId;
    }

    public void setUserOneName(String inputUserOneName)
    {
        this.mUserOneName = inputUserOneName;
    }

    public void setUserTwoId(String inputUserTwoId)
    {
        this.mUserTwoId = inputUserTwoId;
    }

    public void setUserTwoName(String inputUser2Name) {
        this.mUserTwoName = inputUser2Name;
    }

    public ArrayList<Message> getMessages()
    {
        return messages;
    }
    public String getUserOneId()
    {
        return mUserOneId;
    }

    public String getUserOneName()
    {
        return mUserOneName;
    }

    public String getUserTwoId()
    {
        return mUserTwoId;
    }

    public String getUserTwoName()
    {
        return mUserTwoName;
    }

}
