package com.example.tszya2020.animalhelp;

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

public class ChatFragment extends Fragment
        implements TextView.OnEditorActionListener, ValueEventListener, View.OnClickListener
{

    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private FirebaseAnalytics messageAnalytics;
    private ChatAdapter adapter;

    private final String loggingName = "ChatActivity";
    //UI references
    //https://developer.android.com/reference/android/support/v7/widget/LinearLayoutManager
    private LinearLayoutManager linearLayoutManager;
    private EditText messageInput;
    private Button sendButton;

    private String messageUserId;
    private String messageUsername;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        messageAnalytics = FirebaseAnalytics.getInstance(this.getContext());

        linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setStackFromEnd(true);

        messageUsername = user.getDisplayName();
        messageUserId = user.getUid();

        adapter = new ChatAdapter();

        setupConnection();
    }


    //https://github.com/DeKoServidoni/FirebaseChatAndroid/blob/660ae1b823ad645c921dc4dd57febaa7420841d8/app/src/main/java/com/dekoservidoni/firebasechat/fragments/ChatFragment.java#L78
    //when a new chat is sent and a new view needs to be created
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View baseView = inflater.inflate(R.layout.chat_fragment, container, false);

        messageInput = baseView.findViewById(R.id.chat_input);
        messageInput.setOnEditorActionListener(this);

        sendButton = baseView.findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);

        RecyclerView chat = baseView.findViewById(R.id.chat_recycler_view);
        chat.setLayoutManager(linearLayoutManager);
        chat.setAdapter(adapter);

        return baseView;
    }

    //when user presses enter on keyboard
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
    {

        ChatMessage message = new ChatMessage(messageUserId,
                messageUsername, messageInput.getText().toString());

        databaseReference.child(String.valueOf(new Date().getTime())).setValue(message);

        messageInput.setText("");
        return true;
    }

    //connecting to database
    private void setupConnection() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Log.d(loggingName,"Success");

        adapter.clearContent();

        //https://firebase.google.com/docs/reference/android/com/google/firebase/database/DataSnapshot.html#getChildren()
        for(DataSnapshot item : dataSnapshot.getChildren()) {
            //https://firebase.google.com/docs/reference/android/com/google/firebase/database/DataSnapshot#getValue(java.lang.Class%3CT%3E)
            ChatMessage data = item.getValue(ChatMessage.class);
            adapter.addChat(data);
        }
        adapter.notifyDataSetChanged();
    }

    public void onClick(View view) {
        ChatMessage chatMessage = new ChatMessage(messageUserId,
                messageUsername, messageInput.getText().toString());
        databaseReference.child("messages").push().setValue(chatMessage);
        messageInput.setText("");
        messageAnalytics.logEvent("message_sent", null);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.e(loggingName,"Failed. Error: " + databaseError.getMessage());
        Toast.makeText(this.getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
    }
}