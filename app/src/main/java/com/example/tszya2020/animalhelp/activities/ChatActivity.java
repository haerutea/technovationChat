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

/**
 * screen where user can chat with another user that was connected
 * through their chat request.
 */
public class ChatActivity extends AppCompatActivity implements TextView.OnEditorActionListener
{
    private final String LOG_TAG = "ChatFragmentDatabase";
    private DatabaseReference roomReference;
    private DatabaseReference messageRef;
    private ChatAdapter adapter;

    //UI references
    //https://developer.android.com/reference/android/support/v7/widget/LinearLayoutManager
    private LinearLayoutManager linearLayoutManager;
    private EditText messageInput;

    //objects referenced
    private String userUid;
    private DatabaseReference userChatStatus;
    private String chatRoomId;
    private String username;
    private Chat newChatLog;

    /**
     * when activity is first created, set content from chat_activity.xml,
     * creates a new linearLayoutManager for chat messages to be displayed.
     * gets current user's uid and username from sharedPreferences,
     * gets chat object from intent and sets up chatAdapter for recyclerView,
     * assigns input to viewId, also changes user chat status to true.
     * @param savedInstanceState data saved from onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        Log.d("inChatActivity", "here");

        //sets EditText
        messageInput = findViewById(R.id.chat_input);
        messageInput.setOnEditorActionListener(this);

        userUid = UserSharedPreferences.getInstance(this).getStringInfo(Constants.UID_KEY);
        username = UserSharedPreferences.getInstance(this).getStringInfo(Constants.USERNAME_KEY);

        //sets user's chat status on database to true
        userChatStatus = Constants.BASE_INSTANCE.child(Constants.USER_PATH).child(userUid)
                .child(Constants.CHATTING_KEY);
        userChatStatus.setValue(true);

        //gets chat room name
        chatRoomId = getIntent().getStringExtra(Constants.CHAT_ROOM_ID_KEY);
        setupConnection();

        //for recyclerView
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        newChatLog = (Chat) getIntent().getSerializableExtra(Constants.CHAT_OBJECT_KEY);
        adapter = new ChatAdapter(newChatLog);
        RecyclerView chat = findViewById(R.id.chat_recycler_view);
        chat.setLayoutManager(linearLayoutManager);
        chat.setAdapter(adapter);
    }

    /**
     * triggered when user presses enter on keyboard after typing message
     * @param textView textView containing text, not used
     * @param i identifier for action, not used
     * @param keyEvent event triggered by enter key
     * @return true after action is processed by code
     */
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
    {
        if(!messageInput.getText().toString().isEmpty())
        {
            //creates new message object
            Message message = new Message(userUid,
                    username, messageInput.getText().toString());

            //adds to database with time as key
            messageRef.child(String.valueOf(new Date().getTime())).setValue(message);

            //scroll to latest message
            linearLayoutManager.scrollToPosition(adapter.getItemCount() - 1);

            //clear editText
            messageInput.setText("");
        }
        return true;
    }

    /**
     * connects to database with the chat room name, constantly listens for new message,
     * updates UI (RecyclerView) through adapter when new message is added to database.
     */
    private void setupConnection()
    {
        roomReference = Constants.BASE_INSTANCE.child(Constants.CHAT_PATH).child(chatRoomId);
        messageRef = roomReference.child(Constants.MESSAGE_PATH);
            messageRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    Log.d(LOG_TAG, "Success");

                    adapter.clearContent();

                    for (DataSnapshot item : dataSnapshot.getChildren())
                    {
                        //https://firebase.google.com/docs/reference/android/com/google/firebase/database/DataSnapshot#getValue(java.lang.Class%3CT%3E)
                        Message data = item.getValue(Message.class);
                        adapter.addChat(data);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    Log.e(LOG_TAG, "Failed. Error: " + databaseError.getMessage());
                    Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                }
            });
        }

    /**
     * when device's back button is pressed, remove the chatlog from database,
     * set user's chat status to false, and go back to ProfileActivity
     */
    @Override
    public void onBackPressed()
    {
        roomReference.removeValue();
        userChatStatus.setValue(false);
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }
}