package com.example.tszya2020.animalhelp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tszya2020.animalhelp.obsoleteForNow.FragmentChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
    private String bothUsersUid;

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
        final TaskCompletionSource<String> getUserSource = new TaskCompletionSource<>();

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
                        getUserSource.setResult(null);
                    }
                }
                    //EL-changed this so that dataSnapshot works correctly. datasnapshot.getChildren returns a list.

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    getUserSource.setException(databaseError.toException());
                    Log.d(logging, "onCancelled: " + databaseError.getMessage());
                }
            });

        getUserSource.getTask().addOnCompleteListener(new OnCompleteListener<String>()
        {
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
        final ProgressDialog connecting = ProgressDialogUtils
                .showProgressDialog(this, "Attempting to connect to chat");
        allUsersDBRef = FirebaseDatabase.getInstance().getReference().child(Constants.USER_PATH);
        final TaskCompletionSource<String> getOpposingUserSource = new TaskCompletionSource<>();

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
                        Log.d("opposingUser", "found");
                        opposingUser = tempUser;
                        getOpposingUserSource.setResult(null);
                        break;
                        //TODO: IF NO USERS ARE AVAILABLE
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                getOpposingUserSource.setException(databaseError.toException());
                Log.d("connecting to user", "onCancelled: " + databaseError.getMessage());
            }
        });

        //when system gets opposing user to chat with
        getOpposingUserSource.getTask().addOnCompleteListener(new OnCompleteListener<String>()
        {
            @Override
            public void onComplete(@NonNull Task<String> task)
            {
                connectChat();
                connecting.dismiss();
            }
        });

    }

    public void connectChat()
    {
        newChatlog = new Chat(userAccount.getUsername(), userAccount.getUid(),
                opposingUser.getUsername(), opposingUser.getUid());
        bothUsersUid = userUid + opposingUser.getUid();
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.CHAT_PATH).addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        FirebaseDatabase.getInstance().getReference().child(Constants.CHAT_PATH)
                                .child(bothUsersUid).setValue(newChatlog);
                        Log.d("toChatActivity", "fromProfileActivity");
                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        //https://stackoverflow.com/a/2736612
                        intent.putExtra(Constants.CURRENT_USER_KEY, userAccount);
                        //TODO: FIGURE OUT IF ALL THESE IS NEEDED vvv
                        intent.putExtra(Constants.UID_KEY, userUid);
                        intent.putExtra(Constants.OPPOSING_USER_KEY, opposingUser);
                        intent.putExtra(Constants.CHAT_ROOM_ID_KEY, bothUsersUid);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                    }
                });
                //child(bothUsersUid);
        //chatDBRef.setValue(newChatlog);
    }

    public void onClick(View v)
    {
        Log.d("user", "clicked on profile");
        int id = v.getId();
        if(id == chat.getId())
        {
            findOpposingUser();
        }
        else if(id == logout.getId())
        {
            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            startActivity(intent);
        }
    }
}
