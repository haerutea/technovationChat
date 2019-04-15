package com.example.tszya2020.animalhelp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SavedMessagesAdapter extends RecyclerView.Adapter<SavedMessagesAdapter.SavedMessageHolder>
{
    public static class SavedMessageHolder extends RecyclerView.ViewHolder
    {
        private TextView messageView;

        public SavedMessageHolder(View v)
        {
            super(v);
            messageView = v.findViewById(R.id.saved_msg);
        }
    }

    private ArrayList<String> allMessages;

    /**
     * constructor, instantiates fields
     * @param inMessages arraylist containing saved messages
     */
    public SavedMessagesAdapter(ArrayList<String> inMessages)
    {
        allMessages = inMessages;
    }

    /**
     * gets the amount of messages there are
     * @return size of allMessages
     */
    @Override
    public int getItemCount()
    {
        return allMessages.size();
    }

    /**
     * called to add new view holders to parent
     * @param viewGroup parent, where the new view will be added
     * @param type view type, not used
     * @return new MessageViewHolder object with the new inflated view
     */
    @NonNull
    @Override
    public SavedMessageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.saved_message, viewGroup, false);
        return new SavedMessageHolder(view);
    }

    /**
     * called to display corresponding info onto viewHolder
     * @param messageViewHolder the MessageViewHolder that needs to be updated
     * @param position position of new item in messageContent
     */
    @Override
    public void onBindViewHolder(@NonNull SavedMessageHolder messageViewHolder, int position)
    {
        String message = allMessages.get(position);
        messageViewHolder.messageView.setText(message);
    }
}
