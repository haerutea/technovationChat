package com.example.tszya2020.animalhelp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//https://github.com/DeKoServidoni/FirebaseChatAndroid/blob/master/app/src/main/java/com/dekoservidoni/firebasechat/adapters/ChatAdapter.java
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder>
{
    public static class MessageViewHolder extends RecyclerView.ViewHolder
    {
        TextView username;
        TextView message;

        public MessageViewHolder(View v)
        {
            super(v);
            username = itemView.findViewById(R.id.chat_username);
            message = itemView.findViewById(R.id.chat_message);
        }
    }

    private List<ChatMessage> messageContent;

    public ChatAdapter()
    {
        messageContent = new ArrayList<ChatMessage>();
    }

    public int getItemCount() {
        return messageContent.size();
    }

    public void addChat(ChatMessage chat)
    {
        messageContent.add(chat);
    }

    public void clearContent()
    {
        messageContent.clear();
    }

    @NonNull
    public MessageViewHolder onCreateViewHolder(@NonNull  ViewGroup viewParent, int type)
    {
        LayoutInflater inflater = LayoutInflater.from(viewParent.getContext());
        View view = inflater.inflate(R.layout.chat_message, viewParent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageHolder, int positionIndex) {
        ChatMessage data = messageContent.get(positionIndex);

        messageHolder.username.setText(data.getSenderName());
        messageHolder.message.setText(data.getMessage());
    }

}

