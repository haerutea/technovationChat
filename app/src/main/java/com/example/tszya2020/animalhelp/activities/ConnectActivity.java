package com.example.tszya2020.animalhelp.activities;

import android.app.ProgressDialog;
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

public class ConnectActivity extends AppCompatActivity implements View.OnClickListener
{

    //variables
    private final String LOG_TAG = "ConnectActivity";
    private User userAccount;
    private User opposingUser;
    private Chat newChatlog;
    private String bothUsersUid;
    private final String AGE = "age";
    private String selectedAgeGroup;
    private final String CATEGORY ="category";
    private String selectedCategory;
    private final String LANGUAGE = "language";
    private String selectedLanguage;
    private DatabaseReference allUsersDBRef;

    //UI
    private Button bFind;
    private Spinner ageDropdown;
    private Spinner contentDropdown;
    private Spinner languageDropdown;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);

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
                (ConnectActivity.this, android.R.layout.simple_spinner_item, Constants.LANGUAGE_GROUPS);
        languageDropdown.setAdapter(languageArrAdapter);
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
    }

    private void findOpposingUser()
    {
        final ProgressDialog connecting = DialogUtils
                .showProgressDialog(this, "Attempting to connect to chat");
        allUsersDBRef = Constants.BASE_INSTANCE.child(Constants.USER_PATH);
        final TaskCompletionSource<String> getOpposingUserSource = new TaskCompletionSource<>();

        allUsersDBRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User okUser = null;
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren())
                {
                    Log.d("userChildrenData", userSnapshot.getValue(User.class).toString());
                    User tempUser = userSnapshot.getValue(User.class);
                    boolean age, category, language;
                    //if status-wise they're avail
                    if(!tempUser.getEmail().equals(userAccount.getEmail())
                            && tempUser.getOnline() && !tempUser.getChatting())
                    {
                        ArrayList<String> matching = tempUser.getStrengths();
                        age = matching.contains(selectedAgeGroup);
                        category = matching.contains(selectedCategory);
                        language = matching.contains(selectedLanguage);
                        if(age && category && language) //if user with all 3 matches
                        {
                            opposingUser = tempUser;
                            break;
                        }
                        else if(language && category || language && age)
                        {
                            okUser = tempUser;
                        }
                        else if(language && okUser == null)
                        {
                            okUser = tempUser;
                        }
                    }
                }
                //if no perfect match but there was still a user that was ok
                if(opposingUser == null)
                {
                    opposingUser = okUser;
                }
                Log.d("opposingUser", "found");
                HashMap<String, String> setPreferences = new HashMap<>();
                setPreferences.put(AGE, selectedAgeGroup);
                setPreferences.put(CATEGORY, selectedCategory);
                setPreferences.put(LANGUAGE, selectedLanguage);
                Request chatRequest = new Request(userAccount.getUsername(), setPreferences);
                //add request to database with opposing user's UID as path name
                Constants.BASE_INSTANCE.child(Constants.REQUEST_PATH)
                        .child(opposingUser.getUid()).child(userAccount.getUid()).setValue(true);
                //add request details to userAccount branch
                Constants.BASE_INSTANCE.child(Constants.USER_PATH).child(userAccount.getUid()).child(Constants.REQUEST_PATH).setValue(chatRequest);
                getOpposingUserSource.setResult(null);

                //if there wasn't even an ok one LOL
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
                ProgressDialog waitingForConfirm = DialogUtils.showProgressDialog(ConnectActivity.this,
                        "Opposing user found, waiting for confirmation...");
                waitingForConfirm.show();
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        if (id == bFind.getId())
        {
            findOpposingUser();
        }
    }
}