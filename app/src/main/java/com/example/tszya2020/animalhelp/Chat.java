package com.example.tszya2020.animalhelp;

import java.util.ArrayList;

public class Chat
{
    private User userOne;
    private User userTwo;
    private ArrayList<Message> messages;

    public Chat()
    {
    }

    public Chat(User inUserOne, User inUserTwo)
    {
        userOne = inUserOne;
        userTwo = inUserTwo;
        messages = new ArrayList<>();
    }

    public void addMessage(Message inputMessage)
    {
        messages.add(inputMessage);
    }

    public void setUserOne(User inputUserOne)
    {
        this.userOne = inputUserOne;
    }

    public void setUserTwo(User inputUserTwo)
    {
        this.userTwo = inputUserTwo;
    }

    public ArrayList<Message> getMessages()
    {
        return messages;
    }

    public User getUserOne()
    {
        return userOne;
    }

    public User getUserTwo()
    {
        return userTwo;
    }
}
