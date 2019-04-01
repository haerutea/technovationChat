package com.example.tszya2020.animalhelp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tszya2020.animalhelp.object_classes.Constants;
import com.example.tszya2020.animalhelp.R;
import com.example.tszya2020.animalhelp.object_classes.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener
{
    private String logName;
    private Request sentRequest;
    //UI fields
    private TextView tDescript;
    private Button bAccept;
    private Button bDeny;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_activity);
        logName = "confirmingRequest";
        tDescript = findViewById(R.id.request_description);
        bAccept = findViewById(R.id.request_accept);
        bDeny = findViewById(R.id.request_deny);

        bAccept.setOnClickListener(this);
        bDeny.setOnClickListener(this);

    }

    private void setDescription()
    {
        Constants.BASE_INSTANCE.child(Constants.REQUEST_PATH)
                .addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Log.d(logName, "requesterUid: " + dataSnapshot.getValue().toString());
                //sentRequest = dataSnapshot.getValue(Request.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.d(logName, "onCancelled: " + databaseError.getMessage());
            }
        });
    }
    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        if(id == bAccept.getId())
        {
            //connectChat();
        }
        else if(id == bDeny.getId())
        {

        }
    }
/*
    private void connectChat()
    {
        bothUsersUid = userAccount.getUid() + opposingUser.getUid();
        Constants.BASE_INSTANCE.child(Constants.CHAT_PATH)
                .addListenerForSingleValueEvent(new ValueEventListener()
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
                        intent.putExtra(Constants.UID_KEY, userAccount.getUid());
                        intent.putExtra(Constants.OPPOSING_USER_KEY, opposingUser);
                        intent.putExtra(Constants.CHAT_ROOM_ID_KEY, bothUsersUid);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                    }
                });
    }*/
}