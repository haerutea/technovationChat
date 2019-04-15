package com.example.tszya2020.animalhelp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tszya2020.animalhelp.object_classes.Chat;
import com.example.tszya2020.animalhelp.object_classes.Constants;
import com.example.tszya2020.animalhelp.object_classes.Message;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

//https://github.com/DeKoServidoni/FirebaseChatAndroid/blob/master/app/src/main/java/com/dekoservidoni/firebasechat/adapters/ChatAdapter.java

/**
 * adapter for ChatActivity's recycler view, contains ViewHolder class within to set each
 * message's view.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder>
{
    /**
     * class for each individual chat message view object
     */
    public static class MessageViewHolder extends RecyclerView.ViewHolder
    {
        private View wholeView;
        private TextView username;
        private TextView message;
        private ImageView star;

        /**
         * assigns view for each field
         * @param v individual chat message view
         */
        public MessageViewHolder(View v)
        {
            super(v);
            wholeView = v;
            username = v.findViewById(R.id.chat_username);
            message = v.findViewById(R.id.chat_message);
            star = v.findViewById(R.id.star_view);
        }

        /**
         * allows user to save or unsave message by tapping on each chat message
         * @param dataObj Message object contained in the view user clicked on
         */
        public void addSaving(final Message dataObj)
        {
            if(dataObj != null)
            {
                wholeView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Log.d("chatObj", "clicked!");
                        String uid = v.getContext().getSharedPreferences(Constants.PREF_USER_INFO, MODE_PRIVATE)
                                .getString(Constants.UID_KEY, "");
                        DatabaseReference savedMsgRef = null;
                        if(uid != null)
                        {
                            savedMsgRef = Constants.BASE_INSTANCE.child(Constants.USER_PATH).child(uid)
                                    .child(Constants.SAVED_MESSAGE_PATH);
                        }

                        String toastMsg = "";

                        if(star.getVisibility() == View.INVISIBLE) //if user wants to save message
                        {

                            toastMsg = "Saved message";
                            star.setVisibility(View.VISIBLE);
                            //save message to DB
                            if(uid != null)
                            {
                                savedMsgRef.child(message.getText().toString()).setValue(true);
                            }
                        }
                        else if(star.getVisibility() == View.VISIBLE) //if user wants to unsave message
                        {
                            toastMsg = "Unsaved message";
                            star.setVisibility(View.INVISIBLE);

                            //remove message from DB
                            if(uid != null)
                            {
                                savedMsgRef.child(message.getText().toString()).removeValue();
                            }
                        }
                        Toast.makeText(v.getContext(), toastMsg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
    private Chat userChatlog;
    private ArrayList<Message> messageContent;

    /**
     * required empty constructor
     */
    public ChatAdapter()
    {

    }

    /**
     * constructor, instantiates fields
     * @param inputChatlog Chat object containing messages
     */
    public ChatAdapter(Chat inputChatlog)
    {
        userChatlog = inputChatlog;
        messageContent = userChatlog.getMessages();
    }

    /**
     * called when new messages are sent, needing to create more views of the new chat messages
     * @param viewParent where the new view will be added
     * @param type view type, not used
     * @return new MessageViewHolder object with the new inflated view
     */
    @NonNull
    public MessageViewHolder onCreateViewHolder(@NonNull  ViewGroup viewParent, int type)
    {
        LayoutInflater inflater = LayoutInflater.from(viewParent.getContext());
        View view = inflater.inflate(R.layout.chat_message, viewParent, false);
        return new MessageViewHolder(view);
    }

    /**
     * called when needed to display new data of the new chat message that was added
     * @param messageHolder the MessageViewHolder that needs to be updated
     * @param positionIndex position of new item in messageContent
     */
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageHolder, int positionIndex) {
        Message data = messageContent.get(positionIndex);
        messageHolder.username.setText(data.getSenderName());
        messageHolder.message.setText(data.getMessage());
        messageHolder.addSaving(data);
    }

    /**
     * gets the amount of messages there are
     * @return size of messageContent
     */
    public int getItemCount()
    {
        return messageContent.size();
    }

    /**
     * adds new message to messageContent
     * @param message new Message object to be added
     */
    public void addChat(Message message)
    {
        messageContent.add(message);
    }

    /**
     * deletes everything in messageContent
     */
    public void clearContent()
    {
        messageContent.clear();
    }
}

