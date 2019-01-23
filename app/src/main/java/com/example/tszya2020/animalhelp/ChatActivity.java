package com.example.tszya2020.animalhelp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity implements TextView.OnEditorActionListener
{
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

    private String currentUid;
    private User currentAccount;
    private String opposingUid;
    private User opposingUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activty);
        Log.d("inChatActivity", "here");
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        currentUid = getIntent().getStringExtra(Constants.UID_KEY);
        currentUserDBRef = FirebaseDatabase.getInstance().getReference().child(Constants.USER_PATH).child(currentUid);

        currentUserDBRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //EL-changed this so that dataSnapshot works correctly. datasnapshot.getChildren returns a list.
                Log.d("userChildrenData", dataSnapshot.getValue(User.class).toString());
                currentAccount = dataSnapshot.getValue(User.class);

            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.d(LOGGING_NAME, "onCancelled: " + databaseError.getMessage());
            }
        });

//        allUsersDBRef = FirebaseDatabase.getInstance().getReference().child(Constants.USER_PATH);
//        final TaskCompletionSource<String> source = new TaskCompletionSource<>();
//
//        allUsersDBRef.addValueEventListener(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                for(DataSnapshot userSnapshot : dataSnapshot.getChildren())
//                {
//                    //currentAccount = dataSnapshot.getValue(User.class);
//                    Log.d("userChildrenData", userSnapshot.getValue(User.class).toString());
//                    User tempUser = userSnapshot.getValue(User.class);
//                    if(tempUser.getOnline() && !tempUser.getChatting()) //if online but not chatting
//                    {
//                        opposingUser = tempUser;
//                        opposingUid = opposingUser.getUid();
//                        source.setResult(null);
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError)
//            {
//                source.setException(databaseError.toException());
//                Log.d(LOGGING_NAME, "onCancelled: " + databaseError.getMessage());
//            }
//        });



        /*
        setupConnection();
        setupChat();
        adapter = new ChatAdapter();

        chatRoomName = findViewById(R.id.room_name);

        messageInput = findViewById(R.id.chat_input);
        messageInput.setOnEditorActionListener(this);

        setupConnection();

        RecyclerView chat = findViewById(R.id.chat_recycler_view);
        chat.setLayoutManager(linearLayoutManager);
        chat.setAdapter(adapter);*/
    }

    //https://github.com/DeKoServidoni/FirebaseChatAndroid/blob/660ae1b823ad645c921dc4dd57febaa7420841d8/app/src/main/java/com/dekoservidoni/firebasechat/fragments/ChatFragment.java#L78
    //when user presses on "Chat"
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

    //when user presses enter on keyboard
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
    {

        if(!messageInput.getText().toString().isEmpty())
        {
            /*
            Message message = new Message(messageUserId,
                    messageUsername, messageInput.getText().toString());

            chatsRef.child(String.valueOf(new Date().getTime())).setValue(message);
*/
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
                //count = dataSnapshot.getChildrenCount();
                Log.d(LOGGING_NAME, "room number get success");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.e(LOGGING_NAME, "room number get failed: " + databaseError.getMessage());
            }
        });
        /*

        roomReference = FirebaseDatabase.getInstance().getReference(chatRoomName.getText().toString());
        if (chatChild != null)
        {
            chatsRef = roomReference.child(chatChild);

            chatsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(LOGGING_NAME, "Success");

                    adapter.clearContent();

                    //https://firebase.google.com/docs/reference/android/com/google/firebase/database/DataSnapshot.html#getChildren()
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
        }*/
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

    }
}

