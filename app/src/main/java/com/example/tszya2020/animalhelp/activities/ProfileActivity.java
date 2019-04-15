package com.example.tszya2020.animalhelp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tszya2020.animalhelp.UserSharedPreferences;
import com.example.tszya2020.animalhelp.object_classes.Constants;
import com.example.tszya2020.animalhelp.R;
import com.example.tszya2020.animalhelp.object_classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * where the profile of user is displayed, also where the user can connect to chat.
 * change it's settings, and see it's saved messages
 */
public class ProfileActivity extends AppCompatActivity
        implements View.OnClickListener
{
    private final String LOG_TAG = "profileActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference userRef;

    private User userAccount;
    private String userUid;

    //UI references
    private TextView verification;
    private TextView username;
    private TextView email;
    private Button chat;
    private Button savedMsg;
    private Button settings;
    private Button logout;

    /**
     * when activity is first opened, set content from profile_activity.xml,
     * gets current FirebaseUser, assigns views to fields, gets User from database,
     * sets view texts, adds info to SharedPreferences, sets onClickListeners to buttons
     * @param savedInstanceState data saved from onSaveInstanceState, not used
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState)
    {
        Log.d("screen", "profile created");
        setContentView(R.layout.profile_activity);
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        verification = findViewById(R.id.profile_verification);
        username = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);
        chat = findViewById(R.id.chat_button);
        savedMsg = findViewById(R.id.saved_msg_button);
        settings = findViewById(R.id.settings_button);
        logout = findViewById(R.id.log_out_button);


        userUid = mUser.getUid();
        userRef = Constants.BASE_INSTANCE.child(Constants.USER_PATH).child(userUid);
        Log.d("userRef", userRef.toString());

        final TaskCompletionSource<String> getUserSource = new TaskCompletionSource<>();

        userRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    userAccount = dataSnapshot.getValue(User.class);
                    userAccount.setOnline(true);
                    getUserSource.setResult(null);
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    getUserSource.setException(databaseError.toException());
                    Log.d(LOG_TAG, "onCancelled: " + databaseError.getMessage());
                }

            });

        //when user ref listener is done
        getUserSource.getTask().addOnCompleteListener(new OnCompleteListener<String>()
        {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(userAccount != null)
                {
                    //show text according to verification status
                    if(mAuth.getCurrentUser().isEmailVerified())
                    {
                        verification.setText(R.string.verified_email);
                    }
                    else
                    {
                        verification.setText(R.string.not_verified_email);
                    }
                    username.setText(getString(R.string.username_text, userAccount.getUsername()));
                    email.setText(getString(R.string.email_text,userAccount.getEmail()));
                    UserSharedPreferences.getInstance(ProfileActivity.this).
                            setInfo(Constants.UID_KEY, userAccount.getUid());
                    UserSharedPreferences.getInstance(ProfileActivity.this).
                            setInfo(Constants.USERNAME_KEY, userAccount.getUsername());
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                    startActivity(intent);
                }
            }
        });

        //adds uid and username to sharedPreferences, it's done here to prevent these details
        //from missing when user logins from another device.

        chat.setOnClickListener(this);
        savedMsg.setOnClickListener(this);
        settings.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    /**
     * triggered when user clicks on something with a onClick listener
     * @param v  the view the user clicked on
     */
    public void onClick(View v)
    {
        int id = v.getId();
        if(id == chat.getId())
        {
            Log.d(LOG_TAG, "clicked on chat");
            Intent intent = new Intent(getApplicationContext(), ConnectActivity.class);
            intent.putExtra(Constants.CURRENT_USER_KEY, userAccount);
            startActivity(intent);
        }
        else if(id == savedMsg.getId())
        {
            Log.d(LOG_TAG, "clicked on saved msg");
            Intent intent = new Intent(getApplicationContext(), SavedMessagesActivity.class);
            intent.putExtra(Constants.UID_KEY, userUid);
            startActivity(intent);

        }
        else if(id == settings.getId())
        {
            Log.d(LOG_TAG, "clicked on settings");
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
        else if(id == logout.getId())
        {
            Log.d(LOG_TAG, "clicked on logout");
            userAccount.setOnline(false);
            userRef.child(Constants.ONLINE_KEY).setValue(false);
            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            startActivity(intent);
        }
    }
}