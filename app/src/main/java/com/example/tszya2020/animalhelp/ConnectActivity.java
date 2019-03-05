package com.example.tszya2020.animalhelp;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConnectActivity extends AppCompatActivity implements View.OnClickListener
{

    //variables
    private User userAccount;
    private String userUid;
    private User opposingUser;
    private Chat newChatlog;
    private String bothUsersUid;
    private String selectedAgeGroup;
    private String selectedCategory;
    private DatabaseReference allUsersDBRef;
    private DatabaseReference chatDBRef;

    //dropdown menu choices, these categories are referenced off 7Cups' list
    private final String[] targetAgeGroups =
            {"Teens (18<)", "Young Adults (18-35)", "Adults (35+)", "Everyone"};
    private final String[] categoryGroups =
            {"Depression", "Anxiety", "Stress", "Motivation", "ADHD", "General Mental Health", "Others"};

    //UI
    private Button bFind;
    private Spinner ageDropdown;
    private Spinner contentDropdown;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);

        userAccount = (User) getIntent().getSerializableExtra(Constants.CURRENT_USER_KEY);
        bFind = (Button) findViewById(R.id.find_button);
        bFind.setOnClickListener(this);
        //https://developer.android.com/guide/topics/ui/controls/spinner#java
        //age dropdown menu
        ageDropdown = (Spinner) findViewById(R.id.age_spinner);
        //since items are from an array
        ArrayAdapter<String> ageArrAdapter = new ArrayAdapter<String>(ConnectActivity.this,
                android.R.layout.simple_spinner_item, targetAgeGroups);

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
        contentDropdown = (Spinner) findViewById(R.id.content_spinner);
        //since items are from an array
        ArrayAdapter<String> contentArrAdapter = new ArrayAdapter<String>(ConnectActivity.this,
                android.R.layout.simple_spinner_item, categoryGroups);

        contentArrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageDropdown.setAdapter(contentArrAdapter);
        ageDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
    }

    private void findOpposingUser()
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
                boolean found = false;
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren())
                {
                    //currentAccount = dataSnapshot.getValue(User.class);
                    Log.d("userChildrenData", userSnapshot.getValue(User.class).toString());
                    User tempUser = userSnapshot.getValue(User.class);
                    Log.d("tempUserCurrent", userAccount.getEmail());
                    Log.d("tempUser", tempUser.getEmail());
                    if(tempUser.getEmail().equals(userAccount.getEmail()))
                    {
                        Log.d("tempUser", "temp");
                        continue;
                    }
                    if(tempUser.getOnline() && !tempUser.getChatting()) //if online but not chatting
                    {
                        found = true;
                        Log.d("opposingUser", "found");
                        opposingUser = tempUser;
                        getOpposingUserSource.setResult(null);
                        break;
                        //TODO: IF NO USERS ARE AVAILABLE
                    }
                }
                if(!found)
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
                connectChat();
                connecting.dismiss();
            }
        });

    }

    public void connectChat()
    {

        bothUsersUid = userUid + opposingUser.getUid();
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.CHAT_PATH).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                newChatlog = new Chat(userAccount, opposingUser);
                FirebaseDatabase.getInstance().getReference().child(Constants.CHAT_PATH)
                        .child(bothUsersUid).setValue(newChatlog);
                Log.d("toChatActivity", "fromConnectActivity");
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
    }

    public void onClick(View v)
    {
        int id = v.getId();
        if (id == bFind.getId())
        {
            findOpposingUser();
        }
    }
}
