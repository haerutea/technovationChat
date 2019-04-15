package com.example.tszya2020.animalhelp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tszya2020.animalhelp.object_classes.Chat;
import com.example.tszya2020.animalhelp.object_classes.Constants;
import com.example.tszya2020.animalhelp.DialogUtils;
import com.example.tszya2020.animalhelp.R;
import com.example.tszya2020.animalhelp.object_classes.Request;
import com.example.tszya2020.animalhelp.object_classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * allows user to select preferences, creates new chat request and add to database,
 * has simple matching algorithm to match user to other users
 */
public class ConnectActivity extends AppCompatActivity implements View.OnClickListener
{

    //variables
    private final String LOG_TAG = "ConnectActivity";
    private User userAccount;
    private User opposingUser;
    private final String AGE = "age";
    private String selectedAgeGroup;
    private final String CATEGORY ="category";
    private String selectedCategory;
    private final String LANGUAGE = "language";
    private String selectedLanguage;
    private String bothUid;
    private ArrayList<User> unavailUsers;
    private CountDownTimer timer;
    private Request chatRequest;
    private DatabaseReference allUsersDBRef;
    private DatabaseReference requestRef;
    private DatabaseReference chatsRef;

    //UI
    private Button bFind;
    private Spinner ageDropdown;
    private Spinner contentDropdown;
    private Spinner languageDropdown;


    /**
     * when activity is first opened, set content from connect_activity.xml,
     * assigns views to fields, populate each spinner (dropdown menu) with different
     * content, add onItemSelectedListener and assign field for item selected in spinner
     * @param savedInstanceState data saved from onSaveInstanceState, not used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);
        unavailUsers = new ArrayList<>();

        userAccount = (User) getIntent().getSerializableExtra(Constants.CURRENT_USER_KEY);

        bFind = findViewById(R.id.find_button);
        bFind.setOnClickListener(this);

        //https://developer.android.com/guide/topics/ui/controls/spinner#java
        //age dropdown menu
        ageDropdown = findViewById(R.id.age_spinner);
        //since items are from an array
        ArrayAdapter<String> ageArrAdapter = new ArrayAdapter<String>(ConnectActivity.this,
                android.R.layout.simple_spinner_item, Constants.TARGET_AGE_GROUPS);
        ageArrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageDropdown.setAdapter(ageArrAdapter);
        //triggered whenever user selects something different
        ageDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedAgeGroup = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        //content dropdown menu
        contentDropdown = findViewById(R.id.content_spinner);
        //since items are from an array
        ArrayAdapter<String> contentArrAdapter = new ArrayAdapter<String>(ConnectActivity.this,
                android.R.layout.simple_spinner_item, Constants.CATEGORY_GROUPS);
        contentArrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contentDropdown.setAdapter(contentArrAdapter);
        //triggered whenever user selects something different
        contentDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        //language dropdown menu
        languageDropdown = findViewById(R.id.language_spinner);
        //items are in an array
        ArrayAdapter<String> languageArrAdapter = new ArrayAdapter<String>
                (ConnectActivity.this, android.R.layout.simple_spinner_dropdown_item, Constants.LANGUAGE_GROUPS);
        languageDropdown.setAdapter(languageArrAdapter);
        //triggered whenever user selects something different
        languageDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedLanguage = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        allUsersDBRef = Constants.BASE_INSTANCE.child(Constants.USER_PATH);
        chatsRef = Constants.BASE_INSTANCE.child(Constants.CHAT_PATH);
    }

    /**
     * called when user presses find user, utilizes a basic for loop to search and match
     * each user with another user.  When another user is found, it then adds the request
     * to the database, which triggers a Cloud Function, and calls waitingToAccept().
     */
    private void findOpposingUser()
    {
        final ProgressDialog connecting = DialogUtils
                .showProgressDialog(this, "Attempting to connect to chat");

        final TaskCompletionSource<String> getOpposingUserSource = new TaskCompletionSource<>();

        allUsersDBRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User okUser = null;
                //iterates through all users
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren())
                {
                    Log.d("userChildrenData", userSnapshot.getValue(User.class).toString());
                    User tempUser = userSnapshot.getValue(User.class);
                    boolean age, category, language;

                    //if status-wise they're available, and they haven't not-accepted a request for this user recently
                    if(!tempUser.getEmail().equals(userAccount.getEmail())
                            && tempUser.getOnline() && !tempUser.getChatting()
                            && !unavailUsers.contains(tempUser))
                    {
                        //match preferences with user's strengths
                        ArrayList<String> matching = tempUser.getStrengths();
                        age = matching.contains(selectedAgeGroup);
                        category = matching.contains(selectedCategory);
                        language = matching.contains(selectedLanguage);

                        //if user with all 3 matches
                        if(age && category && language)
                        {
                            opposingUser = tempUser;
                            break;
                        }
                        //if only 2 match
                        else if(language && category || language && age)
                        {
                            okUser = tempUser;
                        }
                        //if only language matches and no user better than this is found yet
                        else if(language && okUser == null)
                        {
                            okUser = tempUser;
                        }
                    }
                }

