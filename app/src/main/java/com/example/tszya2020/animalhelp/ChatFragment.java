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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatFragment extends Fragment
        implements TextView.OnEditorActionListener
{

    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private FirebaseAnalytics messageAnalytics;
    private ChatAdapter adapter;
    private FragmentChangeListener mCallback;

    private final String userLoggingName = "UserCreation";
    private final String loggingName = "ChatFragmentDatabase";
    //UI references
    //https://developer.android.com/reference/android/support/v7/widget/LinearLayoutManager
    private LinearLayoutManager linearLayoutManager;
    private EditText messageInput;
    private Button sendButton;

    private String messageUserId;
    private String messageUsername;

    private String usersBranch;
    private String user1;
    private String user2;
    private String opposingUsername;
    private String roomName;

    private int count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        messageAnalytics = FirebaseAnalytics.getInstance(this.getContext());

        adapter = new ChatAdapter();
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setStackFromEnd(true);
        messageUsername = user.getDisplayName();
        messageUserId = user.getUid();

        usersBranch = "users";
        user1 = "user1";
        user2 = "user2";
        count = 1;
        roomName = "room" + count;

        setupConnection();
    }


    //https://github.com/DeKoServidoni/FirebaseChatAndroid/blob/660ae1b823ad645c921dc4dd57febaa7420841d8/app/src/main/java/com/dekoservidoni/firebasechat/fragments/ChatFragment.java#L78
    //when user presses on "Chat"
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View baseView = inflater.inflate(R.layout.chat_fragment, container, false);

        messageInput = baseView.findViewById(R.id.chat_input);
        messageInput.setOnEditorActionListener(this);

        RecyclerView chat = baseView.findViewById(R.id.chat_recycler_view);
        chat.setLayoutManager(linearLayoutManager);
        chat.setAdapter(adapter);

        return baseView;
    }

    //when user presses enter on keyboard
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
    {

        if(!messageInput.getText().toString().isEmpty())
        {
            ChatMessage message = new ChatMessage(messageUserId,
                    messageUsername, messageInput.getText().toString());

            databaseReference.child(String.valueOf(new Date().getTime())).setValue(message);

            linearLayoutManager.scrollToPosition(adapter.getItemCount() - 1);

            messageInput.setText("");
        }
        return true;
    }

    //connecting to database
    private void setupConnection()
    {

        databaseReference = FirebaseDatabase.getInstance().getReference(roomName);
        databaseReference.child(usersBranch).push();
        databaseReference.child(usersBranch).child(user1).push();
        databaseReference.child(usersBranch).child(user2).push();

        final DatabaseReference usersTab = databaseReference.child("users");

        usersTab.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(userLoggingName, "Failed: " + databaseError.getMessage());
            }
        });
        /*
        usersTab.child("user1").push().setValue(messageUserId);
        usersTab.child("user2");*/

        databaseReference.addValueEventListener(new ValueEventListener()
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
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(loggingName,"Failed. Error: " + databaseError.getMessage());
                Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* getting rid of send_button
    public void onClick(View view) {
        ChatMessage chatMessage = new ChatMessage(messageUserId,
                messageUsername, messageInput.getText().toString());
        databaseReference.child("messages").push().setValue(chatMessage);
        messageInput.setText("");
        messageAnalytics.logEvent("message_sent", null);
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (FragmentChangeListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //https://stackoverflow.com/a/36185703
        databaseReference.removeValue();
        mCallback = null;
    }
}