package com.example.tszya2020.animalhelp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    protected final void onCreate(Bundle savedInstanceState)
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
                    Log.d("userChildrenData", dataSnapshot.getValue(User.class).toString());
                    userAccount = dataSnapshot.getValue(User.class);
                    userAccount.setOnline(true);
                    getUserSource.setResult(null);

                }

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

    public void onClick(View v)
    {
        Log.d("user", "clicked on profile");
        int id = v.getId();
        if(id == chat.getId())
        {
            Intent intent = new Intent(getApplicationContext(), ConnectActivity.class);
            intent.putExtra(Constants.CURRENT_USER_KEY, userAccount);
            startActivity(intent);
        }
        else if(id == logout.getId())
        {
            userAccount.setOnline(false);
            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            startActivity(intent);
        }
    }
}