                //if a user was found
                if(opposingUser != null || okUser != null)
                {
                    //if no perfect match but there was still a user that was ok
                    if(opposingUser == null)
                    {
                        opposingUser = okUser;
                    }

                    Log.d("opposingUser", "found");

                    //add info into hashmap for Request object
                    HashMap<String, String> setPreferences = new HashMap<>();
                    setPreferences.put(AGE, selectedAgeGroup);
                    setPreferences.put(CATEGORY, selectedCategory);
                    setPreferences.put(LANGUAGE, selectedLanguage);
                    HashMap<String, Boolean> uid = new HashMap<>();
                    uid.put(userAccount.getUid(), true);
                    chatRequest = new Request(uid, userAccount.getUsername(), setPreferences);

                    //add request details to userAccount branch
                    requestRef = Constants.BASE_INSTANCE.child(Constants.REQUEST_PATH)
                            .child(opposingUser.getUid());
                    requestRef.setValue(chatRequest);
                    //add request to database with opposing user's UID as path name
                    getOpposingUserSource.setResult(null);
                }

                //if there wasn't even an ok user
                if(opposingUser == null)
                {
                    Log.d("opposingUser", "not found");
                    connecting.dismiss();
                    Toast.makeText(ConnectActivity.this, "Matching failed, no users available",
                            Toast.LENGTH_SHORT).show();
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
                Log.d("connect", " to chat database");
                connecting.dismiss();
                waitForAccept();
            }
        });
    }

    /**
     * Then, a count down timer for 20 seconds and a listener for chat branch is created.
     * Listener listens for when the user accepts it, and starts ChatActivity accordingly.
     * If more than 20 seconds passed and opposing user did not accept, listener will be removed.
     */
    private void waitForAccept()
    {
        final ProgressDialog waitingForConfirm = DialogUtils.showProgressDialog(ConnectActivity.this,
                "Opposing user found, waiting for confirmation...");
        waitingForConfirm.show();

        final ValueEventListener listener = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                bothUid = opposingUser.getUid() + userAccount.getUid();
                //https://firebase.google.com/docs/reference/android/com/google/firebase/database/DataSnapshot.html#hasChild(java.lang.String)
                if(dataSnapshot.hasChild(bothUid))
                {
                    timer.cancel();
                    connectChat();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        };

        timer = new CountDownTimer(40000, 10000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                Toast.makeText(ConnectActivity.this, (millisUntilFinished/1000) +
                        " seconds left, opposing user has yet to accept", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish()
            {
                //if opposing user didn't accept within 30 seconds
                chatsRef.removeEventListener(listener);
                requestRef.removeValue();
                waitingForConfirm.dismiss();
                unavailUsers.add(opposingUser);
                Toast.makeText(ConnectActivity.this,
                        "Opposing user failed to accept within 40 seconds", Toast.LENGTH_LONG).show();
            }
        };
        timer.start();
        chatsRef.addValueEventListener(listener);
    }

    /**
     * adds information to intent and starts ChatActivity to chat with opposing user
     */
    private void connectChat()
    {
        Chat chatlog = new Chat(opposingUser.getUid(), userAccount.getUid());
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.CHAT_ROOM_ID_KEY, bothUid);
        intent.putExtra(Constants.CHAT_OBJECT_KEY, chatlog);
        startActivity(intent);
    }

    /**
     * triggered when user clicks on view with button
     * @param v view that was clicked on
     */
    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        //if user clicked on find button
        if (id == bFind.getId())
        {
            findOpposingUser();
        }
    }
}