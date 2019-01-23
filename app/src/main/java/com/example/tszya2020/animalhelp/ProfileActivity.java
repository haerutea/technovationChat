package com.example.tszya2020.animalhelp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity
        implements View.OnClickListener
{

    //changing from fragment to another: https://developer.android.com/training/basics/fragments/communicating
    private FragmentChangeListener mCallback;
    private String logging = "profileActivity";

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private DatabaseReference allUsersDBRef;
    private DatabaseReference chatDBRef;

    private User userAccount;
    private String userUid;
    private User opposingUser;
    private Chat newChatlog;

    //UI references
    private TextView username;
    private TextView email;
    private Button chat;
    private Button logout;

    @Override
    public final void onCreate(Bundle savedInstanceState)
    {
        Log.d("screen", "profile created");
        setContentView(R.layout.profile_activity);
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);
        chat = findViewById(R.id.chat_button);
        logout = findViewById(R.id.log_out_button);

        //EL - There is no user in the Database at this point. It only lives in FirebaseAuth, not FirebaseDatabase,
        // so if a user can't be found in the database, you would have to get the info from auth and save it in the database.
        //The code bellow always returns null for the user since its never saved to the database.
        userUid = getIntent().getStringExtra(Constants.UID_KEY);
        userRef = FirebaseDatabase.getInstance().getReference().child(Constants.USER_PATH).child(userUid);
        Log.d("userRef", userRef.toString());
        final TaskCompletionSource<String> source = new TaskCompletionSource<>();

        userRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot != null)
                    {
                        Log.d("userChildrenData", dataSnapshot.getValue(User.class).toString());
                        userAccount = dataSnapshot.getValue(User.class);
                        userAccount.setOnline(true);
                        source.setResult(null);
                    }
                }
                    //EL-changed this so that dataSnapshot works correctly. datasnapshot.getChildren returns a list.

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    source.setException(databaseError.toException());
                    Log.d(logging, "onCancelled: " + databaseError.getMessage());
                }
            });

        source.getTask().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(userAccount!= null)
                {
                    username.setText(userAccount.getUsername());
                    email.setText(userAccount.getEmail());
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                    startActivity(intent);
                }
            }
        });

        chat.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    protected void findOpposingUser()
    {
        allUsersDBRef = FirebaseDatabase.getInstance().getReference().child(Constants.USER_PATH);
        final TaskCompletionSource<String> source = new TaskCompletionSource<>();

        allUsersDBRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren())
                {
                    //currentAccount = dataSnapshot.getValue(User.class);
                    Log.d("userChildrenData", userSnapshot.getValue(User.class).toString());
                    User tempUser = userSnapshot.getValue(User.class);
                    if(tempUser.getOnline() && !tempUser.getChatting()) //if online but not chatting
                    {
                        opposingUser = tempUser;
                        source.setResult(null);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                source.setException(databaseError.toException());
                Log.d("connecting to user", "onCancelled: " + databaseError.getMessage());
            }
        });

        //when system gets opposing user to chat with
        source.getTask().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                connectChat();
            }
        });

    }

    public void connectChat()
    {
        newChatlog = new Chat(userAccount.getUsername(), userAccount.getUid(),
                opposingUser.getUsername(), opposingUser.getUid());
        chatDBRef = FirebaseDatabase.getInstance().getReference()
                .child(Constants.CHAT_PATH).child(userUid + opposingUser.getUid());
        chatDBRef.setValue(newChatlog);
        //start intent to activity
    }

    public void onClick(View v)
    {
        Log.d("user", "clicked on profile");
        int id = v.getId();
        if(id == chat.getId())
        {
            Log.d("toChatActivity", "thing");
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra(Constants.UID_KEY, userUid);
            startActivity(intent);
        }
        else if(id == logout.getId())
        {
            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            startActivity(intent);
        }
    }
}
