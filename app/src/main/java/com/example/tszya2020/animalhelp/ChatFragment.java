package com.example.tszya2020.animalhelp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ChatFragment extends Fragment
        implements TextView.OnEditorActionListener
{

    private FirebaseUser user;
    private DatabaseReference roomReference;
    private DatabaseReference chatsRef;
    private ChatAdapter adapter;
    private FragmentChangeListener mCallback;

    private final String userLoggingName = "UserCreation";
    private final String loggingName = "ChatFragmentDatabase";

    //UI references
    //https://developer.android.com/reference/android/support/v7/widget/LinearLayoutManager
    private LinearLayoutManager linearLayoutManager;
    private EditText messageInput;
    private Button sendButton;
    private TextView chatRoomName;

    private String messageUserId;
    private String messageUsername;

    private String usersBranch;
    private String user1;
    private String user2;
    private String roomName;
    private String chatChild;
    private boolean newRoom;

    private long count;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();

        linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setStackFromEnd(true);
        messageUsername = user.getDisplayName();
        messageUserId = user.getUid();

        usersBranch = "users";

        /*
        setupConnection();
        setupChat();*/
        adapter = new ChatAdapter();
    }


    //https://github.com/DeKoServidoni/FirebaseChatAndroid/blob/660ae1b823ad645c921dc4dd57febaa7420841d8/app/src/main/java/com/dekoservidoni/firebasechat/fragments/ChatFragment.java#L78
    //when user presses on "Chat"
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View baseView = inflater.inflate(R.layout.chat_fragment, container, false);

        chatRoomName = baseView.findViewById(R.id.room_name);

        messageInput = baseView.findViewById(R.id.chat_input);
        messageInput.setOnEditorActionListener(this);

        setupConnection();

        RecyclerView chat = baseView.findViewById(R.id.chat_recycler_view);
        chat.setLayoutManager(linearLayoutManager);
        chat.setAdapter(adapter);

        return baseView;
/*
        Query thing = FirebaseDatabase.getInstance().getReference().child(roomName);

        FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>().setQuery(messages, Message.class).setLayout(R.layout.message).build();

        ListView messageList = chatActivity.findViewById(R.id.message_list);
        adapter = new FirebaseListAdapter<Message>(options){
            @Override
            protected void populateView(View v, Message model, int position) {
                // Get references to the views of message.xml
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageBody());
                messageUser.setText(model.getUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getTime()));
            }
        };

        messageList.setAdapter(adapter);
        adapter.startListening();*/
    }

    //when user presses enter on keyboard
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
    {

        if(!messageInput.getText().toString().isEmpty())
        {
            ChatMessage message = new ChatMessage(messageUserId,
                    messageUsername, messageInput.getText().toString());

            chatsRef.child(String.valueOf(new Date().getTime())).setValue(message);

            linearLayoutManager.scrollToPosition(adapter.getItemCount() - 1);

            messageInput.setText("");
        }
        return true;
    }

    //connecting to database
    private void setupConnection()
    {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                count = dataSnapshot.getChildrenCount();
                Log.d(loggingName, "room number get success");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.d(loggingName, "room number get failed: " + databaseError.getMessage());
            }
        });

        count = count + 1;
        roomName = "room_" + count;
        chatRoomName.setText(roomName);
        roomReference = FirebaseDatabase.getInstance().getReference(chatRoomName.getText().toString());
        /*
        databaseReference.child(usersBranch).push();
        databaseReference.child(usersBranch).child(user1).push();
        databaseReference.child(usersBranch).child(user2).push();*/

        final DatabaseReference usersTab = roomReference.child(usersBranch);

        usersTab.addListenerForSingleValueEvent(new ValueEventListener()
        {

            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Log.d(userLoggingName, "Success");
                for(DataSnapshot item : dataSnapshot.getChildren())
                {
                    if(item.getValue() == null)
                    {
                        usersTab.child(user1).setValue(messageUserId);
                        return;
                    }
                    else
                    {
                        usersTab.child(user2).setValue(messageUserId);
                    }
                }
            }
            /*
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Log.d(userLoggingName, "add child success");
                if (dataSnapshot.getChildrenCount() == 0)
                {
                    usersTab.child(user1).push().setValue(user);
                } else if (dataSnapshot.getChildrenCount() == 1)
                {
                    usersTab.child(user2).push().setValue(user);
                } else {
                    newRoom = true;
                    count = count + 1;
                    roomName = "room_" + count;
                    chatRoomName.setText(roomName);
                    FirebaseDatabase.getInstance().getReference(chatRoomName.getText().toString()).
                            child(usersBranch).child(user1).push().setValue(user);
                    //TODO: SEE THIS IF THIS WORKS? vvv
                }
            }
            */
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.d(userLoggingName, "Failed: " + databaseError.getMessage());
            }
        });

        roomReference = FirebaseDatabase.getInstance().getReference(chatRoomName.getText().toString());
        chatsRef = roomReference.child(chatChild);
        chatsRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Log.d(loggingName,"Success");

                adapter.clearContent();

                //https://firebase.google.com/docs/reference/android/com/google/firebase/database/DataSnapshot.html#getChildren()
                for(DataSnapshot item : dataSnapshot.getChildren())
                {
                    //https://firebase.google.com/docs/reference/android/com/google/firebase/database/DataSnapshot#getValue(java.lang.Class%3CT%3E)
                    ChatMessage data = item.getValue(ChatMessage.class);
                    adapter.addChat(data);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.e(loggingName,"Failed. Error: " + databaseError.getMessage());
                Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* getting rid of send_button
    public void onClick(View view)
    {
        ChatMessage chatMessage = new ChatMessage(messageUserId,
                messageUsername, messageInput.getText().toString());
        databaseReference.child("messages").push().setValue(chatMessage);
        messageInput.setText("");
        messageAnalytics.logEvent("message_sent", null);
    }*/

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mCallback = (FragmentChangeListener) context;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        //https://stackoverflow.com/a/36185703
        roomReference.removeValue();
        mCallback = null;
    }


}