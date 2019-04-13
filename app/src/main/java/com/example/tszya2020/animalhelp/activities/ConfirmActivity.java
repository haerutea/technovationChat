package com.example.tszya2020.animalhelp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tszya2020.animalhelp.UserSharedPreferences;
import com.example.tszya2020.animalhelp.object_classes.Chat;
import com.example.tszya2020.animalhelp.object_classes.Constants;
import com.example.tszya2020.animalhelp.R;
import com.example.tszya2020.animalhelp.object_classes.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * special activity that can only be accessed through chat request notification,
 * allows user to view request details, accept or deny.
 */
public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener
{
    private String currentUid;
    private String logName;
    private String requesterUid;
    private Request sentRequest;
    private DatabaseReference requestRef;

    //UI fields
    private TextView tDescript;
    private Button bAccept;
    private Button bDeny;

    /**
     * when activity is first opened, set content from confirm_activity.xml layout,
     * assigns views to fields, sets description of textview, adds listener for buttons
     * @param savedInstanceState data saved from onSaveInstanceState, not used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_activity);

        logName = "confirmingRequest";
        currentUid = UserSharedPreferences.getInstance(ConfirmActivity.this).getStringInfo(Constants.UID_KEY);

        tDescript = findViewById(R.id.request_description);
        bAccept = findViewById(R.id.request_accept);
        bDeny = findViewById(R.id.request_deny);
        bAccept.setOnClickListener(this);
        bDeny.setOnClickListener(this);

        setDescription();
    }

    /**
     * sets description of text view by getting the request details from database,
     * when details are obtained, set textView text.
     */
    private void setDescription()
    {
        final TaskCompletionSource<String> getRequestData = new TaskCompletionSource<>();
        requestRef = Constants.BASE_INSTANCE.child(Constants.REQUEST_PATH)
                .child(currentUid);
        final ValueEventListener descriptListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                sentRequest = dataSnapshot.getValue(Request.class);
                Log.d(logName, "request " + sentRequest);
                requesterUid = sentRequest.getUidString();
                getRequestData.setResult(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        };
        requestRef.addValueEventListener(descriptListener);

        //when code finishes getting uid from database
        getRequestData.getTask().addOnCompleteListener(new OnCompleteListener<String>()
        {
            @Override
            public void onComplete(@NonNull Task<String> task)
            {
                tDescript.setText(sentRequest.toString());
                requestRef.removeEventListener(descriptListener);
            }
        });

    }

    /**
     * triggered when button is clicked
     * @param v view that was clicked on
     */
    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        //if accept button was clicked
        if(id == bAccept.getId())
        {
            connectChat();
        }
        //if deny was clicked
        else if(id == bDeny.getId())
        {
            //TODO: ADD DENY
        }
    }

    /**
     * connects user to chat by creating a Chat object and database branch,
     * then starts ChatActivity
     */
    private void connectChat()
    {
        //removes request
        requestRef.removeValue();

        //create chat room ID
        String bothUsersUid = currentUid + requesterUid;

        //add Chat obj to database
        Chat newChatlog = new Chat(currentUid, requesterUid);
        Constants.BASE_INSTANCE.child(Constants.CHAT_PATH);
        FirebaseDatabase.getInstance().getReference().child(Constants.CHAT_PATH)
                .child(bothUsersUid).setValue(newChatlog);
        Log.d("toChatActivity", "fromConnectActivity");

        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        //https://stackoverflow.com/a/2736612
        intent.putExtra(Constants.CHAT_ROOM_ID_KEY, bothUsersUid);
        intent.putExtra(Constants.CHAT_OBJECT_KEY, newChatlog);
        startActivity(intent);

    }
}