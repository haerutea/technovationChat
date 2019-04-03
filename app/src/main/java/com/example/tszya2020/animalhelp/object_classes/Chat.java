package com.example.tszya2020.animalhelp.object_classes;

import java.io.Serializable;
import java.util.ArrayList;

//https://stackoverflow.com/a/2736612
public class Chat implements Serializable
{
    private String userOne;
    private String userTwo;
    private ArrayList<Message> messages;

    public Chat()
    {
    }

    public Chat(String inUserOne, String inUserTwo)
    {
        userOne = inUserOne;
        userTwo = inUserTwo;
        messages = new ArrayList<>();
    }

    public void addMessage(Message inputMessage)
    {
        messages.add(inputMessage);
    }

    public void setUserOne(String inputUserOne)
    {
        this.userOne = inputUserOne;
    }

    public void setUserTwo(String inputUserTwo)
    {
        this.userTwo = inputUserTwo;
    }

    public ArrayList<Message> getMessages()
    {
        return messages;
    }

    public String getUserOne()
    {
        return userOne;
    }

    public String getUserTwo()
    {
        return userTwo;
    }
}
