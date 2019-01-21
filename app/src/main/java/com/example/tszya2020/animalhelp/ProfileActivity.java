package com.example.tszya2020.animalhelp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private User userAccount;
    private String userPath = "users/";
    private String userUid;
    private String logging = "profileActivity";

    //UI references
    private TextView username;
    private TextView email;
    private Button chat;
    private Button logout;

    @Override
    public final void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.profile_activity);
        super.onCreate(savedInstanceState);

        userUid = getIntent().getStringExtra(Constants.UID_KEY);
        usersRef = FirebaseDatabase.getInstance().getReference().child(userPath).child(userUid);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    userAccount = dataSnapshot.getValue(User.class);
                    UserSharedPreferences.getInstance(getApplicationContext()).saveUserInfo(userAccount);
                }
                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Log.d(logging, "onCancelled: " + databaseError.getMessage());
                }
            });

        username = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);
        chat = findViewById(R.id.chat_button);
        logout = findViewById(R.id.log_out_button);

        if(userAccount!= null)
        {
            username.setText(userAccount.getUsername());
            email.setText(userAccount.getEmail());
        }

        chat.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        int id = v.getId();
        if(id == chat.getId())
        {
            Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
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
