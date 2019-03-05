package com.example.tszya2020.animalhelp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ChatActivity extends AppCompatActivity implements TextView.OnEditorActionListener
{
    //TODO: ADD SAVE CHAT FEATURE
    private final String USER_LOGGING_NAME = "UserCreation";
    private final String LOGGING_NAME = "ChatFragmentDatabase";
    private DatabaseReference roomReference;
    private DatabaseReference chatsRef;
    private ChatAdapter adapter;
    private DatabaseReference currentUserDBRef;
    private DatabaseReference allUsersDBRef;
    private DatabaseReference chatDBRef;

    //UI references
    //https://developer.android.com/reference/android/support/v7/widget/LinearLayoutManager
    private LinearLayoutManager linearLayoutManager;
    private EditText messageInput;
    private Button sendButton;
    private TextView chatRoomName;

    //String referenced
    private String userUid;
    private User userAccount;
    private User opposingUser;
    private String chatRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activty);
        Log.d("inChatActivity", "here");
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        userUid = getIntent().getStringExtra(Constants.UID_KEY);
        userAccount = (User) getIntent().getSerializableExtra(Constants.CURRENT_USER_KEY);
        opposingUser = (User) getIntent().getSerializableExtra(Constants.OPPOSING_USER_KEY);
        chatRoomId = getIntent().getStringExtra(Constants.CHAT_ROOM_ID_KEY);

        setupConnection();

        Chat newChatLog = new Chat(userAccount, opposingUser);
        adapter = new ChatAdapter(newChatLog);

        chatRoomName = findViewById(R.id.room_name);

        messageInput = findViewById(R.id.chat_input);
        messageInput.setOnEditorActionListener(this);

        RecyclerView chat = findViewById(R.id.chat_recycler_view);
        chat.setLayoutManager(linearLayoutManager);
        chat.setAdapter(adapter);
    }

    //when user presses enter on keyboard
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
    {
        if(!messageInput.getText().toString().isEmpty())
        {
            Message message = new Message(userUid,
                    userAccount.getUsername(), messageInput.getText().toString());

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
                Log.d(LOGGING_NAME, "room number get success");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.e(LOGGING_NAME, "room number get failed: " + databaseError.getMessage());
            }
        });

        roomReference = FirebaseDatabase.getInstance().getReference(chatRoomId);

            roomReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(LOGGING_NAME, "Success");

                    adapter.clearContent();

                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        //https://firebase.google.com/docs/reference/android/com/google/firebase/database/DataSnapshot#getValue(java.lang.Class%3CT%3E)
                        Message data = item.getValue(Message.class);
                        adapter.addChat(data);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(LOGGING_NAME, "Failed. Error: " + databaseError.getMessage());
                    Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                }
            });
        }
/*
     //getting rid of send_button
    public void onClick(View view)
    {
        Message chatMessage = new Message(messageUserId,
                messageUsername, messageInput.getText().toString());
        databaseReference.child("messages").push().setValue(chatMessage);
        messageInput.setText("");
        messageAnalytics.logEvent("message_sent", null);
    }*/

    @Override
    public void onBackPressed()
    {
        roomReference.removeValue();
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra(Constants.UID_KEY, userUid);
        startActivity(intent);
    }
}