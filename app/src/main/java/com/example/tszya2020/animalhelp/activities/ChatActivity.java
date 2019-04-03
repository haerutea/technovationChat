package com.example.tszya2020.animalhelp.activities;

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

import com.example.tszya2020.animalhelp.UserSharedPreferences;
import com.example.tszya2020.animalhelp.object_classes.Chat;
import com.example.tszya2020.animalhelp.ChatAdapter;
import com.example.tszya2020.animalhelp.object_classes.Constants;
import com.example.tszya2020.animalhelp.object_classes.Message;
import com.example.tszya2020.animalhelp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ChatActivity extends AppCompatActivity implements TextView.OnEditorActionListener
{
    //TODO: ADD SAVE CHAT FEATURE
    private final String LOG_TAG = "ChatFragmentDatabase";
    private DatabaseReference roomReference;
    private DatabaseReference messageRef;
    private ChatAdapter adapter;

    //UI references
    //https://developer.android.com/reference/android/support/v7/widget/LinearLayoutManager
    private LinearLayoutManager linearLayoutManager;
    private EditText messageInput;
    private Button sendButton;
    private TextView chatRoomName;

    //objects referenced
    private String userUid;
    private String chatRoomId;
    private String username;
    private Chat newChatLog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        Log.d("inChatActivity", "here");
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        userUid = UserSharedPreferences.getInstance(this).getStringInfo(Constants.UID_KEY);
        username = UserSharedPreferences.getInstance(this).getStringInfo(Constants.USERNAME_KEY);

        chatRoomId = getIntent().getStringExtra(Constants.CHAT_ROOM_ID_KEY);
        newChatLog = (Chat) getIntent().getSerializableExtra(Constants.CHAT_OBJECT_KEY);
        setupConnection();

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
                    username, messageInput.getText().toString());

            messageRef.child(String.valueOf(new Date().getTime())).setValue(message);

            linearLayoutManager.scrollToPosition(adapter.getItemCount() - 1);

            messageInput.setText("");
        }
        return true;
    }

    //connecting to database
    private void setupConnection()
    {
        roomReference = Constants.BASE_INSTANCE.child(Constants.CHAT_PATH).child(chatRoomId);
        messageRef = roomReference.child(Constants.MESSAGE_PATH);
            messageRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(LOG_TAG, "Success");

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
                    Log.e(LOG_TAG, "Failed. Error: " + databaseError.getMessage());
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
        startActivity(intent);
    }
}