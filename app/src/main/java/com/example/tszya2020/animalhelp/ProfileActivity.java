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
    private String userPath = "users";
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
        usersRef = FirebaseDatabase.getInstance().getReference().child(Constants.USER_PATH).child(userUid);
        Log.d("userRef", usersRef.toString());
        usersRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {

                    //EL-changed this so that dataSnapshot works correctly. datasnapshot.getChildren returns a list.
                    Log.d("userChildrenData", dataSnapshot.getValue(User.class).toString());
                    userAccount = dataSnapshot.getValue(User.class);
                    Log.d( "email", dataSnapshot.getValue(User.class).getEmail());
                    Log.d("email2", userAccount.getEmail());

                    if(userAccount!= null)
                    {
                        username.setText(userAccount.getUsername());
                        email.setText(userAccount.getEmail());
                    }
                    else
                    {
                        username.setText("Something went wrong");
                        email.setText("user not found");
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Log.d(logging, "onCancelled: " + databaseError.getMessage());
                }
            });


        chat.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        Log.d("user", "clicked on profile");
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
